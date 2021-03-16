package com.epam.pop;

import org.apache.spark.api.java.JavaRDD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.util.List;

@Service
public class PopServiceImpl implements PopService {

    @Autowired
    private UserConfig userConfig;

    public List<String> topWords(JavaRDD<String> lines, int wordsNum) {
        return lines.map(String::toLowerCase)
                .flatMap(s -> List.of(s.split(" ")).iterator())
                .filter(w -> !userConfig.garbage.contains(w))
                .mapToPair(w -> new Tuple2<>(w, 1))
                .reduceByKey((a, b) -> a + b)
                .mapToPair(Tuple2::swap)
                .sortByKey(false)
                .map(Tuple2::_2)
                .take(wordsNum);
    }
}
