package com.sugar.cryptotrading.jobs.impl;

import java.io.IOException;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.sugar.cryptotrading.jobs.TradingJob;
import com.sugar.cryptotrading.restapi.RestClient;
import com.sugar.cryptotrading.utils.bitbank.BitbankBuyer;
import com.sugar.cryptotrading.utils.bitbank.BitbankKeyReader;
import com.sugar.cryptotrading.utils.bitbank.BitbankOrderValues;

import cc.bitbank.Bitbankcc;
import cc.bitbank.entity.Trades;
import cc.bitbank.entity.Ticker;
import cc.bitbank.entity.Trade;
import cc.bitbank.entity.enums.CurrencyPair;
import cc.bitbank.exception.BitbankException;

public class BitbankJobImpl implements TradingJob {
	
	@Override
	public void placeBuyOrders(int strategy) {
		System.out.println("Bitbank/TSUMITATE ORDERS! " + new Date());
		
		String NEWLINE = System.lineSeparator();
		Bitbankcc bb = new Bitbankcc();
		List<BitbankBuyer> buyers = new ArrayList<BitbankBuyer>();
		bb.setKey(BitbankKeyReader.getReader().getApiKey(), BitbankKeyReader.getReader().getSecretKey());
		
		RestClient.setRestApiKey(BitbankKeyReader.getRestApiKey());
		StringBuilder sb = new StringBuilder();
		sb.append("Tsumitate Orders:");
		sb.append(NEWLINE);
		try{
			List<BitbankOrderValues> orders = BitbankKeyReader.getCoinValueList();
			orders.forEach(order -> {
				//BigDecimal minimumBuyAmount, int roundPrice, int roundAmt
				BigDecimal minimumBuyAmount = null;
				int roundPrice = 0;
				int roundAmt = 0;
				CurrencyPair pair = order.getPair();
				try {
					Ticker ticker = bb.getTicker(pair);
					BigDecimal lastPrice = ticker.last;
					BigDecimal lowPrice = ticker.low;
					if(pair.equals(CurrencyPair.BTC_JPY)) {
						minimumBuyAmount = new BigDecimal("0.0002");
						roundPrice = 2;
						roundAmt = 4;
					}else if(pair.equals(CurrencyPair.XRP_JPY)) {
						minimumBuyAmount = new BigDecimal("1");
						roundPrice = 2;
						roundAmt = 2;
					}else if(pair.equals(CurrencyPair.MONA_JPY)) {
						minimumBuyAmount = new BigDecimal("0.3");
						roundPrice = 2;
						roundAmt = 2;
					}else if(pair.equals(CurrencyPair.BCC_JPY)){	
						minimumBuyAmount = new BigDecimal("0.0005");
						roundPrice = 2;
						roundAmt = 4;
					}
					
					if(minimumBuyAmount != null) {
						BigDecimal buyOrderJPYAmount = order.getBaseAmountJPY();
						BigDecimal buyOrderJPYAmountLow = order.getBaseAmountJPYLow();
						BitbankBuyer buyer;
						if(strategy == 0 && buyOrderJPYAmount.compareTo(BigDecimal.ZERO) > 0) {
							buyer = new BitbankBuyer(bb, pair, order.getBaseAmountJPY(), minimumBuyAmount, roundPrice, roundAmt);
							buyer.calculateBuyPriceNormal(lastPrice, lowPrice);
							buyer.calculateBuyAmount();
							buyers.add(buyer);
						}else if(strategy == 1 && buyOrderJPYAmountLow.compareTo(BigDecimal.ZERO) > 0) {
							buyer = new BitbankBuyer(bb, pair, order.getBaseAmountJPYLow(), minimumBuyAmount, roundPrice, roundAmt);
							buyer.calculateBuyPriceLower(lastPrice, lowPrice);					
							buyer.calculateBuyAmount();
							buyers.add(buyer);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			for(BitbankBuyer sbuyer : buyers) {
				sb.append(sbuyer.sendBuyOrder());
				sb.append(NEWLINE);
			}
			System.out.println("Orders in bitbank / " + sb.toString());
		} catch (BitbankException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
		
//	private static boolean hasTradePair(String[] args, String pair) {
//		if(args == null || args.length == 0) {
//			return true;
//		}
//		for(String tempPair : args) {
//			if(tempPair.equalsIgnoreCase(pair)) {
//				return true;
//			}
//		}
//		return false;
//	}

	@Override
	public void getHistory(String sinceStr) {
		Bitbankcc bb = new Bitbankcc();
		bb.setKey(BitbankKeyReader.getReader().getApiKey(), BitbankKeyReader.getReader().getSecretKey());
		
		DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date sinceDate = null;
		try {
			sinceDate = dateTimeFormat.parse(sinceStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long since = sinceDate.getTime();
		try {
			Trades result = bb.getHistory(CurrencyPair.BTC_JPY, since);
			Trade[] results = result != null ? result.trades : null;
			Arrays.sort(results);
			for(Trade trade : results) {
				outputHistory(trade);
			}
			results = bb.getHistory(CurrencyPair.MONA_JPY, since).trades;
			Arrays.sort(results);
			for(Trade trade : results) {
				outputHistory(trade);
			}
			results = bb.getHistory(CurrencyPair.BCC_JPY, since).trades;
			Arrays.sort(results);
			for(Trade trade : results) {
				outputHistory(trade);
			}
			results = bb.getHistory(CurrencyPair.XRP_JPY, since).trades;
			Arrays.sort(results);
			for(Trade trade : results) {
				outputHistory(trade);
			}
		} catch (BitbankException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void outputHistory(Trade trade) {
		System.out.println(trade.pair + " " + trade.side + " " + trade.price + " " + trade.amount + " " + trade.executedAt.toString().replace(" ", "/"));
	}



}
