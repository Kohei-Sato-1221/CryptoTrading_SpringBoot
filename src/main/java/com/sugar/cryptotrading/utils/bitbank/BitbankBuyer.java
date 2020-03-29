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
	private BigDecimal minimumBuyAmount;
	private BigDecimal buyPrice;
	private BigDecimal buyAmount;
	
	private int roundPrice = 0;
	private int roundAmt = 0;
	Bitbankcc bb;
	
	public BitbankBuyer(Bitbankcc bb, CurrencyPair pair, BigDecimal baseAmountJPY, BigDecimal minimumBuyAmount, int roundPrice, int roundAmt) {
		this.bb = bb;
		this.pair = pair;
		this.baseAmountJPY = baseAmountJPY;
		this.minimumBuyAmount = minimumBuyAmount;
		this.roundPrice = roundPrice;
		this.roundAmt = roundAmt;
		
//		this.buyPrice = buyPrice;
//		this.buyAmount = buyAmount;
	}
	


	public String sendBuyOrder() throws BitbankException, IOException, NullPointerException{
		BigDecimal baseAmount = baseAmountJPY;
		if(isBTCbasePair()) {
			Ticker tickerForBTC = bb.getTicker(CurrencyPair.BTC_JPY);
			baseAmount = baseAmountJPY.divide(tickerForBTC.last, 8, BigDecimal.ROUND_HALF_UP);			
		}
		System.out.println(buyPrice + " " + buyAmount);
		Order order = bb.sendOrder(pair, buyPrice, buyAmount, OrderSide.BUY, OrderType.LIMIT);
		if(order == null) {
			System.out.println("Order result is Null! ");			
		}else {
			System.out.println("" + order);			
			
		}
		return pair + " price:" + buyPrice + " vol:" + buyAmount;
	}
	
	
	public void calculateBuyAmount() {
		BigDecimal retValue = baseAmountJPY.divide(buyPrice, roundAmt, BigDecimal.ROUND_HALF_UP);
		if(retValue.compareTo(minimumBuyAmount) < 0) {
			System.out.println("set minimuBuyAmount:" + minimumBuyAmount);
			retValue = minimumBuyAmount;
		}
		this.buyAmount = retValue;
	}
	
	public void calculateBuyPriceNormal(BigDecimal lastPrice, BigDecimal lowPrice) {
		this.buyPrice = calculateBuyPrice("0.6","0.4", lastPrice, lowPrice);
	}
	
	public void calculateBuyPriceLower(BigDecimal lastPrice, BigDecimal lowPrice) {
		this.buyPrice = calculateBuyPrice("0.2","0.8", lastPrice, lowPrice);
	}
	
	private BigDecimal calculateBuyPrice(String percent1, String percent2, BigDecimal lastPrice, BigDecimal lowPrice) {
		lastPrice = lastPrice.multiply(new BigDecimal(percent1));
	    lowPrice = lowPrice.multiply(new BigDecimal(percent2));
	    BigDecimal retValue = lastPrice.add(lowPrice);
	    retValue = retValue.setScale(roundPrice, BigDecimal.ROUND_HALF_UP);
	    return retValue;
	}
	
	public void showTicker(Ticker ticker) throws BitbankException, IOException {
		ticker = this.bb.getTicker(this.pair);
		System.out.println(ticker); 
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
