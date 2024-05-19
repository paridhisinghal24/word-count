package com.word.count.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
public class FixedWindowRateLimiter {
	long windowSize;
    // The number of allowed requests.
    int maxRequestCount;
    // The number of requests that pass through the current window.
    AtomicInteger counter = new AtomicInteger(0);
    // The right boundary of the window.
    long windowBorder;
    public FixedWindowRateLimiter(long windowSize, int maxRequestCount) {
        this.windowSize = windowSize;
        this.maxRequestCount = maxRequestCount;
        this.windowBorder = System.currentTimeMillis() + windowSize;
    }
    public synchronized boolean tryAcquire() {
        long currentTime = System.currentTimeMillis();
        if (windowBorder < currentTime) {
            //logger.info("window reset");
            do {
                windowBorder += windowSize;
            } while (windowBorder < currentTime);
            counter = new AtomicInteger(0);
        }

        if (counter.intValue() < maxRequestCount) {
            counter.incrementAndGet();
            //logger.info("tryAcquire success");
            return true;
        } else {
            //logger.info("tryAcquire fail");
            return false;
        }
    }
}
