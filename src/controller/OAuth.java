package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import model.Token;

import java.util.Map.Entry;

// OAuth 1.0
public class OAuth {
	//Does this still work if there're multiple users using this methode?
	private static String callbackConfirmed = null;
	private static Token token;
	
	public String getTempToken(String consumerKey, String consumerSecret)
			throws ClientProtocolException, IOException {
		token = new Token();
		token.setConsumerKey(consumerKey);
		token.setConsumerSecret(consumerSecret);
		String uri = "https://login.twinfield.com/oauth/initiate.aspx";
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
		headerReq.append("oauth_consumer_key=\"" + consumerKey + "\", ");
		headerReq.append("oauth_signature_method=\"PLAINTEXT\", ");
		headerReq.append("oauth_timestamp=\"\", ");
		headerReq.append("oauth_nonce=\"\", ");
		headerReq.append("oauth_callback=\"" + callback + "\", ");
		headerReq.append("oauth_signature=\"" + consumerSecret + "&\"");
		
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
					token.setTempKey(entry.getValue());
				}
				if(entry.getKey().equals("oauth_token_secret")){
					token.setTempSecret(entry.getValue());
				}
				if(entry.getKey().equals("oauth_callback_confirmed")){
					callbackConfirmed = entry.getValue();// Value is always "true".
				}
			}
		}finally{
			response.close();
		}
		return token.getTempKey();
	}
	
	public Token getAccessToken(String tempToken, String verifyToken) throws ClientProtocolException, IOException{
		token.setTempKey(tempToken);
		token.setVerifyKey(verifyToken);
		
		String uri = "https://login.twinfield.com/oauth/finalize.aspx";
		CloseableHttpClient httpclient;
		httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(uri);
		
//		//Build access token request
//		//Set consumer_key, consumer_secret_key, temporary_secret_key, temporary_key and verify_key
		StringBuilder headerReq = new StringBuilder();
		headerReq.append("OAuth ");
		headerReq.append("realm=\"Twinfield\", ");
		headerReq.append("oauth_consumer_key=\"" + token.getConsumerKey() + "\", ");
		headerReq.append("oauth_signature_method=\"PLAINTEXT\", ");
		headerReq.append("oauth_signature=\"" + token.getConsumerSecret() + "&"+ token.getTempSecret() +"\", ");
		headerReq.append("oauth_token=\"" + token.getTempKey() + "\", ");
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
					token.setAccessKey(entry.getValue());
				}
				if(entry.getKey().equals("oauth_token_secret")){
					token.setAccessSecret(entry.getValue());
				}
			}
		}finally{
			response.close();
		}
		return token;
	}
}