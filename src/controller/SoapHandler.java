package controller;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import model.Token;

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

	public static Document createSOAP(String session, String data) {
		// Create SOAP Connection
		SOAPMessage soapResponse = null;
		SOAPConnection soapConnection = null;
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
			soapConnection.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getXmlString(soapResponse);
	}
	//Converts String to XML
	private static Document getXmlString(SOAPMessage soapResponse) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;     
        Document doc = null;
        try{  
        	SOAPEnvelope soapPart = soapResponse.getSOAPPart().getEnvelope();
     		String xmlString = soapPart.getBody().getFirstChild().getFirstChild().getTextContent();
            builder = factory.newDocumentBuilder();  
            doc = builder.parse(new InputSource( new StringReader(xmlString))); 
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return doc;
    }
	//Set a body with parameter read
	private static void setReadBody(SOAPEnvelope envelope, String data) throws SOAPException {
		SOAPBody soapBody = envelope.getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement("ProcessXmlString", "", "http://www.twinfield.com/");
		SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("xmlRequest");
		soapBodyElem1.addTextNode("<![CDATA[<read>" + data + "</read>]]>");
	}
	//Global header
	private static void setHeader(SOAPEnvelope envelope, String session) throws SOAPException {
		envelope.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		envelope.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");

		// SOAP head
		SOAPHeader soapHead = envelope.getHeader();
		SOAPElement soapHeadElem = soapHead.addChildElement("Header", "", "http://www.twinfield.com/");
		SOAPElement soapHeadElem1 = soapHeadElem.addChildElement("SessionID");
		soapHeadElem1.addTextNode(session);
	}
	

	private static void printSOAPResponse(SOAPMessage soapResponse) throws Exception {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		Source sourceContent = soapResponse.getSOAPPart().getContent();
		System.out.print("\nResponse SOAP Message = ");
		StreamResult result = new StreamResult(System.out);
		transformer.transform(sourceContent, result);
	}
}
