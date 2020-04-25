package com.sugar.cryptotrading.restapi;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;

import cc.bitbank.entity.Trade;
import jp.nyatla.jzaif.api.result.TradeHistoryResult;

public class RestClient {
	private static String restApiKey = "";
	private static String apiKey = "";
	
	/*　参考：http://www.techscore.com/blog/2016/09/20/jersey-client-api/　*/
//	public static String get(String subject, String content) {
//		Client client = ClientBuilder.newClient();
//		WebTarget target = client.target("https://script.google.com")
//				.path("/macros/s/AKfycbwPXXdYUU00dkylvvfOveP1RXHyuI78FNTdV_da0MWingkL_No/exec")
//				.queryParam("key", restApiKey)
//				.queryParam("subject", subject)
//				.queryParam("content", content);
//		
//		
//		String result;
//		try {
//			result = target.request().get(String.class);
//		} catch (BadRequestException e) {
//			result = "error: response=" + e.getResponse().readEntity(String.class);
//			System.out.println("error: response=" + e.getResponse().readEntity(String.class));
//			throw e;
//		}
//		return result;
//	}
	
	public String saveTradeResults(TradeHistoryResult.Item item) {
		 URIBuilder builder = new URIBuilder();
		 String side = item.action.toString() == "ASK" ? "bid" : "ask";
		 builder.setScheme("https").setHost("script.google.com").setPath("/macros/s/AKfycbwPXXdYUU00dkylvvfOveP1RXHyuI78FNTdV_da0MWingkL_No/exec")
		 .setParameter("pair", item.currency_pair.toString())
		 .setParameter("side", side)
		 .setParameter("price", "" + item.price)
		 .setParameter("amount", "" + item.amount)
		 .setParameter("date", item.timestamp.toString().replace(" ", "/"));
		 
		 String result = null;
		 try( CloseableHttpClient httpClient = HttpClients.createDefault();){
			 URI uri = builder.build();
			 HttpGet httpget = new HttpGet(uri);
			 System.out.println(httpget.getURI());
			 
			 try(CloseableHttpResponse response = httpClient.execute(httpget);){
				 int sc = response.getStatusLine().getStatusCode();				 
			 }catch(Exception e1) {
				 throw e1;
			 }
		 }catch(Exception e2) {
			 e2.printStackTrace();
		 }
		 return result;
	}
		
	public String saveTradeResults(Trade trade) {
		 URIBuilder builder = new URIBuilder();
		 builder.setScheme("https").setHost("script.google.com").setPath("/macros/s/AKfycbwPXXdYUU00dkylvvfOveP1RXHyuI78FNTdV_da0MWingkL_No/exec")
		 .setParameter("pair", trade.pair)
		 .setParameter("side", trade.side.toString())
		 .setParameter("price", "" + trade.price)
		 .setParameter("amount", "" + trade.amount)
		 .setParameter("date", trade.executedAt.toString().replace(" ", "/"));
		 
		 String result = null;
		 CloseableHttpClient client = null;
		 CloseableHttpResponse response = null;
		 try {
			 URI uri = builder.build();
			 HttpGet httpget = new HttpGet(uri);
			 System.out.println(httpget.getURI());
			 client = HttpClients.createDefault();
			 response = client.execute(httpget);
			 int sc = response.getStatusLine().getStatusCode(); //. 200 の想定
			 HttpEntity entity = response.getEntity();
			 result = EntityUtils.toString( entity, "UTF-8" );
			 System.out.println("status:" + sc + "/result:" + result);
		 }catch(Exception e) {
			 e.printStackTrace();
		 }finally {
			 try {
				client.close();
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		 }
		 return result;
	}
	
	
	
	public static void setRestApiKey(String restApiKey) {
		RestClient.restApiKey = restApiKey;
	}
	
	public static void setApiKey(String apiKey) {
		RestClient.apiKey = apiKey;
	}
}
