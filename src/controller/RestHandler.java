package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.ServletException;

import org.json.JSONException;
import org.json.JSONObject;

import object.rest.Employee;
import object.rest.HourType;
import object.rest.Material;
import object.rest.Project;
import object.rest.Relation;

public class RestHandler {
	private static String version = "7";
	final static String softwareToken = "622a8ef3a712344ef07a4427550ae1e2b38e5342";

	public static int checkToken(String token) {
		String link = "https://www.werkbonapp.nl/openapi/" + version + "/employees/?token=" + token + "&software_token="
				+ softwareToken;

		int code = 0;
		String output = null;
		try {
			URL url = new URL(link);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");

			// OutputStream os =
			// conn.getOutputStream();os.write(input.getBytes());os.flush();

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			while ((output = br.readLine()) != null) {

				JSONObject json = new JSONObject(output);
				code = json.getInt("code");
			}
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
		return code;

	}

	public static void addData(String token, Object array, String type) throws ServletException, IOException {
		String link = "https://www.werkbonapp.nl/openapi/" + version + "/" + type + "/?token=" + token
				+ "&software_token=" + softwareToken;
		URL url = new URL(link);
		String input = null;
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		switch (type) {
		case "employees":
			input = employeeInput(array);
			break;
		case "projects":
			input = projectInput(array);
			break;
		case "relations":
			input = relationInput(array);
			break;
		case "materials":
			input = materialInput(array);
			break;
		case "hourtypes":
			input = hourtypeInput(array);
			break;
		}

		OutputStream os = conn.getOutputStream();
		os.write(input.getBytes());
		os.flush();

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		String output;
		// System.out.println("Output from Server .... \n");
		// while ((output = br.readLine()) != null) {
		// System.out.println(output);
		// }

		conn.disconnect();

	}

	public static String employeeInput(Object obj) {
		@SuppressWarnings("unchecked")
		ArrayList<Employee> array = (ArrayList<Employee>) obj;
		String input = "[";
		int i = 1;
		for (Employee e : array) {
			if (i == array.size()) {
				input += "{\"firstname\":\"" + e.getFirstName() + "\",\"lastname\":\"" + e.getLastName()
						+ "\",\"number\":\"" + e.getCode() + "\"}";
			} else {
				i++;
				input += "{\"firstname\":\"" + e.getFirstName() + "\",\"lastname\":\"" + e.getLastName()
						+ "\",\"number\":\"" + e.getCode() + "\"},";
			}
		}
		return input += "]";
	}

	public static String projectInput(Object obj) {
		@SuppressWarnings("unchecked")
		ArrayList<Project> array = (ArrayList<Project>) obj;
		String input = "[";
		int i = 1;
		for (Project p : array) {
			if (i == array.size()) {
				input += "{\"code\":\"" + p.getCode() + "\",\"code_ext\":\"" + p.getCode_ext()
						+ "\",\"debtor_number\":\"" + p.getDebtor_number() + "\",\"status\":\"" + p.getStatus()
						+ "\",\"name\":\"" + p.getName() + "\",\"description\":\"" + p.getDescription()
						+ "\",\"progress\":\"" + p.getProgress() + "\",\"date_start\":\"" + p.getDate_start()
						+ "\",\"date_end\":\"" + p.getDate_end() + "\",\"active\":\"" + p.getActive() + "\"}";
			} else {
				i++;
				input += "{\"code\":\"" + p.getCode() + "\",\"code_ext\":\"" + p.getCode_ext()
						+ "\",\"debtor_number\":\"" + p.getDebtor_number() + "\",\"status\":\"" + p.getStatus()
						+ "\",\"name\":\"" + p.getName() + "\",\"description\":\"" + p.getDescription()
						+ "\",\"progress\":\"" + p.getProgress() + "\",\"date_start\":\"" + p.getDate_start()
						+ "\",\"date_end\":\"" + p.getDate_end() + "\",\"active\":\"" + p.getActive() + "\"},";
			}
		}
		return input += "]";
	}

	public static String relationInput(Object obj) {
		@SuppressWarnings("unchecked")
		ArrayList<Relation> array = (ArrayList<Relation>) obj;
		String input = "[";
		int i = 1;
		for (Relation r : array) {
			if (i == array.size()) {
				i++;
				input += "{\"name\":\"" + r.getName() + "\",\"debtor_number\":\"" + r.getDebtorNumber()
						+ "\",\"contact\":\"" + r.getContact() + "\",\"phone_number\":\"" + r.getPhoneNumber()
						+ "\",\"email\":\"" + r.getEmail() + "\",\"email_workorder\":\"" + r.getEmailWorkorder()
						+ "\",\"street\":\"" + r.getStreet() + "\",\"house_number\":\"" + r.getHouseNumber()
						+ "\",\"postal_code\":\"" + r.getPostalCode() + "\",\"city\":\"" + r.getCity()
						+ "\",\"remark\":\"" + r.getRemark() + "\"}";
			} else {
				i++;
				input += "{\"name\":\"" + r.getName() + "\",\"debtor_number\":\"" + r.getDebtorNumber()
						+ "\",\"contact\":\"" + r.getContact() + "\",\"phone_number\":\"" + r.getPhoneNumber()
						+ "\",\"email\":\"" + r.getEmail() + "\",\"email_workorder\":\"" + r.getEmailWorkorder()
						+ "\",\"street\":\"" + r.getStreet() + "\",\"house_number\":\"" + r.getHouseNumber()
						+ "\",\"postal_code\":\"" + r.getPostalCode() + "\",\"city\":\"" + r.getCity()
						+ "\",\"remark\":\"" + r.getRemark() + "\"},";
			}
		}
		return input += "]";
	}

	public static String materialInput(Object obj) {
		@SuppressWarnings("unchecked")
		ArrayList<Material> array = (ArrayList<Material>) obj;
		String input = "[";
		int i = 1;
		for (Material m : array) {
			String code = m.getSubCode();
			if (m.getSubCode().equals("")) {
				code = m.getCode();
			}
			if (i == array.size()) {
				input += "{\"code\":\"" + code + "\",\"description\":\"" + m.getDescription() + "\",\"price\":\""
						+ m.getPrice() + "\",\"unit\":\"" + m.getUnit() + "\"}";
			} else {
				i++;
				input += "{\"code\":\"" + code + "\",\"description\":\"" + m.getDescription() + "\",\"price\":\""
						+ m.getPrice() + "\",\"unit\":\"" + m.getUnit() + "\"},";
			}
		}
		return input += "]";
	}

	public static String hourtypeInput(Object obj) {
		@SuppressWarnings("unchecked")
		ArrayList<HourType> array = (ArrayList<HourType>) obj;
		String input = "[";
		int i = 1;
		for (HourType h : array) {
			if (i == array.size()) {
				input += "{\"code\":\"" + h.getCode() + "\",\"name\":\"" + h.getName() + "\",\"cost_booking\":\""
						+ h.getCostBooking() + "\",\"sale_booking\":\"" + h.getSaleBooking() + "\",\"cost_price\":\""
						+ h.getCostPrice() + "\",\"sale_price\":\"" + h.getSalePrice() + "\",\"active\":\""
						+ h.getActive() + "\"}";
			} else {
				i++;
				input += "{\"code\":\"" + h.getCode() + "\",\"name\":\"" + h.getName() + "\",\"cost_booking\":\""
						+ h.getCostBooking() + "\",\"sale_booking\":\"" + h.getSaleBooking() + "\",\"cost_price\":\""
						+ h.getCostPrice() + "\",\"sale_price\":\"" + h.getSalePrice() + "\",\"active\":\""
						+ h.getActive() + "\"},";
			}
		}
		return input += "]";
	}
}
