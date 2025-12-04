package com.crawler;

import java.util.Set;

// thread class to perform crawling tasks.
public class Crawler implements Runnable {
    
    // References to our shared tools
    private final CommonStorage storage;
    private final URLFetcher fetcher;
    private final int maxPages; // Safety limit to prevent infinite running

    // Constructor to connect the tools
    public Crawler(CommonStorage storage, URLFetcher fetcher, int maxPages) {
        this.storage = storage;
        this.fetcher = fetcher;
        this.maxPages = maxPages;
    }

    @Override
    public void run() {
        while (true) {
            //  Get the next URL 
            String url = storage.getNextUrl();
            
            // If queue returns null 
            if (url == null) break;

            if (!url.contains("en.wikipedia.org") || 
                 url.contains("Help:") || 
                 url.contains("File:") || 
                 url.contains("Talk:") || 
                 url.contains("Special:")) {
                continue;
            }

            System.out.println(Thread.currentThread().getName() + " processing: " + url);

            // This calls the file you created in the last step
            Set<String> newLinks = fetcher.fetchUrls(url);

            // 4. Process the new links
            for (String link : newLinks) {
                if (link.contains("/wiki/") && !link.contains("#")) {
                    storage.addUrl(link);
                }
            }
        }
    }
}