package com.epam.pop;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.List;

public interface PopWordsService {

    /**
     * Returns sorted list of strings od descending order.
     *
     * @param lines    RDD of text lines, separated by a single space.
     * @param wordsNum number of words to be returned in the list. So, if you have 1000 words in total,
     *                 but need to get only first two most populate words, set wordsNum to 2.
     */
    List<String> popTop(Dataset<Row> lines, int wordsNum);
}
