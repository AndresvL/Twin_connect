package DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import DBUtil.DBConnection;
import object.Employee;
import object.Material;
import object.Project;
import object.Relation;

public class ObjectDAO {
	private static Statement statement;
	
	public static void saveEmployees(ArrayList<Employee> emp) {
		try{
			Connection con = DBConnection.createDatabaseConnection();
			statement = con.createStatement();
			for (Employee e : emp) {
				statement.execute("INSERT INTO employees (code, firstname, lastname)" + "VALUES ('" 
						+ e.getCode() + "','"
						+ e.getFirstName() + "','" 
						+ e.getLastName() + "')");
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void saveMaterials(ArrayList<Material> mat) {
		try{
			Connection con = DBConnection.createDatabaseConnection();
			statement = con.createStatement();
			for (Material m : mat) {
				statement.execute("INSERT INTO materials (code, description, price, unit)" + "VALUES ('" 
						+ m.getCode() + "','"
						+ m.getDescription() + "','" 
						+ m.getPrice() + "','" 
						+ m.getUnit() + "')");
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void saveProjects(ArrayList<Project> projects) {
		Connection con;
		try {
			con = DBConnection.createDatabaseConnection();
		
		statement = con.createStatement();
		for (Project p : projects) {
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void saveRelations(ArrayList<Relation> relations) {
		try{
			Connection con = DBConnection.createDatabaseConnection();
			statement = con.createStatement();
			for (Relation r : relations) {
				statement.execute("INSERT INTO relations (name, debtor_number, contact, phone_number, email, email_workorder, street, house_number, postal_code, city, remark)" + "VALUES ('" 
						+ r.getName() + "','"
						+ r.getDebtorNumber() + "','"
						+ r.getContact() + "','"
						+ r.getPhoneNumber() + "','"
						+ r.getEmail() + "','"
						+ r.getEmailWorkorder() + "','"
						+ r.getStreet() + "','"
						+ r.getHouseNumber() + "','" 
						+ r.getPostalCode() + "','" 
						+ r.getCity() + "','" 
						+ r.getRemark() + "')");
			}
			con.close();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
