package com.word.count.utils;

import java.util.*;
import java.util.concurrent.*;

import org.springframework.stereotype.Component;

@Component
public class GetEssayConcurrent {

	// Executor service to manage threads
	private static ExecutorService executor = Executors.newFixedThreadPool(2);
	    
	public HashMap<String, Integer> processEachEssay(List<String> listOfEssayUrl, Set<String> validBankOfWords)
			throws InterruptedException, ExecutionException {

		CompletionService<HashMap<String, Integer>> completionService = new ExecutorCompletionService<>(executor);
		// Submit tasks to process each essay concurrently	    
		for (int i = 0 ; i < listOfEssayUrl.size() ; i++) {
			completionService.submit(new WordCountTask(listOfEssayUrl.get(i), validBankOfWords));			
		}

		// Aggregate results as they become available
		HashMap<String, Integer> combinedWordCountMap = new HashMap<>();
		for (int i = 0; i < listOfEssayUrl.size(); i++) {
			Future<HashMap<String, Integer>> future = completionService.take();
			HashMap<String, Integer> wordCountMap = future.get();
			for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
				combinedWordCountMap.put(entry.getKey(),
						combinedWordCountMap.getOrDefault(entry.getKey(), 0) + entry.getValue());
			}
		}
		executor.shutdown();
		return combinedWordCountMap;
	}
}
