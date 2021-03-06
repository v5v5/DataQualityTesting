package com.epam.queue.tasks.rabbit.queue.tasks;

import com.epam.queue.tasks.interfaces.Receiver;
import com.epam.queue.tasks.interfaces.Sender;
import com.epam.queue.tasks.rabbit.queue.PersistReceiver;
import com.epam.queue.tasks.rabbit.queue.PersistSender;
import com.epam.queue.tasks.rabbit.simple.tasks.ImplementReceiverTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.*;

public class FixReceiverWithAckTask {

    private static final Logger logger = LoggerFactory.getLogger(ImplementReceiverTask.class);

    public final ExecutorService exec = Executors.newFixedThreadPool(4);

    private final String queueName;
    private final CountDownLatch latch = new CountDownLatch(1);

    public FixReceiverWithAckTask(String queueName) {
        this.queueName = queueName;
    }

    public void startSender() {
        Sender snd = new PersistSender();
        snd.setQueueName(queueName);

        Future<?> sf = exec.submit(() -> {
            for (int i = 0; i < 10; i++) {
                snd.send("my to be ack message " + i);
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                TimeUnit.MILLISECONDS.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
        });
        try {
            sf.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            snd.close();
        }
    }

    public void applyReceiver(Receiver rcv) {
        if (Objects.isNull(rcv)) {
            logger.error("Receiver not provided test will fail!");
            return;
        }
        rcv.setQueueName(queueName);
        exec.submit(() -> {
            rcv.receive();
            try {
                latch.await();
                TimeUnit.MILLISECONDS.sleep(1000);
                rcv.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
