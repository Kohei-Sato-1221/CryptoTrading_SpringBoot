package com.sugar.cryptotrading.jobs.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sugar.cryptotrading.jobs.TradingJob;
import com.sugar.cryptotrading.restapi.RestClient;
import com.sugar.cryptotrading.utils.zaif.ZaifBuyer;
import com.sugar.cryptotrading.utils.zaif.ZaifKeyReader;
import com.sugar.cryptotrading.utils.zaif.ZaifOrderValues;
import jp.nyatla.jzaif.types.CurrencyPair;

public class ZaifJobImpl implements TradingJob {
	
	@Override
	public void placeBuyOrders() {
		System.out.println("Zaif/TSUMITATE ORDERS! " + new Date());
		
		List<ZaifBuyer> sbuyers = new ArrayList();
		final String APIKEY = ZaifKeyReader.getApiKey();
		final String SECKEY = ZaifKeyReader.getSecretKey(); 
		// memo Zaifでは restapikey, apikeyが一緒になっている　→　一旦、seckeyはRestclientに引き渡す。
		try {
			ZaifOrderValues xemValues = ZaifKeyReader.getCoinValue(CurrencyPair.XEMJPY);
			ZaifOrderValues ethValues = ZaifKeyReader.getCoinValue(CurrencyPair.ETHJPY);
			if(xemValues != null) {
				sbuyers.add(new ZaifBuyer(CurrencyPair.XEMJPY, new BigDecimal(10), xemValues.getBaseAmountJPY(), xemValues.getBaseAmountJPYLow(), 3, 1, APIKEY, SECKEY));
			}else {
				sbuyers.add(new ZaifBuyer(CurrencyPair.XEMJPY, new BigDecimal(10), new BigDecimal(150), new BigDecimal(250), 3, 1, APIKEY, SECKEY));
			}
			if(ethValues != null) {
				sbuyers.add(new ZaifBuyer(CurrencyPair.ETHJPY, new BigDecimal(10), ethValues.getBaseAmountJPY(), ethValues.getBaseAmountJPYLow(), -2, 4, APIKEY, SECKEY));
			}else {
				sbuyers.add(new ZaifBuyer(CurrencyPair.ETHJPY, new BigDecimal(10), new BigDecimal(150), new BigDecimal(250), -2, 4, APIKEY, SECKEY));
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
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
				sb.append(temp_buyer.sendBuyOrderLower());
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
}
