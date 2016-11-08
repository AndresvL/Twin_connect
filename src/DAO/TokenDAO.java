package DAO;

import java.sql.*;

import DBUtil.DBConnection;
import object.Token;

public class TokenDAO {
	private Statement statement;
	private ResultSet output;
	public Token getAccessToken() throws SQLException{
		Token t = null;
		
		Connection con = DBConnection.createDatabaseConnection();
		statement = con.createStatement();
		output = statement.executeQuery("SELECT * FROM credentials");
		while (output.next()){
			String accessToken = output.getString("accessToken");
			String accessSecret = output.getString("accessSecret");
			String consumerToken = output.getString("consumerToken");
			String consumerSecret = output.getString("consumerSecret");
			t = new Token(consumerToken, consumerSecret, accessToken, accessSecret);
			break;
		}
		con.close();
		return t;	
	}
	
	public void saveAccessToken(Token t) throws SQLException {	
		Connection con = DBConnection.createDatabaseConnection();
		statement = con.createStatement();
		statement.execute("INSERT INTO credentials (accessToken, accessSecret, consumerToken, consumerSecret)"+ 
		"VALUES ('" + t.getAccessToken() + "','" + t.getAccessSecret() + "','" + t.getConsumerToken() + "','"+ t.getConsumerSecret() + "')");
		con.close();
	}
}
