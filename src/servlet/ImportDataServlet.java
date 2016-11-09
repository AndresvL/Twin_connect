package servlet;

import java.io.*;
import java.sql.SQLException;
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
		System.out.println("importdataservlet office = " + office);
		String session = (String) req.getSession().getAttribute("session");
		ArrayList<String> responseArray = null;
		String[][] options = null;
		Search searchObject;

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
			if (emp != null) {
				try {
					ObjectDAO.saveEmployees(emp);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			break;
		case "getMaterials":
			// Create search object
			// Parameters: type, pattern, field, firstRow, maxRows, options
			options = null;
			searchObject = new Search("ART", "*", 0, 1, 100, options);
			responseArray = SoapHandler.createSOAPFinder(session, searchObject);
			ArrayList<Material> materials = new ArrayList<Material>();
			for (int i = 0; i < responseArray.size(); i++) {
				String[] parts = responseArray.get(i).split(",");
				Material m = (Material) SoapHandler.createSOAPXML(session,
						"<type>article</type><office>" + office + "</office><code>" + parts[0] + "</code>", "material");
				materials.add(m);
			}
			if (materials != null) {
				try {
					ObjectDAO.saveMaterials(materials);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
				Project p = (Project) SoapHandler.createSOAPXML(session, "<type>dimensions</type><office>" + office
						+ "</office><dimtype>PRJ</dimtype><code>" + parts[0] + "</code>", "project");
				projects.add(p);
			}
			if (projects != null) {
				try {
					ObjectDAO.saveProjects(projects);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
				Relation r = (Relation) SoapHandler.createSOAPXML(session, "<type>dimensions</type><office>" + office
						+ "</office><dimtype>DEB</dimtype><code>" + parts[0] + "</code>", "relation");
				relations.add(r);
			}
			if (relations != null) {
				try {
					ObjectDAO.saveRelations(relations);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		case "getHourTypes":
			// Create search object
			// Parameters: type, pattern, field, firstRow, maxRows, options
			options = new String[][] { { "ArrayOfString", "string", "office", office } };
			searchObject = new Search("USR", "*", 0, 1, 100, options);
			responseArray = SoapHandler.createSOAPFinder(session, searchObject);
//			if(hourTypes != null){
//				
//			}
			break;
		}
		String temp = "ArrayList:\n";
		for (int i = 0; i < responseArray.size(); i++) {
			temp += responseArray.get(i) + ", \n";

		}
		RequestDispatcher rd = req.getRequestDispatcher("adapter.jsp");
		req.getSession().setAttribute("currentOffice", office);
		req.getSession().setAttribute("soap", temp);
		rd.forward(req, resp);
	}
}
