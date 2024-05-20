package com.word.count.service;

import java.io.IOException;
import java.net.MalformedURLException;

import java.util.concurrent.ExecutionException;

public interface TopNWordService {
	public void topNWords(int top)
			throws MalformedURLException, IOException, InterruptedException, ExecutionException;
}
