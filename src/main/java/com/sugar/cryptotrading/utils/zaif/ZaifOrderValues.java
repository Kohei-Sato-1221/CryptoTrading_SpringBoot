package com.sugar.cryptotrading.utils.zaif;

import java.math.BigDecimal;

import jp.nyatla.jzaif.types.CurrencyPair;


public class ZaifOrderValues {
	private CurrencyPair pair;
	private BigDecimal baseAmountJPY;
	private BigDecimal baseAmountJPYLow; 
	
	public ZaifOrderValues(String pairStr, String baseAmountJPY, String baseAmountJPYLow){
		this.setPair(convertPair(pairStr));
		this.setBaseAmountJPY(new BigDecimal(baseAmountJPY));
		this.setBaseAmountJPYLow(new BigDecimal(baseAmountJPYLow));					
	}
	
	private CurrencyPair convertPair(String pairStr) {
		if(pairStr.equals("xem_jpy")) {
			return CurrencyPair.XEMJPY;
		}else if(pairStr.equals("eth_jpy")){
			return CurrencyPair.ETHJPY;
		}else {
			return null;
		}
	}
			
	public CurrencyPair getPair() {
		return pair;
	}

	public void setPair(CurrencyPair pair) {
		this.pair = pair;
	}

	public BigDecimal getBaseAmountJPYLow() {
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