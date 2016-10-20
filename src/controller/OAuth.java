package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Map.Entry;
// OAuth 1.0
public class OAuth {
	//Does this still work if there're multiple users using this methode?
	private static String temporaryToken = null;
	private static String temporarySecret = null; 
	private static String callbackConfirmed = null;
	private static String consumerToken = null;
	private static String consumerSecret = null;
	private static String accessToken = null;
	private static String accessSecret = null;
	
	public String getTempToken(String consumer_key, String consumer_secret)
			throws ClientProtocolException, IOException {
		String uri = "https://login.twinfield.com/oauth/initiate.aspx";
		consumerToken = consumer_key;
		consumerSecret = consumer_secret;
		//Change to WorkOrder host
		//action is verify.do
		String callback = "http://localhost:8080/Twinfield_connector/verify.do";
		CloseableHttpClient httpclient;
		httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(uri);
		//Build temporary token request
		//Set consumer_key, consumer_secret_key and callback
		StringBuilder headerReq = new StringBuilder();
		headerReq.append("OAuth ");
		headerReq.append("realm=\"Twinfield\", ");
		headerReq.append("oauth_consumer_key=\"" + consumerToken + "\", ");
		headerReq.append("oauth_signature_method=\"PLAINTEXT\", ");
		headerReq.append("oauth_timestamp=\"\", ");
		headerReq.append("oauth_nonce=\"\", ");
		headerReq.append("oauth_callback=\"" + callback + "\", ");
		headerReq.append("oauth_signature=\"" + consumer_secret + "&\"");

		httpGet.addHeader("Authorization", headerReq.toString());
		CloseableHttpResponse response = httpclient.execute(httpGet);
		
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
		    //Get temporary token and secret_temporary token
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
			System.out.println("tempToken: "+ temporaryToken);
			System.out.println("tempSecret: "+ temporarySecret);
			System.out.println();
		}finally{
			response.close();
		}
		return temporaryToken;
	}
	
	public String getAccessToken(String tempToken, String verifyToken) throws ClientProtocolException, IOException{
		String uri = "https://login.twinfield.com/oauth/finalize.aspx";
		CloseableHttpClient httpclient;
		httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(uri);
//		//Build access token request
//		//Set consumer_key, consumer_secret_key, temporary_secret_key, temporary_key and verify_key
		StringBuilder headerReq = new StringBuilder();
		headerReq.append("OAuth ");
		headerReq.append("realm=\"Twinfield\", ");
		headerReq.append("oauth_consumer_key=\"" + consumerToken + "\", ");
		headerReq.append("oauth_signature_method=\"PLAINTEXT\", ");
		headerReq.append("oauth_signature=\"" + consumerSecret + "&"+ temporarySecret +"\", ");
		headerReq.append("oauth_token=\"" + tempToken + "\", ");
		headerReq.append("oauth_verifier=\""+ verifyToken +"\"");
		
		httpGet.addHeader("Authorization", headerReq.toString());
		CloseableHttpResponse response = httpclient.execute(httpGet);
		
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
		    //Get access token and secret_access token
			for(Entry<String, String> entry : map.entrySet()){
				if(entry.getKey().equals("oauth_token")){
					accessToken = entry.getValue();
				}
				if(entry.getKey().equals("oauth_token_secret")){
					accessSecret = entry.getValue();
				}
			}
			System.out.println("accessToken: "+ accessToken);
			System.out.println("accessSecret: "+ accessSecret);
		}finally{
			response.close();
		}
		return accessToken;
	}
}