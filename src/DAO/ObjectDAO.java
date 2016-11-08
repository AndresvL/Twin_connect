package DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import DBUtil.DBConnection;
import object.Employee;

public class ObjectDAO {
	private static Statement statement;
	

	public static void saveEmployee(ArrayList<Employee> emp) throws SQLException {
		Connection con = DBConnection.createDatabaseConnection();
		statement = con.createStatement();
		for (Employee e : emp) {
			System.out.println("name " + e.getLastName());
			System.out.println("code " + e.getCode());
			statement.execute("INSERT INTO employees (code, firstname, lastname)" + "VALUES ('" + e.getCode() + "','"
					+ e.getFirstName() + "','" + e.getLastName() + "')");
		}
		con.close();
	}
}
