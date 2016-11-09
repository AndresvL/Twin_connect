package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.OAuth;
import controller.SoapHandler;
import object.Token;

public class OAuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String token = "818784741B7543C7AE95CE5BFB783DF2";
		String secret = "F441FB65B6AA42C995F9FAF3662E8A10";
	
		OAuth oauth = new OAuth();
		Token checkToken = null;
		try {
			checkToken = oauth.getTempToken(token, secret);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (checkToken.getAccessToken() == null) {
			resp.sendRedirect("https://login.twinfield.com/oauth/login.aspx?oauth_token=" + checkToken.getTempToken());
			System.out.println("No AccessToken found in database!");
		}else{
			String sessionID = SoapHandler.getSession(checkToken);
			@SuppressWarnings("unchecked")
			ArrayList<String> offices = (ArrayList<String>) SoapHandler.createSOAPXML(sessionID, "<type>offices</type>", "office");
			System.out.println("AccessToken found in database: sessionID = " + sessionID);
			RequestDispatcher rd = null;
			rd = req.getRequestDispatcher("adapter.jsp");
			req.getSession().setAttribute("session", sessionID);
			req.getSession().setAttribute("offices", offices);
			rd.forward(req, resp);
		}
	}
}
	