package com.epam.pop;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static List<String> getWords(String line) {
        String[] words = line.split(" ");
        return Arrays.stream(words)
                .map(w -> (w.startsWith("(") || w.endsWith(")")) ? w.replace("(", "").replace(")", "") : w)
                .map(w -> (w.startsWith(",") || w.endsWith(",")) ? w.replace(",", "") : w)
                .map(w -> (w.startsWith("‘")) ? w.replace("‘", "") : w)
                .map(w -> {
                    if (w.contains("’")) {
                        if (w.startsWith("’") || w.endsWith("’")) {
                            w = w.replace("’", "");

                        } else {
                            String[] wds = w.split("’");
                            w = wds[0];
                        }
                    }

                    return w;
                })
                .map(w -> {
                    if (w.contains("'")) {
                        if (w.startsWith("'") || w.endsWith("'")) {
                            w = w.replace("'", "");

                        } else {
                            String[] wds = w.split("'");
                            w = wds[0];
                        }
                    }

                    return w;
                })
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }
}
