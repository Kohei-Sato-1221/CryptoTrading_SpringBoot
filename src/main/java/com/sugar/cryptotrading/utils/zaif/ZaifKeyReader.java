package com.sugar.cryptotrading.utils.zaif;

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

import com.sugar.cryptotrading.utils.bitbank.BitbankOrderValues;

import jp.nyatla.jzaif.types.CurrencyPair;


public class ZaifKeyReader {
	public static ZaifKeyReader skr = new ZaifKeyReader();
	private final String[] keyFilePaths = {"/Users/kohei.sato/zaif.key",
	                                      "/Users/koheisato/zaif.key",
	                                      "/home/kohei.sato/work/zaif.key"};
	private final String[] valFilePaths = {"/Users/kohei.sato/ordervalue.txt",
								           "/Users/koheisato/ordervalue.txt",
								           "/home/kohei.sato/work/ordervalue.txt"};
	private static HashMap<String, String> keyMap;
	private static List<ZaifOrderValues> valList;
	
	private ZaifKeyReader(){
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
				System.out.println("file not found....");
				continue;
			}
			if(br != null) break;
		}
		try {
			Stream<String> lines = br.lines();
			lines.forEach(line -> {
				String[] keyAndValue = line.split(",");
				if(keyAndValue.length == 2) keyMap.put(keyAndValue[0], keyAndValue[1]);
			});
			if(br != null) br.close();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void convertFromFileToValuelist() {
		valList = new ArrayList<ZaifOrderValues>();
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
					for(ZaifOrderValues val : valList) {
						if(val.getPair().equals(val.convertPair(pair))) {
							isNewPair = false;
							break;
						}
					}
					ZaifOrderValues temVal = new ZaifOrderValues(pair, keyAndValue[1], keyAndValue[2]);
					if(isNewPair && temVal.getPair() != null && temVal.getBaseAmountJPY() != null && temVal.getBaseAmountJPYLow() != null) {
						valList.add(temVal);
					}
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
	
	public static ZaifKeyReader getReader() {
		return skr;
	}
	
	
//	public void showKeyValue() {
//		System.out.println("apikey:" + keyMap.get("apikey"));
//		System.out.println("secretkey:" + keyMap.get("secretkey"));
//	}
//	
	public static ZaifOrderValues getCoinValue(CurrencyPair pair) {
		final List<ZaifOrderValues> filterList = valList.stream()
				.filter(value -> value.getPair().equals(pair))
				.collect(Collectors.toList());
		return filterList.get(0);
		
	}
	
	public static String getApiKey() {
		return keyMap.get("apikey");
	}

	public static String getSecretKey() {
		return keyMap.get("secretkey");
	}
	
}
