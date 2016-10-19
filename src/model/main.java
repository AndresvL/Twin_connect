package model;

import java.io.IOException;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import controller.OAuth;

public class main {
	public static void main(String args[]) throws SQLException, ParserConfigurationException, IOException, SAXException {
		String consumer_key = "818784741B7543C7AE95CE5BFB783DF2";
		String consumer_secret = "F441FB65B6AA42C995F9FAF3662E8A10";
		String link = "https://www.werkbonapp.nl/openapi/7/relations/?";
		String token = "b7281cf5256346a5f7d6e5ffef6462d7";
		String softwareToken = "622a8ef3a712344ef07a4427550ae1e2b38e5342";
		OAuth control = new OAuth();
		control.getAccessToken(consumer_key, consumer_secret, "https://login.twinfield.com/oauth/initiate.aspx");
		
	}
}
