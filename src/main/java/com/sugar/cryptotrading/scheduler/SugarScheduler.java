package com.sugar.cryptotrading.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sugar.cryptotrading.jobs.GetHistoryJob;
import com.sugar.cryptotrading.jobs.TradingJob;
import com.sugar.cryptotrading.jobs.impl.BitbankJobImpl;
import com.sugar.cryptotrading.jobs.impl.ZaifJobImpl;


@Component
public class SugarScheduler{

//	@Scheduled(fixedRate = 86400000, initialDelay = 1000)
	@Scheduled(cron = "0 45 21 * * *", zone = "Asia/Tokyo")
	public void dailyScheduledJob01() {
		System.out.println("### SugarScheduler#dailyScheduledJob01 START!!");
		TradingJob bbJob = new BitbankJobImpl();
		TradingJob zaifJob = new ZaifJobImpl();
		
		bbJob.placeBuyOrders(0);
		zaifJob.placeBuyOrders(0);
		
		System.out.println("### SugarScheduler#dailyScheduledJob01 END!!");
	}
	
//	@Scheduled(fixedRate = 86400000, initialDelay = 1000)
	@Scheduled(cron = "0 45 09 * * *", zone = "Asia/Tokyo")
	public void dailyScheduledJob02() {
		System.out.println("### SugarScheduler#dailyScheduledJob02 START!!");
		TradingJob bbJob = new BitbankJobImpl();
		TradingJob zaifJob = new ZaifJobImpl();
		
		bbJob.placeBuyOrders(1);
		zaifJob.placeBuyOrders(1);
		
		System.out.println("### SugarScheduler#dailyScheduledJob02 END!!");
	}
	
}
