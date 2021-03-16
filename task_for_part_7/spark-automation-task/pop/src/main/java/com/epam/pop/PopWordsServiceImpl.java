package com.epam.pop;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static org.apache.spark.sql.functions.*;

@Service
public class PopWordsServiceImpl implements PopWordsService {

    @Autowired
    private UserConfig userConfig;

    @Override
    public List<String> popTop(Dataset<Row> lines, int wordsNum) {
        Dataset<Row> sorted = lines.withColumn("words", lower(column("words")))
                .filter(not(column("words").isin(userConfig.garbage.toArray())))
                .groupBy(column("words")).agg(count("words").as("count"))
                .sort(column("count").desc());

        List<Row> lst = sorted.takeAsList(wordsNum);
        List<String> topX = new ArrayList<>(new TreeSet<>());
        lst.forEach(r -> topX.add(r.getString(0)));

        return topX;
    }
}