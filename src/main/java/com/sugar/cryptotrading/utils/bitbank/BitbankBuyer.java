package com.sugar.cryptotrading.utils.bitbank;

import java.io.IOException;
import java.math.BigDecimal;

import cc.bitbank.Bitbankcc;
import cc.bitbank.entity.Order;
import cc.bitbank.entity.Ticker;
import cc.bitbank.entity.enums.CurrencyPair;
import cc.bitbank.entity.enums.OrderSide;
import cc.bitbank.entity.enums.OrderType;
import cc.bitbank.exception.BitbankException;

public class BitbankBuyer {
	private CurrencyPair pair;
	private BigDecimal baseAmountJPY;
	private BigDecimal baseAmountJPYLow; 
	private BigDecimal minimumBuyAmount;
	private int roundPrice = 0;
	private int roundAmt = 0;
	Bitbankcc bb;
	Ticker ticker;
	
	public BitbankBuyer(Bitbankcc bb, BitbankOrderValues sov,BigDecimal minimumBuyAmount, int roundPrice, int roundAmt) throws BitbankException, IOException {
		this.bb = bb;
		this.pair = sov.getPair();
		this.ticker = bb.getTicker(pair);
		this.baseAmountJPY = sov.getBaseAmountJPY(bb);
		this.baseAmountJPYLow = sov.getBaseAmountJPYLow(bb);
		this.minimumBuyAmount = minimumBuyAmount;
		this.roundPrice = roundPrice;
		this.roundAmt = roundAmt;
	}
	
	/*
	public SugarBuyer(Bitbankcc bb, CurrencyPair pair, BigDecimal baseAmountJPY, BigDecimal baseAmountJPYLow,BigDecimal minimumBuyAmount, int roundPrice, int roundAmt) throws BitbankException, IOException {
		this.bb = bb;
		this.pair = pair;
		this.baseAmountJPY = baseAmountJPY;
		this.baseAmountJPYLow = baseAmountJPYLow;
		this.ticker = bb.getTicker(pair);
		this.minimumBuyAmount = minimumBuyAmount;
		this.roundPrice = roundPrice;
		this.roundAmt = roundAmt;
	}
	*/
	
	public String sendBuyOrder() throws BitbankException, IOException, NullPointerException{
		BigDecimal baseAmount = baseAmountJPY;
		if(isBTCbasePair()) {
			Ticker tickerForBTC = bb.getTicker(CurrencyPair.BTC_JPY);
			baseAmount = baseAmountJPY.divide(tickerForBTC.last, 8, BigDecimal.ROUND_HALF_UP);			
		}
		BigDecimal buyPrice = calculateBuyPriceNormal();
		BigDecimal buyAmount = calculateBuyAmount(buyPrice, baseAmount);
		System.out.println(buyPrice + " " + buyAmount);
		Order order = bb.sendOrder(pair, buyPrice, buyAmount, OrderSide.BUY, OrderType.LIMIT);
		if(order == null) {
			System.out.println("Order result is Null! ");			
		}else {
			System.out.println("" + order);			
			
		}
		return pair + " price:" + buyPrice + " vol:" + buyAmount;
	}
	
	public String sendBuyOrderLower() throws BitbankException, IOException {
		BigDecimal baseAmount = baseAmountJPYLow;
		if(isBTCbasePair()) {
			Ticker tickerForBTC = bb.getTicker(CurrencyPair.BTC_JPY);
			baseAmount = baseAmountJPYLow.divide(tickerForBTC.last, 8, BigDecimal.ROUND_HALF_UP);			
		}
		BigDecimal buyPricelow = calculateBuyPriceLower();
		BigDecimal buyAmountlow = calculateBuyAmount(buyPricelow, baseAmount);
		Order order2 = bb.sendOrder(pair, buyPricelow, buyAmountlow, OrderSide.BUY, OrderType.LIMIT);
		System.out.println("" + order2);
		return pair + " price:" + buyPricelow + " vol:" + buyAmountlow;
	}
	
	public BigDecimal calculateBuyAmount(BigDecimal buyPrice, BigDecimal baseAmountJPY) {
		BigDecimal retValue = baseAmountJPY.divide(buyPrice, roundAmt, BigDecimal.ROUND_HALF_UP);
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
		BigDecimal lastPrice = ticker.last;
		BigDecimal lowPrice = ticker.low;
//		System.out.println("lastPrice:" + lastPrice + "/ lowPrice:" + lowPrice);
		lastPrice = lastPrice.multiply(new BigDecimal(percent1));
	    lowPrice = lowPrice.multiply(new BigDecimal(percent2));
//	    System.out.println("#lastPrice:" + lastPrice + "/ #lowPrice:" + lowPrice);
	    BigDecimal retValue = lastPrice.add(lowPrice);
	    retValue = retValue.setScale(roundPrice, BigDecimal.ROUND_HALF_UP);
//	    System.out.println("calculateBuyPrice: " + retValue);
	    return retValue;
	}
	
	public void showTicker() throws BitbankException, IOException {
		this.ticker = this.bb.getTicker(this.pair);
		System.out.println(this.ticker); 
	}
	
	private boolean isBTCbasePair() {
		if(this.pair.equals(CurrencyPair.ETH_BTC) ||
		   this.pair.equals(CurrencyPair.MONA_BTC) ||
		   this.pair.equals(CurrencyPair.BCC_BTC) ||
		   this.pair.equals(CurrencyPair.LTC_BTC) ) {
			return true;
		}
		return false;
	}
}
