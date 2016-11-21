package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import DBUtil.DBConnection;
import object.rest.Address;
import object.rest.Employee;
import object.rest.HourType;
import object.rest.Material;
import object.rest.Project;
import object.rest.Relation;

public class ObjectDAO {
	private static Statement statement;
	// private static ResultSet output;

	public static void saveEmployees(ArrayList<Employee> emp, String token) {
		try {
			Connection con = DBConnection.createDatabaseConnection();
			statement = con.createStatement();
			for (Employee e : emp) {
				if (!checkSoftwareToken(token, "employees", e.getCode(), null)) {
					statement.execute("INSERT INTO employees (code, firstname, lastname, softwareToken)" + "VALUES ('"
							+ e.getCode() + "','" + e.getFirstName() + "','" + e.getLastName() + "','" + token + "')");
				}
			}
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void saveMaterials(ArrayList<Material> mat, String token) {
		try {

			Connection con = DBConnection.createDatabaseConnection();
			statement = con.createStatement();
			for (Material m : mat) {
				if (!checkSoftwareToken(token, "materials", m.getCode(), null)) {
					statement.execute("INSERT INTO materials (code, description, price, unit, softwareToken)"
							+ "VALUES ('" + m.getCode() + "','" + m.getDescription() + "','" + m.getPrice() + "','"
							+ m.getUnit() + "','" + token + "')");
				}
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void saveProjects(ArrayList<Project> projects, String token) {
		Connection con;
		try {
			con = DBConnection.createDatabaseConnection();
			statement = con.createStatement();
			for (Project p : projects) {
				if (!checkSoftwareToken(token, "projects", p.getCode(), null)) {
					statement.execute(
							"INSERT INTO projects (code, code_ext, debtor_number, status, name, description, progress, date_start, date_end, active, softwareToken)"
									+ "VALUES ('" + p.getCode() + "','" + p.getCode_ext() + "','" + p.getDebtor_number()
									+ "','" + p.getStatus() + "','" + p.getName() + "','" + p.getDescription() + "','"
									+ p.getProgress() + "','" + p.getDate_start() + "','" + p.getDate_end() + "','"
									+ p.getActive() + "','" + token + "')");
				}
			}
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void saveRelations(ArrayList<Relation> relations, String token) {
		try {
			Connection con = DBConnection.createDatabaseConnection();
			statement = con.createStatement();
			for (Relation r : relations) {
				for (Address a : r.getAddressess()) {
					String stringCode = a.getAddressId() + "";
					if (!checkSoftwareToken(token, "relations", r.getDebtorNumber(), stringCode)) {
						statement.execute(
								"INSERT INTO relations (name, code, contact, phone_number, email, email_workorder, street, house_number, postal_code, city, remark, type, addressId, softwareToken)"
										+ "VALUES ('" + r.getName() + "','" + r.getDebtorNumber() + "','"
										+ r.getContact() + "','" + a.getPhoneNumber() + "','" + a.getEmail() + "','"
										+ r.getEmailWorkorder() + "','" + a.getStreet().replace("'", "''") + "','"
										+ a.getHouseNumber() + "','" + a.getPostalCode() + "','" + a.getCity() + "','"
										+ a.getRemark() + "','" + a.getType() + "','" + a.getAddressId() + "','" + token
										+ "')");

					} else {
						System.out.println("relatietoken bestaat al");
					}
				}
				con.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	// public ArrayList<Relation> getAddressId(String token, String type) throws
	// SQLException {
	// Relation r = null;
	// Connection con = DBConnection.createDatabaseConnection();
	// statement = con.createStatement();
	// output = statement.executeQuery("SELECT id FROM relations WHERE
	// softwareToken =\"" + token + "\"");
	// while (output.next()) {
	// String accessToken = output.getString("accessToken");
	// String accessSecret = output.getString("accessSecret");
	// String consumerToken = output.getString("consumerToken");
	// String consumerSecret = output.getString("consumerSecret");
	// String softwareToken = output.getString("softwareToken");
	// t = new Token(consumerToken, consumerSecret, accessToken, accessSecret,
	// softwareToken);
	// break;
	// }
	// con.close();
	// return t;
	// }

	public static void saveHourTypes(ArrayList<HourType> hourtypes, String token) {
		try {
			Connection con = DBConnection.createDatabaseConnection();
			statement = con.createStatement();
			for (HourType h : hourtypes) {
				if (!checkSoftwareToken(token, "hourtypes", h.getCode(), null)) {
					statement.execute(
							"INSERT INTO hourtypes (code, name, cost_booking, sale_booking, sale_price, cost_price, active, softwareToken)"
									+ "VALUES ('" + h.getCode() + "','" + h.getName() + "','" + h.getCostBooking()
									+ "','" + h.getSaleBooking() + "" + "','" + h.getCostPrice() + "','"
									+ h.getSalePrice() + "','" + h.getActive() + "','" + token + "')");
				}
			}
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Boolean checkSoftwareToken(String softwareToken, String columnName, String codeString,
			String addressCode) throws SQLException {
		Connection con = DBConnection.createDatabaseConnection();
		statement = con.createStatement();
		boolean b = false;
		ResultSet output;
		if (columnName.equals("relations")) {
			int code = Integer.parseInt(addressCode);
			output = statement.executeQuery("SELECT * FROM " + columnName + " WHERE softwareToken =\"" + softwareToken
					+ "\" AND addressId=" + code + " AND code=\"" + codeString + "\"");

		} else {
			System.out.println("code " + codeString);
			output = statement.executeQuery("SELECT * FROM " + columnName + " WHERE softwareToken =\"" + softwareToken
					+ "\" AND code=\"" + codeString + "\"");
		}

		if (output.next()) {
			System.out.println("name = " + output.getString("name"));
			b = true;
		}
		return b;

	}

	public static Address getAddressID(String softwareToken, String addressType, String codeString) {
		Address a = null;
		Connection con;
		try {
			con = DBConnection.createDatabaseConnection();
			statement = con.createStatement();
			ResultSet output = statement.executeQuery("SELECT * FROM relations WHERE softwareToken =\"" + softwareToken
					+ "\" AND type=\"" + addressType + "\"AND code=\"" + codeString + "\"");
			if (output.next()) {
				a = new Address();
				String addressId = output.getString("addressId");
				a.setAddressId(Integer.parseInt(addressId));
				System.out.println("addressID = " + output.getString("addressId"));
				System.out.println("code = " + output.getString("code"));
				con.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return a;

	}
}
