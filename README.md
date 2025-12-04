---

# High-Performance Multithreaded Web Crawler & Search Engine

A high-throughput, domain-specific web crawler built in Java. This system utilizes **multithreading** and **concurrent data structures** to traverse the web graph efficiently, filtering for high-value content to build a searchable inverted index.

**Tech Stack:** Java , Jsoup, Java Concurrency , Maven.

## Project Overview

This is not a simple script; it is a scalable **Producer-Consumer** system.

- **The Goal:** To map out specific sections of the web (e.g., Computer Science documentation on Wikipedia) without getting trapped in infinite cycles or irrelevant external links.
- **The Engine:** It uses a pool of worker threads to fetch, parse, and index pages in parallel, significantly reducing the I/O bottleneck associated with single-threaded crawlers.

### Key Features

- **Parallel Execution:** Uses `ExecutorService` to manage a fixed thread pool, allowing multiple spiders to crawl simultaneously.
- **Thread Safety:** Implements **Lock-Free** synchronization using `ConcurrentHashMap` and `BlockingQueue` to prevent race conditions without heavy `synchronized` blocks.
- **Polite & Robust:** Includes domain filtering (staying within the target site) and cycle detection to prevent infinite loops.
- **In-Memory Search:** Builds an **Inverted Index** mapping keywords to URLs, allowing for O(1) retrieval of relevant pages.

---

## Core Concepts & Algorithms

### 1. Graph Traversal (BFS)

The web is treated as a **Directed Graph**. The crawler implements a **Breadth-First Search (BFS)** strategy:

- It explores neighbors (links on the current page) before going deeper.
- This ensures we discover the most relevant "hub" pages first rather than getting lost in a single deep path (DFS).

### 2. The Producer-Consumer Pattern

The architecture separates the state management from the execution:

- **Producer:** The `Crawler` threads find new links and add them to the queue.
- **Consumer:** The same `Crawler` threads pull URLs from the queue to process.
- **Shared Resource:** The `URLStorage` class acts as the monitor, handling the synchronization.

---

## Data Structures Used

| **Data Structure** | **Implementation** | **Purpose** |
| --- | --- | --- |
| **Frontier Queue** | `LinkedBlockingQueue<String>` | Thread-safe FIFO queue to manage URLs pending visitation. Handles the concurrency of multiple threads adding/removing items. |
| **Visited Set** | `ConcurrentHashMap<String, Boolean>` | O(1) lookup to check if a URL has already been processed (Cycle Detection). We use the KeySet of the map as a thread-safe Set. |
| **Local Buffer** | `HashSet<String>` | Used inside individual threads to filter duplicate links on a single page before attempting to add them to the global queue. |
| **Inverted Index** | `ConcurrentHashMap<String, List<String>>` | Maps keywords (e.g., "polymorphism") to a list of URLs containing that topic for the search engine feature. |

---

## Code Structure & File Guide

- **`Main.java`**
    - The entry point. It initializes the `URLStorage`, seeds the crawler with the starting URL (e.g., Wikipedia), spins up the `ExecutorService` (Thread Pool), and manages the graceful shutdown of the system.
- **`CommonStorage.java`**
    - **The Brain.** This class manages the global state. It encapsulates the `BlockingQueue` and `Visited` set. It contains the critical logic `putIfAbsent` to ensure no two threads ever crawl the same URL.
- **`Crawler.java`** (Implements `Runnable`)
    - **The Worker.** Represents a single spider unit. It runs in an infinite loop: popping a URL from storage, fetching it, filtering the data, and pushing new links back to storage.
- **`URLFetcher.java`**
    - **The Interface.** Handles the I/O operations. It wraps the **Jsoup** library to handle HTTP connections, HTML parsing, and DOM traversal. It isolates the "messy" network logic from the clean algorithmic logic.
- **`InvertedIndex.java`**
    - **The Search Engine.** Processes the raw text of visited pages, tokenizes content, and updates the search index to make the crawled data queryable.

---

## How to Run

1. **Prerequisites:** Java JDK 11+, Maven.
2. **Clone the Repo:**Bash
    
    `git clone https://github.com/YourUsername/Java-Multithreaded-Crawler.git`
    
3. **Build:**Bash

## Demo Output
*Actual output from a 15-second crawl starting at "Java (programming language)":*

text
Starting Crawl on Wikipedia...
pool-1-thread-1 processing: [https://en.wikipedia.org/wiki/Java_(programming_language](https://en.wikipedia.org/wiki/Java_(programming_language))
pool-1-thread-2 processing: [https://en.wikipedia.org/wiki/ALGOL](https://en.wikipedia.org/wiki/ALGOL)
pool-1-thread-3 processing: [https://en.wikipedia.org/wiki/Forth_(programming_language](https://en.wikipedia.org/wiki/Forth_(programming_language))
...
--- Crawling Finished ---

> Search Query: "compiler"
Found results:
 - [https://en.wikipedia.org/wiki/ALGOL](https://en.wikipedia.org/wiki/ALGOL)
 - [https://en.wikipedia.org/wiki/Forth_(programming_language](https://en.wikipedia.org/wiki/Forth_(programming_language))
 - [https://en.wikipedia.org/wiki/Java_(programming_language](https://en.wikipedia.org/wiki/Java_(programming_language))

> Search Query: "platform"
Found results:
 - [https://en.wikipedia.org/wiki/JDeveloper](https://en.wikipedia.org/wiki/JDeveloper)
 - [https://en.wikipedia.org/wiki/Java_(programming_language](https://en.wikipedia.org/wiki/Java_(programming_language))
    
    
