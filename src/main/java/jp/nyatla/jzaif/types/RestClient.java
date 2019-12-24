package jp.nyatla.jzaif.types;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class RestClient {
	private static String restApiKey = "";
	
	/*　参考：http://www.techscore.com/blog/2016/09/20/jersey-client-api/　*/
	public static String get(String subject, String content) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("https://script.google.com")
			    .path("/macros/s/AKfycbwPXXdYUU00dkylvvfOveP1RXHyuI78FNTdV_da0MWingkL_No/exec")
			    .queryParam("key", restApiKey)
			    .queryParam("subject", subject)
			    .queryParam("content", content);
			 
		
			String result;
			try {
			    result = target.request().get(String.class);
			} catch (BadRequestException e) {
				result = "error: response=" + e.getResponse().readEntity(String.class);
			    System.out.println("error: response=" + e.getResponse().readEntity(String.class));
			    throw e;
			}
		return result;
	}
	
	public static void setRestApiKey(String restApiKey) {
		RestClient.restApiKey = restApiKey;
	}
}
