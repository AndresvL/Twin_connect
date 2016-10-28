package servlet;

import java.io.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.xml.soap.SOAPMessage;

import controller.SoapHandler;
import object.Search;

public class ImportDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String button = req.getParameter("category");
		String code = req.getParameter("code");
		String session = (String) req.getSession().getAttribute("session");
		SOAPMessage response = null;
		String[][] options = null;
		Search object;

		switch (button) {

		case "getEmployees":
			// Create search object
			// Parameters: type, pattern, field, firstRow, maxRows, options
			options = new String[][] { { "ArrayOfString", "string", "office", code } };
			object = new Search("USR", "*", 0, 1, 100, options);
			response = SoapHandler.createSOAPSearch(session, object);
		case "getMaterials":
			// Create search object
			// Parameters: type, pattern, field, firstRow, maxRows, options
//			options = new String[][] { { "ArrayOfString", "string", "office", code } };
			object = new Search("ART", "*", 0, 1, 100, options);
			response = SoapHandler.createSOAPSearch(session, object);
		case "getProjects":
			// Create search object
			// Parameters: type, pattern, field, firstRow, maxRows, options
			options = new String[][] { { "ArrayOfString", "string", "office", code } };
			object = new Search("PRJ", "*", 0, 1, 100, options);
			response = SoapHandler.createSOAPSearch(session, object);
		case "getRelations":
			// Create search object
			// Parameters: type, pattern, field, firstRow, maxRows, options
			options = new String[][] { { "ArrayOfString", "string", "dimtype", "DEB" } };
			object = new Search("DIM", "*", 0, 1, 100, options);
			response = SoapHandler.createSOAPSearch(session, object);
		case "getHourTypes":
			// Create search object
			// Parameters: type, pattern, field, firstRow, maxRows, options
			options = new String[][] { { "ArrayOfString", "string", "office", code } };
			object = new Search("USR", "*", 0, 1, 100, options);
			response = SoapHandler.createSOAPSearch(session, object);
		}

		// Print SOAP response on screen
//		System.out.println("content " + responseString);
//		RequestDispatcher rd = req.getRequestDispatcher("adapter.jsp");
//		req.getSession().setAttribute("soap", responseString);
//		rd.forward(req, resp);
	}
}
