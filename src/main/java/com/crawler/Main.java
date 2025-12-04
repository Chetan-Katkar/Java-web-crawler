package com.crawler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        CommonStorage storage = new CommonStorage();
        URLFetcher fetcher = new URLFetcher();
        
        // Seed the Crawler 
        System.out.println("Starting Crawl on Wikipedia...");
        storage.addUrl("https://en.wikipedia.org/wiki/Java_(programming_language)");
        
        
        //  crawlers running in parallel
        ExecutorService executor = Executors.newFixedThreadPool(4);
        
        // Assign Tasks to the crawlers
        for (int i = 0; i < 4; i++) {
            // We create a new Crawler (worker) and give it the tools
            executor.execute(new Crawler(storage, fetcher, 50));
        }

        executor.shutdown();
        
        try {
            if (!executor.awaitTermination(15, TimeUnit.SECONDS)) {
                executor.shutdownNow(); 
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
        
        System.out.println("\n--- Crawling Finished ---");
        
        InvertedIndex.printUrls("platform");
        InvertedIndex.printUrls("compiler");
        InvertedIndex.printUrls("object");
    }
}