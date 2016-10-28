package servlet;

import java.io.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import controller.SoapHandler;
import object.Search;

public class ImportDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String button = req.getParameter("category");
		String code = req.getParameter("code");
		String session = (String) req.getSession().getAttribute("session");
		String responseString = null;
		
		switch(button){
			case "getOffice":
				responseString = SoapHandler.createSOAPXML(session, "<type>office</type><code>"+code+"</code>");
				break;
			case "getUser" :
				responseString = SoapHandler.createSOAPXML(session, "<type>user</type><code>"+code+"</code>");
				break;
			case "getEmployees" : 
				//Create search object
				//Parameters: type, pattern, field, firstRow, maxRows, options
				String[][] options = {{"ArrayOfString","string","office", code}};
				Search object = new Search("USR", "*", 0, 1, 100, options);
				responseString = SoapHandler.createSOAPSearch(session, object);
		}	
		
		
		//Print SOAP response on screen
		System.out.println("content " + responseString);
		RequestDispatcher rd = req.getRequestDispatcher("adapter.jsp");
		req.getSession().setAttribute("soap", responseString);
		rd.forward(req, resp);
	}
}
