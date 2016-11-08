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
import object.Project;
import object.Search;

public class ImportDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String button = req.getParameter("category");
		String code = req.getParameter("code");
		String session = (String) req.getSession().getAttribute("session");
		ArrayList<String> responseArray = null;
		String[][] options = null;
		Search object;

		switch (button) {
		case "getEmployees":
			// Create search object
			// Parameters: type, pattern, field, firstRow, maxRows, options
			options = new String[][] { { "ArrayOfString", "string", "office", code } };
			object = new Search("USR", "*", 0, 1, 100, options);
			responseArray = SoapHandler.createSOAPSearch(session, object);
			ArrayList<Employee> emp = new ArrayList<Employee>();
			// Split data from ArrayList
			for (int i = 0; i < responseArray.size(); i++) {
				String[] parts = responseArray.get(i).split(",");
				// firstName and Lastname are identical
				Employee e = new Employee(parts[1], parts[1], parts[0]);
				emp.add(e);
			}
			try {
				ObjectDAO.saveEmployees(emp);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			break;
		case "getMaterials":
			// Create search object
			// Parameters: type, pattern, field, firstRow, maxRows, options
			options = new String[][] { { "ArrayOfString", "string", "office", code } };
			object = new Search("ART", "*", 0, 1, 100, options);
			responseArray = SoapHandler.createSOAPSearch(session, object);
			break;
		case "getProjects":
			// Create search object
			// Parameters: type, pattern, field, firstRow, maxRows, options
			options = new String[][] { { "ArrayOfString", "string", "dimtype", "PRJ" } };
			object = new Search("DIM", "*", 0, 1, 100, options);
			responseArray = SoapHandler.createSOAPSearch(session, object);
			ArrayList<Project> projects = new ArrayList<Project>();
			for (int i = 0; i < responseArray.size(); i++) {
				String[] parts = responseArray.get(i).split(",");
				Project p = (Project) SoapHandler.createSOAPXML(session,
						"<type>dimensions</type><office>nla005594</office><dimtype>PRJ</dimtype><code>" + parts[0] + "</code>", "project");
				projects.add(p);
			}
			try {
				ObjectDAO.saveProjects(projects);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "getRelations":
			// Create search object
			// Parameters: type, pattern, field, firstRow, maxRows, options
			options = new String[][] { { "ArrayOfString", "string", "dimtype", "DEB" } };
			object = new Search("DIM", "*", 0, 1, 100, options);
			responseArray = SoapHandler.createSOAPSearch(session, object);
			// Split data from ArrayList
//			for (int i = 0; i < responseArray.size(); i++) {
//				String[] parts = responseArray.get(i).split(",");
//				// firstName and Lastname are identical
//				Employee e = new Employee(parts[1], parts[1], parts[0]);
//				ArrayList<Employee> emp = new ArrayList<Employee>();
//				emp.add(e);
//				try {
//					ObjectDAO.saveEmployees(emp);
//				} catch (SQLException e1) {
//					e1.printStackTrace();
//				}
//			}
			break;
		case "getHourTypes":
			// Create search object
			// Parameters: type, pattern, field, firstRow, maxRows, options
			options = new String[][] { { "ArrayOfString", "string", "office", code } };
			object = new Search("USR", "*", 0, 1, 100, options);
			responseArray = SoapHandler.createSOAPSearch(session, object);
			break;
		}
		String temp = "arrayList:";
		for (int i = 0; i < responseArray.size(); i++) {
			temp += responseArray.get(i) + ", ";

		}
		RequestDispatcher rd = req.getRequestDispatcher("adapter.jsp");
		req.getSession().setAttribute("soap", temp);
		rd.forward(req, resp);
	}
}
