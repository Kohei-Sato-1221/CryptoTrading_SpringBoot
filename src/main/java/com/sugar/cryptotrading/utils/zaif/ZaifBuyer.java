package com.sugar.cryptotrading.utils.zaif;

import java.io.IOException;


import java.math.BigDecimal;

import jp.nyatla.jzaif.api.ApiKey;
import jp.nyatla.jzaif.api.ExchangeApi;
import jp.nyatla.jzaif.api.PublicApi;
import jp.nyatla.jzaif.api.result.TradeResult;
import jp.nyatla.jzaif.types.CurrencyPair;
import jp.nyatla.jzaif.types.TradeType;

public class ZaifBuyer {
	private CurrencyPair pair;
	private BigDecimal baseAmountJPY;
	private BigDecimal baseAmountJPYLow; 
	private BigDecimal minimumBuyAmount;
	private int roundPrice = 0;
	private int roundAmt = 0;
	PublicApi lp;
	private ApiKey apiKey;
	
	public ZaifBuyer(CurrencyPair pair, BigDecimal minimumBuyAmount, BigDecimal baseAmountJPY, BigDecimal baseAmountJPYLow,int roundPrice, int roundAmt, String keyStr, String secKeyStr) throws IOException {
		this.pair = pair;
		this.lp = new PublicApi(pair);
		this.baseAmountJPY = baseAmountJPY;
		this.baseAmountJPYLow = baseAmountJPYLow;
		this.minimumBuyAmount = minimumBuyAmount;
		this.roundPrice = roundPrice;
		this.roundAmt = roundAmt;
		this.apiKey = new ApiKey(keyStr, secKeyStr);
	}
	
	
	public String sendBuyOrder() throws IOException, NullPointerException{
		BigDecimal buyPrice = calculateBuyPriceNormal();
		BigDecimal buyVol = baseAmountJPY.divide(buyPrice, roundAmt, BigDecimal.ROUND_HALF_UP);			
		System.out.println(buyPrice + " " + buyVol);
		ExchangeApi lp = new ExchangeApi(apiKey);
		TradeResult r = lp.trade(pair,TradeType.BID, buyPrice.doubleValue(), buyVol.doubleValue());
		if(r == null) {
			System.out.println("Order result is Null! ");			
		}else {
			System.out.println("order success? - " + r.success + " error type:" + r.error_type + " error text:" + r.error_text);			
			
		}
		System.out.println("# buyorder:" + pair + " price:" + buyPrice.toString() + " vol:" + buyVol.toString());
		return pair + " price:" + buyPrice.toString() + " vol:" + buyVol.toString();
	}

	public String sendBuyOrderLower() throws IOException, NullPointerException{
		BigDecimal buyPricelow = calculateBuyPriceLower();
		BigDecimal buyVol = baseAmountJPYLow.divide(buyPricelow, roundAmt, BigDecimal.ROUND_HALF_UP);	
		System.out.println(buyPricelow + " " + buyVol);
		ExchangeApi lp= new ExchangeApi(apiKey);
		TradeResult r= lp.trade(pair,TradeType.BID, buyPricelow.doubleValue(), buyVol.doubleValue());
		if(r == null) {
			System.out.println("Order result is Null! ");			
		}else {
			System.out.println("order success? - " + r.success + " error type:" + r.error_type + " error text:" + r.error_text);			
			
		}
		System.out.println("# buyorderLow:" + pair + " price:" + buyPricelow + " vol:" + buyVol);
		return pair + " price:" + buyPricelow + " vol:" + buyVol;
	}
	
	
	public BigDecimal calculateBuyAmount(BigDecimal buyPrice, BigDecimal baseAmountJPY) {
		BigDecimal retValue = baseAmountJPY.divide(buyPrice, roundAmt, BigDecimal.ROUND_HALF_UP);
		System.out.println("$ retValue:" + retValue + " buyPrice:" + buyPrice + " baseAmountJPY:" + baseAmountJPY);
		if(retValue.compareTo(minimumBuyAmount) < 0) {
			System.out.println("set minimuBuyAmount:" + minimumBuyAmount);
			retValue = minimumBuyAmount;
		}
		return retValue;
	}
	
	public BigDecimal calculateBuyPriceNormal() {
		return calculateBuyPrice("0.6","0.4");
	}
	
	public BigDecimal calculateBuyPriceLower() {
		return calculateBuyPrice("0.2","0.8");
	}
	
	private BigDecimal calculateBuyPrice(String percent1, String percent2) {
		BigDecimal lastPrice = new BigDecimal(lp.lastPrice());
		BigDecimal lowPrice = new BigDecimal(lp.ticker().low);
		System.out.println("lastPrice:" + lastPrice.toString() + "/ lowPrice:" + lowPrice.toString());
		lastPrice = lastPrice.multiply(new BigDecimal(percent1));
	    lowPrice = lowPrice.multiply(new BigDecimal(percent2));
	    System.out.println("#lastPrice:" + lastPrice.toString() + "/ #lowPrice:" + lowPrice.toString());
	    BigDecimal retValue = lastPrice.add(lowPrice);
	    retValue = retValue.setScale(roundPrice, BigDecimal.ROUND_HALF_UP);
	    System.out.println("calculateBuyPrice: " + retValue.toString());
	    return retValue;
	}
	
	public String getCurrentPair() throws IOException {
		return this.pair.toString(); 
	}
	
	public String getCurrentPrice() throws IOException {
		return "LastPrice(XEM):" + this.lp.ticker().last; 
	}
	
}
