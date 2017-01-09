package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.TokenDAO;
import controller.OAuthTwinfield;
import controller.RestHandler;
import controller.SoapHandler;
import object.Token;

public class OAuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String token = "818784741B7543C7AE95CE5BFB783DF2";
		String secret = "F441FB65B6AA42C995F9FAF3662E8A10";
		String softwareToken = req.getParameter("token");
		OAuthTwinfield oauth = new OAuthTwinfield();
		Token checkToken = null;
		try {
			checkToken = oauth.getTempToken(token, secret, softwareToken);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (RestHandler.checkToken(softwareToken) == 200) {
			req.getSession().setAttribute("error", null);
			if (checkToken.getAccessToken() == null) {
				resp.sendRedirect(
						"https://login.twinfield.com/oauth/login.aspx?oauth_token=" + checkToken.getTempToken());
			} else {
				String sessionID = SoapHandler.getSession(checkToken);
				RequestDispatcher rd = null;
				if(sessionID != null){
					System.out.println("session " +sessionID);
					@SuppressWarnings("unchecked")
					ArrayList<String> offices = (ArrayList<String>) SoapHandler.createSOAPXML(sessionID,
							"<list><type>offices</type></list>", "office");
					
					rd = req.getRequestDispatcher("adapter.jsp");
					req.getSession().setAttribute("session", sessionID);
					req.getSession().setAttribute("softwareToken", softwareToken);
					req.getSession().setAttribute("offices", offices);
					
				}else{
					rd = req.getRequestDispatcher("adapter.jsp");
					try {
						TokenDAO.deleteToken(softwareToken);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					req.getSession().setAttribute("error", "try again");
				}				
				rd.forward(req, resp);
			}
		}else{
			RequestDispatcher rd = null;
			rd = req.getRequestDispatcher("adapter.jsp");
			req.getSession().setAttribute("error", "softwareToken is ongeldig");
			rd.forward(req, resp);
		}
		
	}
}
