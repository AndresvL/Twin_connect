package controller;

import java.io.IOException;

import javax.xml.soap.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;

import model.Token;

public class SoapHandler {
	
	public static String getSession(Token token) {
		String sessionID = null;
		try {
			// Create SOAP Connection
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();
			// Send SOAP Message to SOAP Server
			String url = "https://login.twinfield.com/webservices/session.asmx?/";
			SOAPMessage soapResponse = soapConnection.call(createSOAPSession(token), url);
			printSOAPResponse(soapResponse);
			//Set session
			SOAPEnvelope soapPart = soapResponse.getSOAPPart().getEnvelope();
			sessionID = soapPart.getHeader().getFirstChild().getFirstChild().getTextContent();
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
		SOAPElement soapBodyElem = soapBody.addChildElement("OAuthLogon","","http://www.twinfield.com/");
		SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("clientToken");
		soapBodyElem1.addTextNode(token.getConsumerToken());
		SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("clientSecret");
		soapBodyElem2.addTextNode(token.getConsumerSecret());
		SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("accessToken");
		soapBodyElem3.addTextNode(token.getAccessToken());
		SOAPElement soapBodyElem4 = soapBodyElem.addChildElement("accessSecret");
		soapBodyElem4.addTextNode(token.getAccessSecret());
		soapMessage.saveChanges();
		/* Print the request message */
		System.out.print("Request SOAP Message SESSION= ");
		soapMessage.writeTo(System.out);
		System.out.println();
		return soapMessage;
	}
	
	public static String getUser(String session){
		try {
			// Create SOAP Connection
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();

			// Send SOAP Message to SOAP Server
			String url = "https://login.twinfield.com/webservices/finder.asmx?/";
			SOAPMessage soapResponse = soapConnection.call(createSOAPUser(session), url);
			printSOAPResponse(soapResponse);
			//get User
//			SOAPEnvelope soapPart = soapResponse.getSOAPPart().getEnvelope();
//			String name = soapPart.getBody().getFirstChild().getFirstChild().getTextContent();
//			System.out.println("getUser test " + name);
			soapConnection.close();
		} catch (Exception e) {
			System.err.println("Error occurred while sending SOAP Request to Server");
			e.printStackTrace();
		}
		return session;
	}

	private static SOAPMessage createSOAPUser(String session) throws SOAPException, IOException {
		MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		SOAPMessage soapMessage = messageFactory.createMessage();
		
		SOAPPart soapPart = soapMessage.getSOAPPart();
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		
		envelope.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		envelope.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
		
		//SOAP head
		SOAPHeader soapHead = envelope.getHeader();
		SOAPElement soapHeadElem = soapHead.addChildElement("Header","","http://www.twinfield.com/");
		SOAPElement soapHeadElem1 = soapHeadElem.addChildElement("SessionID");
		soapHeadElem1.addTextNode(session);
		
		// SOAP Body
		SOAPBody soapBody = envelope.getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement("Search","","http://www.twinfield.com/");
		SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("type");
		soapBodyElem1.addTextNode("USR");
		SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("pattern");
		soapBodyElem2.addTextNode("");
		SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("field");
		soapBodyElem3.addTextNode("0");
		SOAPElement soapBodyElem4 = soapBodyElem.addChildElement("firstRow");
		soapBodyElem4.addTextNode("1");
		SOAPElement soapBodyElem5 = soapBodyElem.addChildElement("maxRows");
		soapBodyElem5.addTextNode("2");
		soapMessage.saveChanges();
		
		/* Print the request message */
		System.out.print("Request SOAP Message USER= ");
		soapMessage.writeTo(System.out);
		System.out.println();

		return soapMessage;
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
