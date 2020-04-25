package com.sugar.cryptotrading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.sugar.cryptotrading.jobs.GetHistoryJob;

@SpringBootApplication
@EnableScheduling
public class CryptoTradingSpringBootApplication {

	public static void main(String[] args) {
		String since = "2020/04/09 0:00:00";
		new GetHistoryJob().getHistory(since);
//		SpringApplication.run(CryptoTradingSpringBootApplication.class, args);
	}

}
