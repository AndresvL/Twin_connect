package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.OAuth;

public class OAuthServlet extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String token = req.getParameter("token");
		String secret = req.getParameter("secret");
	
		OAuth oauth = new OAuth();
		String tempToken = oauth.getTempToken(token, secret);
		if (tempToken != null) {
			resp.sendRedirect("https://login.twinfield.com/oauth/login.aspx?oauth_token=" + tempToken);
		}
	}
}
	