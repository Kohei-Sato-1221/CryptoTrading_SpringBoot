package com.sugar.cryptotrading.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sugar.cryptotrading.jobs.TradingJob;
import com.sugar.cryptotrading.jobs.impl.BitbankJobImpl;
import com.sugar.cryptotrading.jobs.impl.ZaifJobImpl;


@Component
public class SugarScheduler{

	@Scheduled(fixedRate = 86400000)
	public void dailyScheduledJobs() {
		System.out.println("### SugarScheduler#dailyScheduledJobs");
		TradingJob bbJob = new BitbankJobImpl();
		TradingJob zaifJob = new ZaifJobImpl();
		
		System.out.println("### START:bbJob.placeBuyOrders");
		bbJob.placeBuyOrders();
		System.out.println("### END:bbJob.placeBuyOrders");
		System.out.println("### START:zaifJob");
		zaifJob.placeBuyOrders();
		System.out.println("### END:zaifJob");

	}
	
}
