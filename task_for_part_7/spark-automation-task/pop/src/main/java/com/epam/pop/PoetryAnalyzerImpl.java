package com.epam.pop;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PoetryAnalyzerImpl implements PoetryAnalyzer {

    @Autowired
    private PopWordsService popWordsService;

    @Autowired
    private WordData wordData;

    @Value("${rootPath}")
    private String path;

    public List<String> mostPopularWords(String popstar, int wordsNum) {
        Dataset<Row> data = wordData.create(path + popstar + "/*");
        return popWordsService.popTop(data, wordsNum);
    }

    public int compare(String star1, String star2, int wordsNum) {
        List<String> star1Words = mostPopularWords(star1, wordsNum);
        List<String> start2Words = mostPopularWords(star2, wordsNum);
        int size = star1Words.size();
        star1Words.removeAll(start2Words);
        return size - star1Words.size();
    }
}