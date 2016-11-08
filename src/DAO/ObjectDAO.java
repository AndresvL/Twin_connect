package DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import DBUtil.DBConnection;
import object.Employee;
import object.Material;
import object.Project;

public class ObjectDAO {
	private static Statement statement;
	
	public static void saveEmployees(ArrayList<Employee> emp) throws SQLException {
		Connection con = DBConnection.createDatabaseConnection();
		statement = con.createStatement();
		for (Employee e : emp) {
			System.out.println("name " + e.getLastName());
			System.out.println("code " + e.getCode());
			statement.execute("INSERT INTO employees (code, firstname, lastname)" + "VALUES ('" 
					+ e.getCode() + "','"
					+ e.getFirstName() + "','" 
					+ e.getLastName() + "')");
		}
		con.close();
	}
	
	public static void saveMaterials(ArrayList<Material> mat) throws SQLException {
		Connection con = DBConnection.createDatabaseConnection();
		statement = con.createStatement();
		for (Material m : mat) {
			System.out.println("code " + m.getCode());
			statement.execute("INSERT INTO materials (code, description, price, unit)" + "VALUES ('" 
					+ m.getCode() + "','"
					+ m.getDescription() + "','" 
					+ m.getPrice() + "','" 
					+ m.getUnit() + "')");
		}
		con.close();
	}

	public static void saveProjects(ArrayList<Project> projects) throws SQLException {
		Connection con = DBConnection.createDatabaseConnection();
		statement = con.createStatement();
		for (Project p : projects) {
			System.out.println("name " + p.getName());
			System.out.println("code " + p.getCode());
			statement.execute("INSERT INTO projects (code, code_ext, debtor_number, status, name, description, progress, date_start, date_end, active)" + "VALUES ('" 
					+ p.getCode() + "','"
					+ p.getCode_ext() + "','"
					+ p.getDebtor_number() + "','"
					+ p.getStatus() + "','"
					+ p.getName() + "','"
					+ p.getDescription() + "','"
					+ p.getProgress() + "','"
					+ p.getDate_start() + "','" 
					+ p.getDate_end() + "','" 
					+ p.getActive() + "')");
		}
		con.close();		
	}
}
