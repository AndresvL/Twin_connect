//package DAO;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//
//import dbUtil.Connect;
//import model.User;
//
//public class UserDAO {
//	private Connection con;
//	private Statement statement;
//	
//	public void safeTrein(ArrayList<User> alleTijden) throws SQLException{
//		Connect connect = new Connect();
//		con = connect.readDatabase();
//		statement = con.createStatement();
//		statement.execute("DELETE FROM User;");
//		for(User t: alleTijden){			
//			statement.execute("INSERT INTO Trein (Spoor, Tijd, RitNummer, Soort, Vervoerder, Bestemming, Vertrek) VALUES ('"+t.getSpoor()+"','"+t.getTijd()+"','"+t.getRit()+"','"+t.getSoort()+"','"+t.getVervoerder()+"','"+t.getEind()+"','"+t.getBegin()+"')");
//		}
//		connect.closeConnection();
//	}
//}
