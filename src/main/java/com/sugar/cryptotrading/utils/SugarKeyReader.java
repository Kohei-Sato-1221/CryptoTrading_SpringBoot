package com.sugar.cryptotrading.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cc.bitbank.entity.enums.CurrencyPair;

public class SugarKeyReader {
	public static SugarKeyReader skr = new SugarKeyReader();
	private final String[] keyFilePaths = {"/Users/kohei.sato/bitbank.key",
	                                      "/Users/koheisato/bitbank.key",
	                                      "/home/kohei.sato/work/bitbank.key"};
	private final String[] valFilePaths = {"/Users/kohei.sato/ordervalue.txt",
								           "/Users/koheisato/ordervalue.txt",
								           "/home/kohei.sato/work/ordervalue.txt"};
	private static HashMap<String, String> keyMap;
	private static List<SugarOrderValues> valList;
	
	private SugarKeyReader(){
		convertFromFileToHashMap();
		convertFromFileToValuelist();
	}
	
	private void convertFromFileToHashMap() {
		keyMap = new HashMap<>();
		BufferedReader br = null;
		for(String path: keyFilePaths) {
			try {
				br = new BufferedReader(new FileReader(new File(path)));
			} catch (FileNotFoundException e) {
				continue;
			}			
		}
		try {
			Stream<String> lines = br.lines();
			lines.forEach(line -> {
				String[] keyAndValue = line.split(",");
				if(keyAndValue.length == 2) keyMap.put(keyAndValue[0], keyAndValue[1]);
			});
			br.close();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void convertFromFileToValuelist() {
		valList = new ArrayList<SugarOrderValues>();
		BufferedReader br = null;
		for(String path: valFilePaths) {
			try {
				br = new BufferedReader(new FileReader(new File(path)));
			} catch (FileNotFoundException e) {
				continue;
			}			
		}
		if(br == null) System.out.println("br is null");
		try {
			Stream<String> lines = br.lines();
			lines.forEach(line -> {
				System.out.println("line:" + line);
				String[] keyAndValue = line.split(",");
				if(keyAndValue.length == 3) valList.add(new SugarOrderValues(keyAndValue[0], keyAndValue[1], keyAndValue[2]));
			});
			br.close();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public static SugarKeyReader getReader() {
		return skr;
	}
	
	
	public void showKeyValue() {
		System.out.println("apikey:" + keyMap.get("apikey"));
		System.out.println("secretkey:" + keyMap.get("secretkey"));
	}
	
	public static String getRestApiKey() {
		return keyMap.get("restapikey");
	}
	
	public String getApiKey() {
		return keyMap.get("apikey");
	}

	public String getSecretKey() {
		return keyMap.get("secretkey");
	}
	
	public static SugarOrderValues getCoinValue(CurrencyPair pair) {
		final List<SugarOrderValues> filtereList = valList.stream()
														  .filter(value -> value.getPair().equals(pair))
														  .collect(Collectors.toList());
		return filtereList.get(0);
	}
}
