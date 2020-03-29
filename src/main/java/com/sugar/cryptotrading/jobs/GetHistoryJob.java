package com.sugar.cryptotrading.jobs;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.sugar.cryptotrading.jobs.impl.BitbankJobImpl;
import com.sugar.cryptotrading.jobs.impl.ZaifJobImpl;

import java.sql.ResultSet;

public class GetHistoryJob {
//	.forName("com.mysql.jdbc.Driver");
	
	public void getHistory() {
		String since = "2020/03/03 0:00:00";
		new ZaifJobImpl().getHistory(since);
		new BitbankJobImpl().getHistory(since);
	}
	
	public void getData() {
		try {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			System.out.println("No Mysql jdbc dirver found......");
			e.printStackTrace();
		}
		
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://ec2-13-230-90-25.ap-northeast-1.compute.amazonaws.com/trading", "xxxxx", "xxxxx");
			String query = "select * from buy_orders where executed = ?";
			PreparedStatement prest = conn.prepareStatement(query);
			prest.setInt(1, 0);
			
			ResultSet result = prest.executeQuery();
			
			while(result.next()) {
				String orderid = result.getString("orderid");
				String pair = result.getString("pair");
				String price = result.getString("price");
				String exchange = result.getString("exchange");
				System.out.println(orderid + " / "+ pair + " / " + price + " / " + exchange);
			}
			
			result.close();
			prest.close();
			conn.close();
			
		}catch(SQLException e) {
			System.out.println("Connection failed......");
			e.printStackTrace();
		}
		
		
		
	}

}
