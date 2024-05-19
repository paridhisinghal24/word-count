package com.word.count.utils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

//public class RateLimiter {
//	private final long capacity; // Maximum number of tokens
//    private final long refillRate; // Tokens refill rate per second
//    private AtomicLong tokens; // Current number of tokens available
//    private long lastRefillTime; // Last time tokens were refilled
//
//    public RateLimiter(long capacity, long refillRate) {
//        this.capacity = capacity;
//        this.refillRate = refillRate;
//        this.tokens = new AtomicLong(capacity);
//        this.lastRefillTime = System.nanoTime();
//    }
//
//    public synchronized boolean allowRequest() {
//        refillTokens();
//        return tokens.getAndDecrement() > 0;
//    }
//
//    private void refillTokens() {
//        long now = System.nanoTime();
//        long elapsedTime = now - lastRefillTime;
//        long tokensToAdd = TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS) * refillRate;
//        long newTokens = Math.min(capacity, tokens.get() + tokensToAdd);
//        tokens.set(newTokens);
//        lastRefillTime = now;
//    }

//}
