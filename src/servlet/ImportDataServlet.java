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
		String session = (String) req.getSession().getAttribute("session");
		Document doc = null;
		if (button.equals("getOffice")) {
			doc = SoapHandler.createSOAP(session, "<type>office</type><code>NLA005593</code>");
		}
		if (button.equals("getUser")) {
			doc = SoapHandler.createSOAP(session, "<type>user</type><code>COM001699</code>");
		}

		OutputFormat format = new OutputFormat(doc);
		format.setLineWidth(65);
		format.setIndenting(true);
		format.setIndent(2);
		Writer out = new StringWriter();
		XMLSerializer serializer = new XMLSerializer(out, format);
		serializer.serialize(doc);
		String content = out.toString();
		System.out.println("content " + content);
		RequestDispatcher rd = req.getRequestDispatcher("adapter.jsp");
		req.getSession().setAttribute("soap", content);
		rd.forward(req, resp);
	}
}
