package controller;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import object.Relation;
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
		Object obj = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document doc = null;
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
			if (type.equals("office")) {
				setListBody(envelope, data);
			} else {
				setReadBody(envelope, data);
			}
			soapMessage.saveChanges();

			soapResponse = soapConnection.call(soapMessage, url);
			xmlString = soapResponse.getSOAPPart().getEnvelope().getBody().getFirstChild().getFirstChild()
					.getTextContent();
			soapConnection.close();

			// Check if SOAP result is 0 or 1
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(xmlString)));

		} catch (Exception e) {
			e.printStackTrace();
		}
		int result = Integer
				.parseInt(doc.getChildNodes().item(0).getAttributes().getNamedItem("result").getNodeValue());
		if (result != 0) {
			// System.out.println("soapResponse: " + xmlString);
			switch (type) {
			case "project":
				obj = getProjectXML(xmlString, doc);
				break;
			case "material":
				obj = getMaterialXML(xmlString, doc);
				break;
			case "relation":
				obj = getRelationXML(xmlString, doc);
				break;
			case "office":
				obj = getOffices(xmlString, doc);
				break;
			}
		}
		return obj;
	}

	// See Finder methode from Twinfield
	public static ArrayList<String> createSOAPFinder(String session, Search object) {
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

	// Set a body with parameter list
	private static void setListBody(SOAPEnvelope envelope, String data) throws SOAPException {
		SOAPBody soapBody = envelope.getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement("ProcessXmlString", "", "http://www.twinfield.com/");
		SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("xmlRequest");
		soapBodyElem1.addTextNode("<![CDATA[<list>" + data + "</list>]]>");
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
	private static Object getProjectXML(String soapResponse, Document doc) {
		Project p = null;
		// <dimension>
		NodeList allData = doc.getChildNodes().item(0).getChildNodes();
		// <code>
		String code = allData.item(2).getTextContent();
		// <uid>
		String code_ext = allData.item(3).getTextContent();
		// <name>
		String name = allData.item(4).getTextContent();
		// <dimension status>
		String status = doc.getChildNodes().item(0).getAttributes().getNamedItem("status").getNodeValue();
		// <projects>
		NodeList projects = doc.getElementsByTagName("projects").item(0).getChildNodes();
		// <invoicedescription>
		String description = projects.item(0).getTextContent();
		// <validfrom>
		String dateStart = projects.item(1).getTextContent();
		// <validfrom>
		String dateEnd = projects.item(2).getTextContent();
		// <customer>
		String debtorNumber = projects.item(4).getTextContent();
		// active
		int active = 0;
		if (status.equals("active")) {
			active = 1;
		}

		System.out.println("code " + code + " name " + name + " code_ext " + code_ext + " status " + status
				+ " description " + description + " debtor " + debtorNumber + " date_start " + dateStart + " date_end "
				+ dateEnd + " active " + active);
		p = new Project(code, code_ext, debtorNumber, status, name, dateStart, dateStart, description, 0, active);

		return p;
	}

	// Converts String to Material Object
	private static Object getMaterialXML(String soapResponse, Document doc) {
		Material m = null;
		// <article>
		NodeList allData = doc.getChildNodes().item(0).getChildNodes();
		// <header>
		NodeList headerData = allData.item(0).getChildNodes();
		// <code>
		String code = headerData.item(1).getTextContent();
		// <lines>
		NodeList lines = allData.item(1).getChildNodes();
		String subcode = null, unit = null, description = null;
		double price = 0d;
		for (int i = 0; i < lines.getLength(); i++) {
			// <line>
			NodeList line = lines.item(i).getChildNodes();
			price = Double.parseDouble(line.item(0).getTextContent());
			unit = line.item(2).getTextContent();
			description = line.item(3).getTextContent();
			subcode = line.item(5).getTextContent();
			// do something with the subMaterials
		}
		System.out.println(
				"code " + code + " subcode " + subcode + " name " + description + " unit " + unit + " price " + price);
		m = new Material(code, subcode, unit, description, price);

		return m;
	}

	// Converts String to Relation Object
	private static Object getRelationXML(String soapResponse, Document doc) {
		Relation r = null;
		// <dimension>
		NodeList allData = doc.getChildNodes().item(0).getChildNodes();
		// <code>
		String debtorNumber = allData.item(2).getTextContent();
		// <uid>
		// String uid = allData.item(3).getTextContent();
		// <name>
		String name = allData.item(4).getTextContent();
		// <financials>
		NodeList financials = doc.getElementsByTagName("financials").item(0).getChildNodes();
		// <ebillmail>
		String emailWorkorder = financials.item(9).getTextContent();
		// <addresses>
		NodeList addresses = doc.getElementsByTagName("addresses").item(0).getChildNodes();
		String phoneNumber = null, email = null, street = null, houseNumber = null, postalCode = null, city = null,
				remark = null;
		for (int i = 0; i < addresses.getLength(); i++) {
			NodeList address = addresses.item(i).getChildNodes();
			phoneNumber = address.item(4).getTextContent();
			email = address.item(6).getTextContent();
			String streetNumber[] = address.item(9).getTextContent().split("\\s+");
			street = streetNumber[0];
			houseNumber = streetNumber[1];
			postalCode = address.item(3).getTextContent();
			city = address.item(2).getTextContent();
			remark = address.item(8).getTextContent();
			// do something with the different addresses
		}
		r = new Relation(name, debtorNumber, null, phoneNumber, email, emailWorkorder, street, houseNumber, postalCode,
				city, remark);

		return r;
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
			if (totalRows > 0) {
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
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allItems;

	}

	public static ArrayList<Map<String, String>> getOffices(String soapResponse, Document doc) {
		ArrayList<Map<String, String>> offices = new ArrayList<Map<String, String>>();

		// <offices>
		NodeList allData = doc.getChildNodes().item(0).getChildNodes();
		for (int i = 0; i < allData.getLength(); i++) {
			String officeCode = allData.item(i).getTextContent();
			String officeName = allData.item(i).getAttributes().getNamedItem("name").getNodeValue();
			Map<String, String> office = new HashMap<String, String>();
			office.put("code", officeCode);
			office.put("name", officeName);
			offices.add(office);
		}

		return offices;
	}
}
