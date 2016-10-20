package model;

public class Token {
	protected String consumerKey;
	protected String consumerSecret;
	protected String tempKey;
	protected String tempSecret;
	protected String accessKey;
	protected String accessSecret;
	protected String verifyKey;
	
	public void setConsumerKey(String key){
		this.consumerKey = key;
	}
	public String getConsumerKey(){
		return consumerKey;
	}
	
	public void setConsumerSecret(String key){
		this.consumerSecret = key;
	}
	public String getConsumerSecret(){
		return consumerSecret;
	}
	
	public void setTempKey(String key){
		this.tempKey = key;
	}
	public String getTempKey(){
		return tempKey;
	}
	
	public void setTempSecret(String key){
		this.tempSecret = key;
	}
	public String getTempSecret(){
		return tempSecret;
	}
	
	public void setAccessKey(String key){
		this.accessKey = key;
	}
	public String getAccessKey(){
		return accessKey;
	}
	
	public void setAccessSecret(String key){
		this.accessSecret = key;
	}
	public String getAccessSecret(){
		return accessSecret;
	}	
	
	public void setVerifyKey(String key){
		this.verifyKey = key;
	}
	public String getVerifyKey(){
		return verifyKey;
	}
}
