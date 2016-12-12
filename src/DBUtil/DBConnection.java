package DBUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static Connection con = null;
	public static Connection createDatabaseConnection() throws SQLException {
		String url = "jdbc:mysql://localhost/Twinfield";
		String user = "root";
		String password = "";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = (Connection) DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return con;
	}
}
