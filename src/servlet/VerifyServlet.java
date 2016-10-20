package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.OAuth;
import controller.SoapHandler;
import model.Token;

public class VerifyServlet extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String temporaryToken = req.getParameter("oauth_token");
		String temporaryVerifier = req.getParameter("oauth_verifier");
		OAuth oauth = new OAuth();
		Token token = oauth.getAccessToken(temporaryToken, temporaryVerifier);
		String sessionID = SoapHandler.getSession(token);
//		SoapHandler.getUser(sessionID);
		resp.sendRedirect("http://localhost:8080/Twinfield_connector/");
	}
}
