package DAO;

import java.sql.*;

import model.Token;

public class TokenDAO {
	private Connection con = null;
	private Statement statement;
	private ResultSet output;

	public Connection createDatabaseConnection() throws SQLException {
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
		return con;
	}
	public Token getAccessToken() throws SQLException{
		Token t = null;
		Connection con = createDatabaseConnection();
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

	public void saveAccesToken(Token t) throws SQLException {	
		Connection con = createDatabaseConnection();
		System.out.println("Token moet gesaved worden");
		statement.execute("INSERT INTO credentials (accessToken, accessSecret, consumerToken, consumerSecret)"+ 
		"VALUES ('" + t.getAccessToken() + "','" + t.getAccessSecret() + "','" + t.getConsumerToken() + "','"+ t.getConsumerSecret() + "')");
		con.close();
	}
}
