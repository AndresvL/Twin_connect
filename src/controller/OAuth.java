package controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import model.User;

import java.util.Map.Entry;

public class OAuth {
	private String temporaryToken = null;
	private String temporarySecret = null; 
	private String callbackConfirmed = null;
	
	public String getAccessToken(String consumer_key, String consumer_secret, String link)
			throws ClientProtocolException, IOException {
		String uri = link;
		URL obj = new URL(uri);
		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		conn.setRequestMethod("GET");
		CloseableHttpClient httpclient;
		httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(uri);
		
		//Build request
		//Set consumer_key, consumer_secret_key and callback
		StringBuilder headerReq = new StringBuilder();
		headerReq.append("OAuth ");
		headerReq.append("realm=\"Twinfield\", ");
		headerReq.append("oauth_consumer_key=\"" + consumer_key + "\", ");
		headerReq.append("oauth_signature_method=\"PLAINTEXT\", ");
		headerReq.append("oauth_timestamp=\"\", ");
		headerReq.append("oauth_nonce=\"\", ");
		headerReq.append("oauth_callback=\"" + link + "\", ");
		headerReq.append("oauth_signature=\"" + consumer_secret + "&\"");

		httpGet.addHeader("Authorization", headerReq.toString());
		CloseableHttpResponse response = httpclient.execute(httpGet);
		response.getEntity();
		
		try{
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity);
			String params[] = responseString.split("&");
		    Map<String, String> map = new HashMap<String, String>();  
		    for (String param : params){  
		        String name = param.split("=")[0];  
		        String value = param.split("=")[1];  
		        map.put(name, value);
		    }  
		    //Get access token and secret_acces token
			for(Entry<String, String> entry : map.entrySet()){
				if(entry.getKey().equals("oauth_token")){
					temporaryToken = entry.getValue();
				}
				if(entry.getKey().equals("oauth_token_secret")){
					temporarySecret = entry.getValue();
				}
				if(entry.getKey().equals("oauth_callback_confirmed")){
					callbackConfirmed = entry.getValue();// Value is always "true".
				}
			}
			System.out.println("token: "+ temporaryToken);
			System.out.println("secret: "+ temporarySecret);
			System.out.println("callback: "+ callbackConfirmed);
		}finally{
			response.close();
			
		}
		return temporaryToken;
	}
	public User getData(){
		User user = new User();
		
		
		return user;
		
	}
}