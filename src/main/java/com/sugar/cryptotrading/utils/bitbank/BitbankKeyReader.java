package com.sugar.cryptotrading.utils.bitbank;

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

public class BitbankKeyReader {
	public static BitbankKeyReader skr = new BitbankKeyReader();
	private final String[] keyFilePaths = {"/Users/kohei.sato/bitbank.key",
	                                      "/Users/koheisato/bitbank.key",
	                                      "/home/kohei.sato/work/bitbank.key"};
	private final String[] valFilePaths = {"/Users/kohei.sato/ordervalue.txt",
								           "/Users/koheisato/ordervalue.txt",
								           "/home/kohei.sato/work/ordervalue.txt"};
	private static HashMap<String, String> keyMap;
	private static List<BitbankOrderValues> valList;
	
	private BitbankKeyReader(){
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
		valList = new ArrayList<BitbankOrderValues>();
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
				String[] keyAndValue = line.split(",");
				String pair = keyAndValue[0];
				if(keyAndValue.length == 3) {
					boolean isNewPair= true;
					for(BitbankOrderValues val : valList) {
						if(val.getPair().equals(val.convertPair(pair))) {
							isNewPair = false;
							break;
						}
					}
					if(isNewPair) valList.add(new BitbankOrderValues(pair, keyAndValue[1], keyAndValue[2]));
				}
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

	
	public static BitbankKeyReader getReader() {
		return skr;
	}
	
	
//	public void showKeyValue() {
//		System.out.println("apikey:" + keyMap.get("apikey"));
//		System.out.println("secretkey:" + keyMap.get("secretkey"));
//	}
//	
	public static String getRestApiKey() {
		return keyMap.get("restapikey");
	}
	
	public String getApiKey() {
		return keyMap.get("apikey");
	}

	public String getSecretKey() {
		return keyMap.get("secretkey");
	}
	
	public static BitbankOrderValues getCoinValue(CurrencyPair pair) {
		final List<BitbankOrderValues> filtereList = valList.stream()
				.filter(value -> value.getPair().equals(pair))
				.collect(Collectors.toList());
		return filtereList.get(0);
	}
	
	public static List<BitbankOrderValues> getCoinValueList() {
		final List<BitbankOrderValues> filtereList = valList.stream()
														  .collect(Collectors.toList());
		return filtereList;
	}
}
