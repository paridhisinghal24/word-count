package com.word.count.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.Callable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.google.common.util.concurrent.RateLimiter;

public class WordCountTask implements Callable<HashMap<String, Integer>> {
	private final String essayUrl;
	private final Set<String> validBankOfWords;

    private static RateLimiter rateLimiter = RateLimiter.create(1.0); // 1 request per second

	public WordCountTask(String essayUrl, Set<String> validBankOfWords) {
		this.essayUrl = essayUrl;
		this.validBankOfWords = validBankOfWords;
	}

	@Override
	public HashMap<String, Integer> call() throws InterruptedException {
		System.out.println("processEssay");
		String essayData = getDataFromUrl(essayUrl);
		HashMap<String, Integer> wordCountMap = new HashMap<>();
		String[] words = essayData.split("\\s+");
		for (String word : words) {
			if (validBankOfWords.contains(word)) {
				word = word.replaceAll("[^a-zA-Z]", "").toLowerCase(); 
				if (!word.isEmpty()) {
					wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
				}
			}
		}		
		return wordCountMap;
	}

	private String getDataFromUrl(String url) {
		rateLimiter.acquire();
		StringBuilder essayData = new StringBuilder();
		try {
			Document doc = Jsoup.connect(url).get();
			Elements articles = doc.select("article");
			for (Element article : articles) {
				essayData.append(article.text());
			}
			return essayData.toString();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return essayData.toString();
	}
}