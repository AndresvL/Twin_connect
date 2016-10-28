package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.OAuth;
import controller.SoapHandler;
import object.Token;

public class VerifyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String temporaryToken = req.getParameter("oauth_token");
		String temporaryVerifier = req.getParameter("oauth_verifier");
		OAuth oauth = new OAuth();
		Token token = oauth.getAccessToken(temporaryToken, temporaryVerifier);
		String sessionID = SoapHandler.getSession(token);
		RequestDispatcher rd = null;
		rd = req.getRequestDispatcher("adapter.jsp");
		req.getSession().setAttribute("session", sessionID);
		rd.forward(req, resp);
	}
}
