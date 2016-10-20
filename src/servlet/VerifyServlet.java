package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.OAuth;

public class VerifyServlet extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String temporaryToken = req.getParameter("oauth_token");
		String temporaryVerifier = req.getParameter("oauth_verifier");
		OAuth oauth = new OAuth();
		String accessToken = oauth.getAccessToken(temporaryToken, temporaryVerifier);
		if (accessToken != null) {
			resp.sendRedirect("https://login.twinfield.com/oauth/login.aspx?oauth_token=" + accessToken);
		}
	}
}
