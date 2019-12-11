package com.sugar.cryptotrading.jobs.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sugar.cryptotrading.jobs.TradingJob;
import com.sugar.cryptotrading.restapi.RestClient;
import com.sugar.cryptotrading.utils.SugarBuyer;
import com.sugar.cryptotrading.utils.SugarKeyReader;
import com.sugar.cryptotrading.utils.SugarOrderValues;

import cc.bitbank.Bitbankcc;
import cc.bitbank.entity.enums.CurrencyPair;
import cc.bitbank.exception.BitbankException;

public class BitbankJobImpl implements TradingJob {
	private String[] tradePairs;
	
	public BitbankJobImpl(String[] tradePairs){
		this.tradePairs = tradePairs;
	}
	
	@Override
	public void placeBuyOrders() {
		System.out.println("TSUMITATE ORDERS! " + new Date());
		
		String NEWLINE = System.lineSeparator();
		Bitbankcc bb = new Bitbankcc();
		List<SugarBuyer> buyers = new ArrayList<SugarBuyer>();
		bb.setKey(SugarKeyReader.getReader().getApiKey(), SugarKeyReader.getReader().getSecretKey());
		
		RestClient.setRestApiKey(SugarKeyReader.getRestApiKey());
		StringBuilder sb = new StringBuilder();
		sb.append("Tsumitate Orders:");
		sb.append(NEWLINE);
		try{
			SugarOrderValues btcValues = SugarKeyReader.getCoinValue(CurrencyPair.BTC_JPY);
			btcValues = btcValues != null ? btcValues : new SugarOrderValues("btc_jpy", "150", "250");
			SugarOrderValues xrpValues = SugarKeyReader.getCoinValue(CurrencyPair.XRP_JPY);
			xrpValues = xrpValues != null ? xrpValues : new SugarOrderValues("xrp_jpy", "50", "75");
			SugarOrderValues monaValues = SugarKeyReader.getCoinValue(CurrencyPair.MONA_JPY);
			monaValues = monaValues != null ? monaValues : new SugarOrderValues("monac_jpy", "50", "75");
			SugarOrderValues bccValues = SugarKeyReader.getCoinValue(CurrencyPair.BCC_JPY);
			bccValues = bccValues != null ? bccValues : new SugarOrderValues("bcc_jpy", "50", "75");
//			SugarOrderValues ethValues = SugarKeyReader.getCoinValue(CurrencyPair.ETH_BTC);
//			ethValues = ethValues != null ? ethValues : new SugarOrderValues("eth_btc", "200", "200");
			if(hasTradePair(this.tradePairs, "btc")) buyers.add(new SugarBuyer(bb, SugarKeyReader.getCoinValue(CurrencyPair.BTC_JPY), new BigDecimal("0.0002"), 2, 4));
			if(hasTradePair(this.tradePairs, "xrp")) buyers.add(new SugarBuyer(bb, SugarKeyReader.getCoinValue(CurrencyPair.XRP_JPY), new BigDecimal("1"), 2, 2));
			if(hasTradePair(this.tradePairs, "mona")) buyers.add(new SugarBuyer(bb, SugarKeyReader.getCoinValue(CurrencyPair.MONA_JPY), new BigDecimal("0.3"), 2, 2));
			if(hasTradePair(this.tradePairs, "bcc")) buyers.add(new SugarBuyer(bb, SugarKeyReader.getCoinValue(CurrencyPair.BCC_JPY), new BigDecimal("0.0005"), 2, 4));
//			if(hasTradePair(args, "eth")) buyers.add(new SugarBuyer(bb, SugarKeyReader.getCoinValue(CurrencyPair.ETH_BTC), new BigDecimal("0.0001"), 8, 8));
			
			for(SugarBuyer sbuyer : buyers) {
				sb.append(sbuyer.sendBuyOrder());
				sb.append(NEWLINE);
				Thread.sleep(500);
				sb.append(sbuyer.sendBuyOrderLower());
				Thread.sleep(500);
				sb.append(NEWLINE);
				sb.append(NEWLINE);
			}
			RestClient.get("Orders in bitbank", sb.toString());
		} catch (BitbankException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	private static boolean hasTradePair(String[] args, String pair) {
		if(args == null || args.length == 0) {
			return true;
		}
		for(String tempPair : args) {
			if(tempPair.equalsIgnoreCase(pair)) {
				return true;
			}
		}
		return false;
	}

}
