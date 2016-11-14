package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import DBUtil.DBConnection;
import object.rest.Employee;
import object.rest.HourType;
import object.rest.Material;
import object.rest.Project;
import object.rest.Relation;

public class ObjectDAO {
	private static Statement statement;
	private static ResultSet output;
	public static void saveEmployees(ArrayList<Employee> emp, String token) {
		try{
			Connection con = DBConnection.createDatabaseConnection();
			statement = con.createStatement();
			for (Employee e : emp) {
				statement.execute("INSERT INTO employees (code, firstname, lastname, softwareToken)" + "VALUES ('" 
						+ e.getCode() + "','"
						+ e.getFirstName() + "','" 
						+ e.getLastName() + "','" 
						+ token + "')");
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void saveMaterials(ArrayList<Material> mat, String token) {
		try{
			Connection con = DBConnection.createDatabaseConnection();
			statement = con.createStatement();
			for (Material m : mat) {
				statement.execute("INSERT INTO materials (code, description, price, unit, softwareToken)" + "VALUES ('" 
						+ m.getCode() + "','"
						+ m.getDescription() + "','" 
						+ m.getPrice() + "','" 
						+ m.getUnit() + "','" 
						+ token + "')");
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static ArrayList<Material> getMaterials(String token) {
		ArrayList<Material> array = new ArrayList<Material>();
		try{
			Connection con = DBConnection.createDatabaseConnection();
			statement = con.createStatement();
			output = statement.executeQuery("SELECT * FROM materials WHERE softwareToken = '"+ token +"'");
			Material m = null;
			while (output.next()){
				String name = output.getString("name");
//				m = new Material(name);
				array.add(m);
			}
			
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return array;
	}
	public static void saveProjects(ArrayList<Project> projects, String token) {
		Connection con;
		try {
			con = DBConnection.createDatabaseConnection();
		
		statement = con.createStatement();
		for (Project p : projects) {
			statement.execute("INSERT INTO projects (code, code_ext, debtor_number, status, name, description, progress, date_start, date_end, active, softwareToken)" + "VALUES ('" 
					+ p.getCode() + "','"
					+ p.getCode_ext() + "','"
					+ p.getDebtor_number() + "','"
					+ p.getStatus() + "','"
					+ p.getName() + "','"
					+ p.getDescription() + "','"
					+ p.getProgress() + "','"
					+ p.getDate_start() + "','" 
					+ p.getDate_end() + "','" 
					+ p.getActive() +"','" 
					+ token + "')");
		}
		con.close();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void saveRelations(ArrayList<Relation> relations, String token) {
		try{
			Connection con = DBConnection.createDatabaseConnection();
			statement = con.createStatement();
			for (Relation r : relations) {
				statement.execute("INSERT INTO relations (name, debtor_number, contact, phone_number, email, email_workorder, street, house_number, postal_code, city, remark, softwareToken)" + "VALUES ('" 
						+ r.getName() + "','"
						+ r.getDebtorNumber() + "','"
						+ r.getContact() + "','"
						+ r.getPhoneNumber() + "','"
						+ r.getEmail() + "','"
						+ r.getEmailWorkorder() + "','"
						+ r.getStreet().replace("'","''") + "','"
						+ r.getHouseNumber() + "','" 
						+ r.getPostalCode() + "','" 
						+ r.getCity() + "','" 
						+ r.getRemark() + "','" 
						+ token + "')");
			}
			con.close();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void saveHourTypes(ArrayList<HourType> hourtypes, String token) {
		try{
			Connection con = DBConnection.createDatabaseConnection();
			statement = con.createStatement();
			for (HourType h : hourtypes) {
				statement.execute("INSERT INTO hourtypes (code, name, cost_booking, sale_booking, sale_price, cost_price, active, softwareToken)" + "VALUES ('" 
						+ h.getCode() + "','"
						+ h.getName() + "','"
						+ h.getCostBooking() + "','"
						+ h.getSaleBooking() + "','"
						+ h.getCostPrice() + "','"
						+ h.getSalePrice() + "','"
						+ h.getActive() + "','" 
						+ token + "')");				
			}
			con.close();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
