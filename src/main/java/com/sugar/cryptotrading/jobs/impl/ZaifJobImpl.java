package com.sugar.cryptotrading.jobs.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.sugar.cryptotrading.jobs.TradingJob;
import com.sugar.cryptotrading.restapi.RestClient;
import com.sugar.cryptotrading.utils.zaif.ZaifBuyer;
import com.sugar.cryptotrading.utils.zaif.ZaifKeyReader;
import com.sugar.cryptotrading.utils.zaif.ZaifOrderValues;

import jp.nyatla.jzaif.api.ApiKey;
import jp.nyatla.jzaif.api.ExchangeApi;
import jp.nyatla.jzaif.api.result.TradeHistoryResult;
import jp.nyatla.jzaif.api.result.TradeHistoryResult.Item;
import jp.nyatla.jzaif.types.CurrencyPair;

public class ZaifJobImpl implements TradingJob {
	
	@Override
	public void placeBuyOrders(int strategy) {
		System.out.println("Zaif/TSUMITATE ORDERS! " + new Date());
		
		List<ZaifBuyer> sbuyers = new ArrayList();
		final String APIKEY = ZaifKeyReader.getApiKey();
		final String SECKEY = ZaifKeyReader.getSecretKey(); 
		// memo Zaifでは restapikey, apikeyが一緒になっている　→　一旦、seckeyはRestclientに引き渡す。
		try {
			ZaifOrderValues xemValues = ZaifKeyReader.getCoinValue(CurrencyPair.XEMJPY);
			ZaifOrderValues ethValues = ZaifKeyReader.getCoinValue(CurrencyPair.ETHJPY);

			ZaifBuyer xemBuyer;
			if(xemValues != null) {
				if(strategy == 0) {
					xemBuyer = new ZaifBuyer(CurrencyPair.XEMJPY, new BigDecimal(10), xemValues.getBaseAmountJPY(), xemValues.getBaseAmountJPY(), 3, 1, APIKEY, SECKEY);
					xemBuyer.setBuyPriceNormal();
				}else {
					xemBuyer = new ZaifBuyer(CurrencyPair.XEMJPY, new BigDecimal(10), xemValues.getBaseAmountJPY(), xemValues.getBaseAmountJPYLow(), 3, 1, APIKEY, SECKEY);
					xemBuyer.setBuyPriceLower();
				}
				Thread.sleep(1000);
			}else {
				if(strategy == 0) {
					xemBuyer = new ZaifBuyer(CurrencyPair.XEMJPY, new BigDecimal(10), new BigDecimal(150), new BigDecimal(200), 3, 1, APIKEY, SECKEY);					
					xemBuyer.setBuyPriceNormal();
				}else {
					xemBuyer = new ZaifBuyer(CurrencyPair.XEMJPY, new BigDecimal(10), new BigDecimal(150), new BigDecimal(300), 3, 1, APIKEY, SECKEY);					
					xemBuyer.setBuyPriceLower();
				}
				Thread.sleep(1000);
			}
			sbuyers.add(xemBuyer);
			
			ZaifBuyer ethBuyer;
			
			if(ethValues != null) {
				if(strategy == 0) {
					ethBuyer = new ZaifBuyer(CurrencyPair.ETHJPY, new BigDecimal(10), ethValues.getBaseAmountJPY(), ethValues.getBaseAmountJPY(), -2, 4, APIKEY, SECKEY);
					ethBuyer.setBuyPriceNormal();
				}else {
					ethBuyer = new ZaifBuyer(CurrencyPair.ETHJPY, new BigDecimal(10), ethValues.getBaseAmountJPY(), ethValues.getBaseAmountJPYLow(), -2, 4, APIKEY, SECKEY);					
					ethBuyer.setBuyPriceLower();
				}
				Thread.sleep(1000);
			}else {
				if(strategy == 0) {
					ethBuyer = new ZaifBuyer(CurrencyPair.ETHJPY, new BigDecimal(10), new BigDecimal(150), new BigDecimal(200), -2, 4, APIKEY, SECKEY);					
					ethBuyer.setBuyPriceNormal();
				}else {
					ethBuyer = new ZaifBuyer(CurrencyPair.ETHJPY, new BigDecimal(10), new BigDecimal(150), new BigDecimal(300), -2, 4, APIKEY, SECKEY);					
					ethBuyer.setBuyPriceLower();
				}
				Thread.sleep(2000);
			}
			
			sbuyers.add(ethBuyer);
			
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		String NEWLINE = System.lineSeparator();
		
		RestClient.setApiKey(SECKEY);
		StringBuilder sb = new StringBuilder();
		sb.append("Tsumitate Orders:");
		sb.append(NEWLINE);
		for(ZaifBuyer temp_buyer : sbuyers) {
			try{
				sb.append("【" + temp_buyer.getCurrentPair() + "】");
				sb.append(NEWLINE);
				sb.append(temp_buyer.sendBuyOrder());
				sb.append(NEWLINE);
				sb.append(temp_buyer.getCurrentPrice());
				sb.append(NEWLINE);
				sb.append(NEWLINE);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Orders in Zaif / " +  sb.toString());
		System.out.println("#### END ####");
	}

	@Override
	public void getHistory(String sinceStr) {
		final String APIKEY = ZaifKeyReader.getApiKey();
		final String SECKEY = ZaifKeyReader.getSecretKey();
		
		ApiKey apiKey = new ApiKey(APIKEY, SECKEY);
		ExchangeApi exchangeApi = new ExchangeApi(apiKey);
		
		DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date sinceDate = null;
		try {
			sinceDate = dateTimeFormat.parse(sinceStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		TradeHistoryResult result = exchangeApi.tradeHistory(null, null, null, null, null, sinceDate, null, CurrencyPair.ETHJPY);
		List<Item> historyList = result.history;
		historyList = historyList.stream()
				.sorted(Comparator.comparing(Item::getTimestamp))
				.collect(Collectors.toList());
		for(TradeHistoryResult.Item item : historyList) {
			outputHistory(item);
		}
		
		result = exchangeApi.tradeHistory(null, null, null, null, null, sinceDate, null, CurrencyPair.XEMJPY);
		historyList = result.history;
		historyList = historyList.stream()
				.sorted(Comparator.comparing(Item::getTimestamp))
				.collect(Collectors.toList());
		for(TradeHistoryResult.Item item : historyList) {
			outputHistory(item);
		}
	}
	
	private void outputHistory(TradeHistoryResult.Item item) {
		System.out.println(item.currency_pair + " " + item.action.toString() + " " + item.price + " " + item.amount + " " + item.timestamp.toString().replace(" ", "/"));
	}
}
