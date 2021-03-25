package com.epam.queue.task;

import com.epam.queue.tasks.interfaces.Receiver;
import com.epam.queue.tasks.rabbit.simple.SimpleReceiver;
import com.epam.queue.tasks.rabbit.simple.tasks.ImplementReceiverTask;
import com.epam.queue.tasks.utils.HttpClientUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 *  Очереди для автоматизаторов. AMQP. Задание 1.
 *
 *  Задача: реализовать интерфейс {@link com.epam.queue.tasks.interfaces.Receiver} и передать объект в
 *  {@link ImplementReceiverTask#applyReceiver(Receiver)} (смотрите метод {@link #testReceiver()}
 *
 *  Если тест прошел, то значит вы все сделали правильно.
 *
 *  Материалы, которые могут помочь в этом нелегком деле:
 *  <a href="https://www.rabbitmq.com/tutorials/tutorial-one-java.html">
 */
public class ImplementReceiverTaskIT {

    private static final String Q_NAME = "simple-queue-1";

    private final ImplementReceiverTask task = new ImplementReceiverTask(Q_NAME);

    @AfterEach
    public void tearDown() {
        try {
            task.exec.shutdown();
            task.exec.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReceiver() throws IOException, TimeoutException {
        Receiver rcv = new Receiver1();
        task.applyReceiver(rcv);
        task.startSender();
        //Сообщений в очереди оставаться не должно:
        assertEquals(0, HttpClientUtils.getMessageReady(Q_NAME));
    }
}

class Receiver1 implements Receiver {

    private String queueName;

    Connection connection;
    Channel channel;

    public Receiver1() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    @Override
    public void setQueueName(String name) {
        this.queueName = name;
    }

    @Override
    public void receive() {
        try {
            channel.queueDeclare(queueName, false, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        if (Objects.nonNull(channel) && channel.isOpen()) {
            try {
                channel.close();
                System.out.println("Channel closed.");
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
        }
        if (Objects.nonNull(connection) && connection.isOpen()) {
            try {
                connection.close();
                System.out.println("Connection closed.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
