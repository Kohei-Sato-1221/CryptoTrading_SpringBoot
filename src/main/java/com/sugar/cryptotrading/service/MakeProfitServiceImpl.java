package com.sugar.cryptotrading.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MakeProfitServiceImpl implements MakeProfitService{

	@Override
	public void test() {

		System.out.println("--XXX---");
        String servername     = "localhost";
        String databasename   = "senngoku";
        String user = "root";
        String password = "1467Sugar0228!";
        String serverencoding = "UTF-8";
        String url =  "jdbc:mysql://" + servername + "/" + databasename;
        
        Connection con = null;
        
        try {
        	Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        	con = DriverManager.getConnection(url, user, password);
        	Statement st = con.createStatement();
        	
        	String query = "select * from buy_orders";
        	ResultSet result = st.executeQuery(query);
        	
        	while(result.next()) {
        		String orderId = result.getString("orderId");
        		String pair = result.getString("pair");
        		System.out.println("$" + orderId + "/" + pair);
        	}
        }catch(Exception e) {
        	
        }
	}

}
