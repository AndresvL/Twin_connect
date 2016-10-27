package servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.SOAPMessage;

import controller.SoapHandler;

public class ImportDataServlet extends HttpServlet{
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String button = req.getParameter("category");
		String session = (String) req.getSession().getAttribute("session");
		SOAPMessage soap = null;
		if(button.equals("getOffice")){
			soap = SoapHandler.createSOAP(session, "<type>office</type><code>NLA005593</code>");
		}
		if(button.equals("getUser")){
			soap = SoapHandler.createSOAP(session, "<type>user</type><code>COM001699</code>");
		}
		RequestDispatcher rd = req.getRequestDispatcher("adapter.jsp");
		req.getSession().setAttribute("soap", soap);
		rd.forward(req, resp);
	}
}
