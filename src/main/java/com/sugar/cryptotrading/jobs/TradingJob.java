package com.sugar.cryptotrading.jobs;

public interface TradingJob {
	void placeBuyOrders(int strategy);
	void getHistory(String sinceDate);
}
