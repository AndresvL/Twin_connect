package servlet;

import java.io.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.w3c.dom.Document;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import controller.SoapHandler;

public class ImportDataServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String button = req.getParameter("category");
		String code = req.getParameter("code");
		System.out.println("code " + code);	
		String session = (String) req.getSession().getAttribute("session");
		String responseString = null;
		if (button.equals("getOffice")) {
			responseString = SoapHandler.createSOAP(session, "<type>office</type><code>"+code+"</code>");
		}
		if (button.equals("getUser")) {
			responseString = SoapHandler.createSOAP(session, "<type>user</type><code>"+code+"</code>");
		}
		System.out.println("content " + responseString);
		RequestDispatcher rd = req.getRequestDispatcher("adapter.jsp");
		req.getSession().setAttribute("soap", responseString);
		rd.forward(req, resp);
	}
}
