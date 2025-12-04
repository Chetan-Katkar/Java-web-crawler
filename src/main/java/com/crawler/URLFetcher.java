package com.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.HashSet;
import java.util.Set;

public class URLFetcher {
    public Set<String> fetchUrls(String url) {
        Set<String> urls = new HashSet<>();
        try {
            Document doc = Jsoup.connect(url)
                                .userAgent("Mozilla/5.0")
                                .timeout(5000)
                                .get();
            
            // extracting text from the page.
            String text = doc.body().text();
            // adding url to the dictionary.
            InvertedIndex.addEntry(text, url);
            
            // extracting all links from the page.
            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String linkHref = link.attr("abs:href");
                if (!linkHref.isEmpty()) {
                    urls.add(linkHref);
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching URLs from " + url + ": " + e.getMessage());
        }
        return urls;
    }
}
