package servlet;

import java.io.IOException;
import java.sql.SQLException;

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
		String token = req.getParameter("token");
		String secret = req.getParameter("secret");
	
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
			System.out.println("AccessToken found in database + sessionID=" + sessionID);
			RequestDispatcher rd = null;
			rd = req.getRequestDispatcher("adapter.jsp");
			req.getSession().setAttribute("session", sessionID);
			rd.forward(req, resp);
		}
	}
}
	