package com.epam.pop;

import java.util.List;

public interface PoetryAnalyzer {

    /**
     * Returns most popular words from popstar's available lyrics.
     * Lyrics must be placed into texts/popstar directory, as a set of text documents. See available examples.
     *
     * @param popstar name of the popstar (must be equal to corresponding directory with texts). So, if you want
     *                to count words for Madonna lyrics, you must have texts/Madonna dir in the project.
     * @param wordsNum       number of words to return in the list.
     */
    List<String> mostPopularWords(String popstar, int wordsNum);

    int compare(String star1, String star2, int wordsNum);
}
