package dbUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
	private Connection con = null;
	
	public Connection readDatabase() throws SQLException {
		String url = "jdbc:mysql://localhost:8889/iac";
		String user = "root";
		String password = "root"; 
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = (Connection) DriverManager.getConnection(url, user, password);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return con;		
	}

	public void closeConnection() throws SQLException {
		con.close();
	}

}
