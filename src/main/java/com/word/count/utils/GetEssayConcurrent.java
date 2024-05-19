package com.word.count.utils;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;

import org.springframework.stereotype.Component;

@Component
public class GetEssayConcurrent {

	// Executor service to manage threads
	private static ExecutorService executor = Executors.newFixedThreadPool(4);
	//private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);
	
	int requestsPerSecond = 100;
    //int numberOfThreads = 10;

    
	public HashMap<String, Integer> processEachEssay(List<String> listOfEssayUrl, Set<String> validBankOfWords)
			throws InterruptedException, ExecutionException {

		CompletionService<HashMap<String, Integer>> completionService = new ExecutorCompletionService<>(executor);
	    //RateLimiter rateLimiter = new RateLimiter(100,100);
	   listOfEssayUrl = listOfEssayUrl.subList(0, 5000);
		// Submit tasks to process each essay concurrently
	    System.out.println("Start time");
	    System.out.println(Instant.now());
		for (int i = 0 ; i < listOfEssayUrl.size() ; i++) {
			//System.out.println(i );
			completionService.submit(new WordCountTask(listOfEssayUrl.get(i), validBankOfWords,i));
			//executor.schedule(new WordCountTask(listOfEssayUrl.get(i), validBankOfWords,i), 1, TimeUnit.SECONDS);

		}

		//		for (String essayUrl : listOfEssayUrl) {
//			if(rateLimiter.allowRequest()) { // Acquire permit before making the request
//				completionService.submit(new WordCountTask(essayUrl, validBankOfWords));
//			} else {
//				Thread.sleep(10000);
//			}
//		}
		// Aggregate results as they become available
		HashMap<String, Integer> combinedWordCountMap = new HashMap<>();
		for (int i = 0; i < listOfEssayUrl.size(); i++) {
			Future<HashMap<String, Integer>> future = completionService.take();
			System.out.println("combinedWordCountMap " + combinedWordCountMap.size());
			HashMap<String, Integer> wordCountMap = future.get();
			for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
				combinedWordCountMap.put(entry.getKey(),
						combinedWordCountMap.getOrDefault(entry.getKey(), 0) + entry.getValue());
			}
		}
		executor.shutdown();
		return combinedWordCountMap;
	}

//
//	private void makeRequest() {
//		// TODO Auto-generated method stub
//		
//	}
}
