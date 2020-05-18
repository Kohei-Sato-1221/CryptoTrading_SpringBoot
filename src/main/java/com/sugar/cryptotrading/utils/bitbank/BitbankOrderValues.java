package com.sugar.cryptotrading.utils.bitbank;

import java.io.IOException;
import java.math.BigDecimal;

import cc.bitbank.Bitbankcc;
import cc.bitbank.entity.enums.CurrencyPair;
import cc.bitbank.exception.BitbankException;

public class BitbankOrderValues {
	private CurrencyPair pair;
	private BigDecimal baseAmountJPY;
	private BigDecimal baseAmountJPYLow; 
	
	public BitbankOrderValues(String pairStr, String baseAmountJPY, String baseAmountJPYLow){
		this.setPair(convertPair(pairStr));
		this.setBaseAmountJPY(new BigDecimal(baseAmountJPY));
		this.setBaseAmountJPYLow(new BigDecimal(baseAmountJPYLow));					
	}
	
	
	public CurrencyPair convertPair(String pairStr) {
		switch (pairStr) {
		case "btc_jpy":
			return CurrencyPair.BTC_JPY;
		case "ltc_btc":
			return CurrencyPair.LTC_BTC;
		case "xrp_jpy":
			return CurrencyPair.XRP_JPY;
		case "eth_btc":
			return CurrencyPair.ETH_BTC;
		case "mona_jpy":
			return CurrencyPair.MONA_JPY;
		case "mona_btc":
			return CurrencyPair.MONA_BTC;
		case "bcc_jpy":
			return CurrencyPair.BCC_JPY;
		case "bcc_btc":
			return CurrencyPair.BCC_BTC;
		default:
			return CurrencyPair.BTC_JPY;
		}
	}
		
	public CurrencyPair getPair() {
		return pair;
	}

	public void setPair(CurrencyPair pair) {
		this.pair = pair;
	}

	public BigDecimal getBaseAmountJPYLow() throws BitbankException, IOException {
		return baseAmountJPYLow;			
	}

	public void setBaseAmountJPYLow(BigDecimal baseAmountJPYLow) {
		this.baseAmountJPYLow = baseAmountJPYLow;
	}

	public BigDecimal getBaseAmountJPY() {
		return baseAmountJPY;
	}

	public void setBaseAmountJPY(BigDecimal baseAmountJPY) {
		this.baseAmountJPY = baseAmountJPY;
	}
}
