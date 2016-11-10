package servlet;

import java.io.*;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import DAO.ObjectDAO;
import controller.SoapHandler;
import object.Employee;
import object.Material;
import object.Project;
import object.Relation;
import object.Search;

public class ImportDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String button = req.getParameter("category");
		String office = req.getParameter("offices");
		String session = (String) req.getSession().getAttribute("session");
		ArrayList<String> responseArray = null;
		String[][] options = null;
		Search searchObject;
		String errorMessage = null;

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
				ObjectDAO.saveEmployees(emp);
			} else {
				errorMessage = "No Employees found";
			}
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
				ObjectDAO.saveMaterials(materials);
			} else {
				errorMessage = "Office " + office + " heeft geen materialen";
			}
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
				ObjectDAO.saveProjects(projects);
			}else {
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
				ObjectDAO.saveRelations(relations);
			}else {
				errorMessage = "Office " + office + " heeft geen relations";
			}
			break;
		case "getHourTypes":
			// Create search object
			// Parameters: type, pattern, field, firstRow, maxRows, options
			options = new String[][] { { "ArrayOfString", "string", "office", office } };
			searchObject = new Search("USR", "*", 0, 1, 100, options);
			responseArray = SoapHandler.createSOAPFinder(session, searchObject);
			// if(hourTypes != null){
			//
			// }
			break;
		}
		String temp = "ArrayList:\n";
		for (int i = 0; i < responseArray.size(); i++) {
			temp += responseArray.get(i) + "\n";

		}
		RequestDispatcher rd = req.getRequestDispatcher("adapter.jsp");
		req.getSession().setAttribute("error", errorMessage);
		req.getSession().setAttribute("soap", temp);
		rd.forward(req, resp);
	}
}
