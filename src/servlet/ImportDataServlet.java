package servlet;

import java.io.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import DAO.ObjectDAO;
import controller.RestHandler;
import controller.SoapHandler;
import object.Search;
import object.rest.Employee;
import object.rest.HourType;
import object.rest.Material;
import object.rest.Project;
import object.rest.Relation;

public class ImportDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String button = req.getParameter("category");
		String office = req.getParameter("offices");
		String token = (String) req.getSession().getAttribute("softwareToken");
		String session = (String) req.getSession().getAttribute("session");
		ArrayList<String> responseArray = null;
		String[][] options = null;
		Search searchObject;
		String errorMessage = null;
		if (button != null) {
			switch (button) {
			case "getEmployees":
				// Create search object
				// Parameters: type, pattern, field, firstRow, maxRows, options
				options = new String[][] { { "ArrayOfString", "string", "office", office } };
				searchObject = new Search("USR", "*", 0, 1, 100, options);
				responseArray = SoapHandler.createSOAPFinder(session, searchObject);
				ArrayList<Employee> emp = new ArrayList<Employee>();
				// Split data from ArrayList
				for (int i = 0; i < responseArray.size(); i++) {
					String[] parts = responseArray.get(i).split(",");
					// firstName and Lastname are identical
					Employee e = new Employee(parts[1], parts[1], parts[0]);
					emp.add(e);
				}
				if (!emp.isEmpty()) {
					ObjectDAO.saveEmployees(emp, token);
					// Post data to WorkorderApp
					RestHandler.addData(token, emp, "employees");
				} else {
					errorMessage = "No Employees found";
				}
				// setDelay(button);
				break;
			case "getMaterials":
				// Create search object
				// Parameters: type, pattern, field, firstRow, maxRows, options
				options = new String[][] { { "ArrayOfString", "string", "office", office } };
				searchObject = new Search("ART", "*", 0, 1, 100, options);
				responseArray = SoapHandler.createSOAPFinder(session, searchObject);
				ArrayList<Material> materials = new ArrayList<Material>();
				for (int i = 0; i < responseArray.size(); i++) {
					String[] parts = responseArray.get(i).split(",");
					String string = "<type>article</type><office>" + office + "</office><code>" + parts[0] + "</code>";
					Object obj = SoapHandler.createSOAPXML(session, string, "material");
					if (obj != null) {
						Material m = (Material) obj;
						materials.add(m);
					}

				}
				if (!materials.isEmpty()) {
					ObjectDAO.saveMaterials(materials, token);
					RestHandler.addData(token, materials, "materials");
				} else {
					errorMessage = "Office " + office + " heeft geen materialen";
				}
				// setDelay(button);
				break;
			case "getProjects":
				// Create search object
				// Parameters: type, pattern, field, firstRow, maxRows, options
				options = new String[][] { { "ArrayOfString", "string", "dimtype", "PRJ" } };
				searchObject = new Search("DIM", "*", 0, 1, 100, options);
				responseArray = SoapHandler.createSOAPFinder(session, searchObject);
				ArrayList<Project> projects = new ArrayList<Project>();
				for (int i = 0; i < responseArray.size(); i++) {
					String[] parts = responseArray.get(i).split(",");
					// XMLString
					String string = "<type>dimensions</type><office>" + office + "</office><dimtype>PRJ</dimtype><code>"
							+ parts[0] + "</code>";
					Object obj = SoapHandler.createSOAPXML(session, string, "project");
					if (obj != null) {
						Project p = (Project) obj;
						projects.add(p);
					}
				}
				if (!projects.isEmpty()) {
					ObjectDAO.saveProjects(projects, token);
					RestHandler.addData(token, projects, "projects");
				} else {
					errorMessage = "Office " + office + " heeft geen projecten";
				}
				break;
			case "getRelations":
				// Create search object
				// Parameters: type, pattern, field, firstRow, maxRows, options
				options = new String[][] { { "ArrayOfString", "string", "dimtype", "DEB" } };
				searchObject = new Search("DIM", "*", 0, 1, 100, options);
				responseArray = SoapHandler.createSOAPFinder(session, searchObject);
				ArrayList<Relation> relations = new ArrayList<Relation>();
				for (int i = 0; i < responseArray.size(); i++) {
					String[] parts = responseArray.get(i).split(",");
					String string = "<type>dimensions</type><office>" + office + "</office><dimtype>DEB</dimtype><code>"
							+ parts[0] + "</code>";
					Object obj = SoapHandler.createSOAPXML(session, string, "relation");
					if (obj != null) {
						Relation r = (Relation) obj;
						relations.add(r);
					}
				}
				if (!relations.isEmpty()) {
					ObjectDAO.saveRelations(relations, token);
					RestHandler.addData(token, relations, "relations");
				} else {
					errorMessage = "Office " + office + " heeft geen relations";
				}
				break;
			case "getHourTypes":
				// Create search object
				// Parameters: type, pattern, field, firstRow, maxRows, options
				options = new String[][] { { "ArrayOfString", "string", "office", office },
						{ "ArrayOfString", "string", "dimtype", "ACT" } };
				searchObject = new Search("DIM", "*", 0, 1, 100, options);
				responseArray = SoapHandler.createSOAPFinder(session, searchObject);
				ArrayList<HourType> hourtypes = new ArrayList<HourType>();
				for (int i = 0; i < responseArray.size(); i++) {
					String[] parts = responseArray.get(i).split(",");
					String string = "<type>dimensions</type><office>" + office + "</office><dimtype>ACT</dimtype><code>"
							+ parts[0] + "</code>";
					Object obj = SoapHandler.createSOAPXML(session, string, "hourtype");
					if (obj != null) {
						HourType h = (HourType) obj;
						hourtypes.add(h);
					}
				}
				if (!hourtypes.isEmpty()) {
					ObjectDAO.saveHourTypes(hourtypes, token);
					RestHandler.addData(token, hourtypes, "hourtypes");
				} else {
					errorMessage = "Office " + office + " heeft geen hourtypes";
				}
				break;
			case "start":
				setDelay(true);
				break;
			case "stop":
				setDelay(false);
				break;
			}
			// String temp = "ArrayList:\n";
			// for (int i = 0; i < responseArray.size(); i++) {
			// temp += responseArray.get(i) + "\n";
			//
			// }
		}

		RequestDispatcher rd = req.getRequestDispatcher("adapter.jsp");
		req.getSession().setAttribute("error", errorMessage);
		req.getSession().setAttribute("soap", "");
		rd.forward(req, resp);
	}

	public void setDelay(Boolean b) {
		Timer t = new Timer();
		if (b) {
			MyTask mTask = new MyTask();
			// This task is scheduled to run every 10 seconds

			t.scheduleAtFixedRate(mTask, 0, 10000);
		} else {
			t.cancel();
		}
	}

	class MyTask extends TimerTask {
		private int i = 0;

		@Override
		public void run() {
			i++;
			System.out.println("nummer " + i);
		}

	}

}
