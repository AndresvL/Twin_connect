package DAO;

import java.sql.*;

import model.Token;

public class TokenDAO {
	private Connection con = null;
	private Statement statement;
	private ResultSet output;

	public void createDatabaseConnection() throws SQLException {
		String url = "jdbc:mysql://localhost:8889/Twinfield";
		String user = "root";
		String password = "root";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = (Connection) DriverManager.getConnection(url, user, password);
			statement = con.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public Token getAccessToken() throws SQLException{
		Token t = null;
		this.createDatabaseConnection();
		output = statement.executeQuery("SELECT * FROM oauthcredentials");
		while (output.next()){
			String accessToken = output.getString("accessToken");
			String accessSecret = output.getString("accessSecret");
			String consumerToken = output.getString("consumerToken");
			String consumerSecret = output.getString("consumerSecret");
			t = new Token(accessToken, accessSecret, consumerToken, consumerSecret);
			break;
		}
		return t;
		
	}

	public void saveAccesToken(Token t) throws SQLException {	
		createDatabaseConnection();	
		statement.execute("INSERT INTO oauthcredentials (accessToken, accessSecret, consumerToken, consumerSecret)"+ 
		"VALUES ('" + t.getAccessToken() + "','" + t.getAccessSecret() + "','" + t.getConsumerToken() + "','"+ t.getConsumerSecret() + "')");	
		System.out.println("Succes saving credentials " + t.getAccessToken());
	}
}
