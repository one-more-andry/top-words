package com.lapsho.app;


import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.stream.Collectors;


public class TopWords
{
    private final static String WORD_REGEX = "([']?[A-Za-z]+[']?[A-Za-z]+[']?)|([']?[A-Za-z]+[']?)";

    private final static Pattern WORD_PATTERN = Pattern.compile(WORD_REGEX);

    public static List<String> top3(String s) {
        List<String> topWords = new ArrayList<>();

        if ( s == null || s.equals("") ) {
            return topWords;
        }
        Map<String, Integer> prioritizedWords = new HashMap<>();
        Matcher wordMatcher = WORD_PATTERN.matcher(s);

        while (wordMatcher.find()) {
            String word = wordMatcher.group().toLowerCase(Locale.ROOT);
            prioritizedWords.put(word, prioritizedWords.getOrDefault(word, 0) + 1);
        }

        for (int i = Math.min(prioritizedWords.size(), 3); i > 0; i--) {
            int maxMatchers = Collections.max(prioritizedWords.values());
            List<String> samePriorityWords = prioritizedWords.entrySet().stream()
                    .filter(entry -> entry.getValue() == maxMatchers)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            if (samePriorityWords.size() == 1) {
                topWords.addAll(samePriorityWords);
                prioritizedWords.keySet().removeAll(samePriorityWords);

                continue;
            }

            if (samePriorityWords.size() < i) {
                topWords.addAll(samePriorityWords);
                prioritizedWords.keySet().removeAll(samePriorityWords);
                i -= samePriorityWords.size();
                i++;

                continue;
            }

            if (samePriorityWords.size() > i) {
                topWords.addAll(samePriorityWords.subList(0, i - 1));

            } else {
                topWords.addAll(samePriorityWords);
            }
            i = 0;
        }

        return topWords;
    }
}
