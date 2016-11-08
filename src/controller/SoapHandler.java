package controller;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import object.Material;
import object.Project;
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

	public static Object createSOAPXML(String session, String data, String type) {
		// Create SOAP Connection
		SOAPMessage soapResponse = null;
		SOAPConnection soapConnection = null;
		String xmlString = null;
		Object obj = new Object();
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
		if(type.equals("project")){
			obj = getProjectXML(xmlString);
		}
		if(type.equals("material")){
			obj = getMaterialXML(xmlString);
		}
		return obj;
	}


	// See Finder methode from Twinfield
	public static ArrayList<String> createSOAPSearch(String session, Search object) {
		// Create SOAP Connection
		SOAPMessage soapResponse = null;
		SOAPConnection soapConnection = null;
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
			soapConnection.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return setArrayList(soapResponse);

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

	// Converts String to Project Object
	private static Object getProjectXML(String soapResponse) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Project p = null;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(soapResponse)));
			//<dimension>
			NodeList allData = doc.getChildNodes().item(0).getChildNodes();
				//<code>
				String code = allData.item(2).getTextContent();
				//<uid>
				String code_ext = allData.item(3).getTextContent();
				//<name>
				String name = allData.item(4).getTextContent();
				//<dimension status>
				String status = doc.getChildNodes().item(0).getAttributes().getNamedItem("status").getNodeValue();
				//<projects>
				NodeList projects = doc.getElementsByTagName("projects").item(0).getChildNodes();
					//<invoicedescription>
					String description = projects.item(0).getTextContent();
					//<validfrom>
					String dateStart = projects.item(1).getTextContent();
					//<validfrom>
					String dateEnd = projects.item(2).getTextContent();
					//<customer>
					String debtorNumber = projects.item(4).getTextContent();
					//active
					int active = 0;
					if(status.equals("active")){
						active = 1;
					}
			
			System.out.println("code " + code+" name " + name+" code_ext " + code_ext+" status " + status+" description " + description+" debtor " + debtorNumber+" date_start " + dateStart+" date_end " + dateEnd+" active " + active);
			p = new Project(code, code_ext, debtorNumber, status, name, dateStart, dateStart, description, 0, active);		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}
	
	// Converts String to Material Object
	private static Object getMaterialXML(String soapResponse) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Material m = null;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(soapResponse)));
			//<article>
			NodeList allData = doc.getChildNodes().item(0).getChildNodes();
				//<header>
				NodeList headerData = allData.item(0).getChildNodes();
					//<code>
					String code = headerData.item(1).getTextContent();
				//<lines>
				NodeList lines = allData.item(1).getChildNodes();
				String subcode = null, unit = null, description = null;
				double price = 0d;
				for(int i = 0; i < lines.getLength(); i++){
					//<line>
					NodeList line = lines.item(i).getChildNodes();
					price = Double.parseDouble(line.item(0).getTextContent());
					unit = line.item(2).getTextContent();
					description = line.item(3).getTextContent();
					subcode = line.item(5).getTextContent();
				}					
			System.out.println("code " + code+ " subcode " + subcode+" name " + description+" unit " + unit+" price " + price);
			m = new Material(code, subcode, unit, description, price);		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m;
	}

	public static ArrayList<String> setArrayList(SOAPMessage response) {
		ArrayList<String> allItems = new ArrayList<String>();
		try {
			// Get data from SOAP message
			// <Body><SearchResponse><data>
			Node data = response.getSOAPPart().getEnvelope().getBody().getFirstChild().getLastChild();
			Element element = null;
			if (data.getNodeType() == Node.ELEMENT_NODE) {
				element = (Element) data;
			}

			NodeList allData = element.getChildNodes();
			// <TotalRows>
			int totalRows = Integer.parseInt(allData.item(0).getFirstChild().getTextContent());
			// <Columns>
			NodeList columns = allData.item(1).getChildNodes();
			// <Items>
			NodeList items = allData.item(2).getChildNodes();
			for (int i = 0; i < totalRows; i++) {
				String temp = null;
				// <ArrayOfString>
				NodeList content = items.item(i).getChildNodes();
				for (int j = 0; j < columns.getLength(); j++) {
					// <string>
					if (temp == null) {
						temp = content.item(j).getTextContent() + ",";
					} else if ((j + 1) != columns.getLength()) {
						temp += content.item(j).getTextContent() + ",";
					} else {
						temp += content.item(j).getTextContent();
					}
				}
				allItems.add(temp);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allItems;

	}

	// Converts String to Formatted XML
	// private static String getFormatString(String soapResponse) {
	// DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	// DocumentBuilder builder;
	// String content = null;
	// try {
	// builder = factory.newDocumentBuilder();
	// Document doc = builder.parse(new InputSource(new
	// StringReader(soapResponse)));
	//
	// // Format the string
	// OutputFormat format = new OutputFormat(doc);
	// format.setLineWidth(65);
	// format.setIndenting(true);
	// format.setIndent(2);
	//
	// Writer out = new StringWriter();
	// XMLSerializer serializer = new XMLSerializer(out, format);
	// serializer.serialize(doc);
	// content = out.toString();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return content;
	// }

}
