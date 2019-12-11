package com.sugar.crytotrading.tasks;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class zaifTasks {

	@Scheduled(fixedRate = 10000)
	public void placeBuyOrders() {
		System.out.println("zaifTasks#placeBuyOrders");
	}
}
