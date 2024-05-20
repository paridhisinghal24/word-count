package com.word.count;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.word.count.service.TopNWordService;

@Component
public class WordCountRunner implements CommandLineRunner {

	private static final int TOP_N_WORDS = 10;
	
	@Autowired
	private TopNWordService topNWordService;
	
    public void run(String... args) throws MalformedURLException, IOException, InterruptedException, ExecutionException {
    	 topNWordService.topNWords(TOP_N_WORDS);
    }

}