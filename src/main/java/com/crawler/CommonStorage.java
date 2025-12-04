package com.crawler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.BlockingQueue;

/*
CommonStorage class to hold shared data structures for the threads.
*/

public class CommonStorage {
    // Thread-safe set to keep track of visited URLs.
    ConcurrentHashMap<String, Boolean> visitedUrls = new ConcurrentHashMap<>();
    // Thread-safe queue to hold URLs to be processed.
    BlockingQueue<String> urlQueue = new LinkedBlockingDeque<>();

    // add a unique url to the storage.
    public Boolean addUrl (String url) {
        if (visitedUrls.putIfAbsent(url, true) == null) {
            urlQueue.offer(url);
            return true;
        }   
        return false;
    }

    // returns if the queue is empty.
    public boolean isEmpty() {
        return urlQueue.isEmpty();
    }  
    
    // gets next url from the queue to be processed.
    public String getNextUrl() {
        try {
            // waits for 100 milliseconds if the queue is empty.
            return urlQueue.poll(100, java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            return null;
        }
    }
}
