package com.word.count.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.word.count.utils.GenericHttpRequest;
import com.word.count.utils.GetEssayConcurrent;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class TopNWordServiceImpl implements TopNWordService {
	@Value("${bank.of.words.url}")
	private String bankOfWordsUrl;
	
	@Value("${list.of.essay.url}")
	private String essayUrl;
	
	@Autowired
	private  GetEssayConcurrent getEssayMultithread;
	
	public Set<String> topNWords(int top) throws MalformedURLException, IOException, InterruptedException, ExecutionException{
		
		Set<String> validBankOfWords = getValidWords();
		
		List<String> listOfEssayUrl = GenericHttpRequest.sendGetRequest(essayUrl);
				
		HashMap<String, Integer> wordCountMap = getEssayMultithread.processEachEssay(listOfEssayUrl,validBankOfWords);
				
		List<Map.Entry<String, Integer>> result = getTopNWords(wordCountMap, top);
		System.out.println(prettyPrintJson(result));
		return null;
	}

	private static String prettyPrintJson(List<Map.Entry<String, Integer>> list) {
        List<Object[]> arrayEntries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : list) {
            arrayEntries.add(new Object[]{entry.getKey(), entry.getValue()});
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(arrayEntries);
    }

	private Set<String> getValidWords() throws IOException {
		List<String> bankOfWords = GenericHttpRequest.sendGetRequest(bankOfWordsUrl);
		Set<String> setBankOfWords = new HashSet<>(bankOfWords);
		Set<String> validBankOfWords = setBankOfWords.stream().filter(word -> isValidWord(word))
				.collect(Collectors.toSet());
		return validBankOfWords;
	}
	
	private static boolean isValidWord(String word) {
        return word.length() >= 3 && Pattern.matches("[a-zA-Z]+", word);
    }
	
	private List<Map.Entry<String, Integer>> getTopNWords(Map<String, Integer> wordCountMap, int top) {		
		 List<Map.Entry<String, Integer>> list = new ArrayList<>(wordCountMap.entrySet());
	     list.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
	     List<Map.Entry<String, Integer>> topN = list.subList(0, Math.min(top, list.size()));
		 return topN;
	}
}
