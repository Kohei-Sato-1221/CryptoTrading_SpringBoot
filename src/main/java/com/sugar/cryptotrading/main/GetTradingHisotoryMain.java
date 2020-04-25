package com.sugar.cryptotrading.main;


import com.sugar.cryptotrading.jobs.GetHistoryJob;

public class GetTradingHisotoryMain{

	public static void main() {
		String since = "2020/04/09 0:00:00";
		new GetHistoryJob().getHistory(since);
	}

}
