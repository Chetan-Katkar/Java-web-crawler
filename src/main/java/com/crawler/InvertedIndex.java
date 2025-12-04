package com.crawler;

import java.util.*;

public class InvertedIndex {

    // word → list of URLs containing that word
    private static final Map<String, Set<String>> index = new HashMap<>();

    public static synchronized void addEntry(String text, String url) {
        String[] words = text.toLowerCase().split("\\W+");

        for (String word : words) {
            if (word.length() < 3) continue; // skip tiny words

            index.computeIfAbsent(word, k -> new HashSet<>()).add(url);
        }
    }

    public static void printUrls(String word) {
        System.out.println("\nResults for: " + word);

        Set<String> urls = index.get(word.toLowerCase());

        if (urls == null || urls.isEmpty()) {
            System.out.println("  No URLs found.");
            return;
        }

        for (String url : urls) {
            System.out.println("  - " + url);
        }
    }
}
