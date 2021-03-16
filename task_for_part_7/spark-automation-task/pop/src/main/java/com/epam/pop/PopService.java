package com.epam.pop;

import org.apache.spark.api.java.JavaRDD;

import java.io.Serializable;
import java.util.List;

/* Provides API to get sorted list of any distributed collection. */
public interface PopService extends Serializable {

    /**
     * Returns sorted list of strings od descending order.
     *
     * @param lines    RDD of text lines, separated by a single space.
     * @param wordsNum number of words to be returned in the list. So, if you have 1000 words in total,
     *                 but need to get only first two most populate words, set wordsNum to 2.
     */
    List<String> topWords(JavaRDD<String> lines, int wordsNum);

}
