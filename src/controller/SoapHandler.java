package controller;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import object.Search;
import object.Token;

public class SoapHandler {
	private static String cluster;

	public static String getSession(Token token) {
		String sessionID = null;
		try {
			// Create SOAP Connection
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();
			// Send SOAP Message to SOAP Server
			String url = "https://login.twinfield.com/webservices/session.asmx?/";
			SOAPMessage soapResponse = soapConnection.call(createSOAPSession(token), url);
			// Set session
			SOAPEnvelope soapPart = soapResponse.getSOAPPart().getEnvelope();
			sessionID = soapPart.getHeader().getFirstChild().getFirstChild().getTextContent();
			cluster = soapPart.getBody().getFirstChild().getLastChild().getTextContent();
			soapConnection.close();
		} catch (Exception e) {
			System.err.println("Error occurred while sending SOAP Request to Server");
			e.printStackTrace();
		}
		return sessionID;
	}

	private static SOAPMessage createSOAPSession(Token token) throws Exception {
		MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		SOAPMessage soapMessage = messageFactory.createMessage();

		SOAPPart soapPart = soapMessage.getSOAPPart();

		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();

		envelope.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		envelope.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");

		// SOAP Body
		SOAPBody soapBody = envelope.getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement("OAuthLogon", "", "http://www.twinfield.com/");
		SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("clientToken");
		soapBodyElem1.addTextNode(token.getConsumerToken());
		SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("clientSecret");
		soapBodyElem2.addTextNode(token.getConsumerSecret());
		SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("accessToken");
		soapBodyElem3.addTextNode(token.getAccessToken());
		SOAPElement soapBodyElem4 = soapBodyElem.addChildElement("accessSecret");
		soapBodyElem4.addTextNode(token.getAccessSecret());
		soapMessage.saveChanges();

		return soapMessage;
	}

	public static String createSOAPXML(String session, String data) {
		// Create SOAP Connection
		SOAPMessage soapResponse = null;
		SOAPConnection soapConnection = null;
		String xmlString = null;
		try {
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			soapConnection = soapConnectionFactory.createConnection();
			// Send SOAP Message to SOAP Server
			String url = cluster + "/webservices/processxml.asmx?wsdl";
			MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
			SOAPMessage soapMessage = messageFactory.createMessage();
			SOAPPart soapPart = soapMessage.getSOAPPart();

			// SOAP Envelope
			SOAPEnvelope envelope = soapPart.getEnvelope();

			// SOAP Header
			setHeader(envelope, session);

			// SOAP Body
			setReadBody(envelope, data);

			soapMessage.saveChanges();

			soapResponse = soapConnection.call(soapMessage, url);
			xmlString = soapResponse.getSOAPPart().getEnvelope().getBody().getFirstChild().getFirstChild()
					.getTextContent();
			soapConnection.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Returns a formatted XML string

		return getFormatString(xmlString);
	}

	public static SOAPMessage createSOAPSearch(String session, Search object) {
		// Create SOAP Connection
		SOAPMessage soapResponse = null;
		SOAPConnection soapConnection = null;
		String soapString = null;
		ArrayList<String> allData = null;
		try {
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			soapConnection = soapConnectionFactory.createConnection();
			// Send SOAP Message to SOAP Server
			String url = cluster + "/webservices/finder.asmx?wsdl";
			MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
			SOAPMessage soapMessage = messageFactory.createMessage();
			SOAPPart soapPart = soapMessage.getSOAPPart();

			// SOAP Envelope
			SOAPEnvelope envelope = soapPart.getEnvelope();

			// SOAP Header
			setHeader(envelope, session);

			// SOAP Body
			setFinderBody(envelope, object);

			soapMessage.saveChanges();

			soapResponse = soapConnection.call(soapMessage, url);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			soapResponse.writeTo(out);
			soapString = new String(out.toByteArray());
			soapConnection.close();
			
			allData = new ArrayList<String>();
			//Get data from SOAP message
			//<Body><SearchResponse><data>
			Node data = soapResponse.getSOAPPart().getEnvelope().getBody().getFirstChild().getLastChild();
			Element element = null;
			if(data.getNodeType() == Node.ELEMENT_NODE){
				element = (Element)data;
			}
			//<TotalRows>
			String totalRows = data.getFirstChild().getTextContent();
			System.out.println("rows " + totalRows);
//			<Columns>
			NodeList columns = element.getElementsByTagName("columns");
			for(int i = 1; i < columns.getLength(); i++){
				allData.add(columns.item(i).getTextContent());
				if(allData != null){
					String temp = allData.get(0);
					allData.set(0, temp + "," +columns.item(i).getTextContent());
				}
				System.out.println("alldata " + allData.get(i));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return soapResponse;

	}

	private static void setFinderBody(SOAPEnvelope envelope, Search object) throws SOAPException {
		// SOAP Body
		SOAPBody soapBody = envelope.getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement("Search", "", "http://www.twinfield.com/");
		SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("type");
		soapBodyElem1.addTextNode(object.getType());
		SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("pattern");
		soapBodyElem2.addTextNode(object.getPattern());
		SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("field");
		soapBodyElem3.addTextNode("" + object.getField());
		SOAPElement soapBodyElem4 = soapBodyElem.addChildElement("firstRow");
		soapBodyElem4.addTextNode("" + object.getFirstRow());
		SOAPElement soapBodyElem5 = soapBodyElem.addChildElement("maxRows");
		soapBodyElem5.addTextNode("" + object.getMaxRows());
		SOAPElement soapBodyElem6 = soapBodyElem.addChildElement("options");
		String[][] options = object.getOptions();
		if (object.getOptions() != null) {
			for (int i = 0; i < options.length; i++) {
				SOAPElement soapBodyElem7 = soapBodyElem6.addChildElement(options[i][0]);
				for (int j = 2; j < options[i].length; j++) {
					SOAPElement soapBodyElem8 = soapBodyElem7.addChildElement(options[i][1]);
					soapBodyElem8.addTextNode(options[i][j]);
				}
			}
		}
	}

	// Set a body with parameter read
	private static void setReadBody(SOAPEnvelope envelope, String data) throws SOAPException {
		SOAPBody soapBody = envelope.getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement("ProcessXmlString", "", "http://www.twinfield.com/");
		SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("xmlRequest");
		soapBodyElem1.addTextNode("<![CDATA[<read>" + data + "</read>]]>");
	}

	// Global header
	private static void setHeader(SOAPEnvelope envelope, String session) throws SOAPException {
		envelope.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		envelope.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");

		// SOAP head
		SOAPHeader soapHead = envelope.getHeader();
		SOAPElement soapHeadElem = soapHead.addChildElement("Header", "", "http://www.twinfield.com/");
		SOAPElement soapHeadElem1 = soapHeadElem.addChildElement("SessionID");
		soapHeadElem1.addTextNode(session);
	}

	// Converts String to XML
	private static String getFormatString(String soapResponse) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		String content = null;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(soapResponse)));

			// Format the string
			OutputFormat format = new OutputFormat(doc);
			format.setLineWidth(65);
			format.setIndenting(true);
			format.setIndent(2);

			Writer out = new StringWriter();
			XMLSerializer serializer = new XMLSerializer(out, format);
			serializer.serialize(doc);
			content = out.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

}
