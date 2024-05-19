package com.word.count.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class GenericHttpRequest {

	// Method to make a GET request
	public static List<String> sendGetRequest(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// Set request method to GET
		connection.setRequestMethod("GET");

		connection.setConnectTimeout(5000);
		connection.setReadTimeout(5000);

		// Get response code
		int responseCode = connection.getResponseCode();
		connection.setInstanceFollowRedirects(false); // This is the default behavior, you can skip this line

		// If response code is not 200, throw an exception
		if (responseCode != 200) {
			throw new RuntimeException("HTTP GET Request Failed with Error code: " + responseCode);
		}

		// Read response
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		List<String> response = new ArrayList<>();
		String line;

		while ((line = reader.readLine()) != null) {
			response.add(line);
		}
		reader.close();
		connection.disconnect();
		return response;
	}

}