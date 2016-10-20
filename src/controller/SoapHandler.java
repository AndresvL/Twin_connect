package controller;

import java.util.Iterator;

import javax.xml.soap.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

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
			SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(token), url);
			
			// Process the SOAP Response
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
	
	private static SOAPMessage createSOAPRequest(Token token) throws Exception {
		MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		SOAPMessage soapMessage = messageFactory.createMessage();
		
		SOAPPart soapPart = soapMessage.getSOAPPart();

		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		
		envelope.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		envelope.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
		/*
        Constructed SOAP Request Message:
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  			<soap:Body>
    			<AccessTokenLogon xmlns="http://www.twinfield.com/">
      				<accessToken>string</accessToken>
    			</AccessTokenLogon>
  			</soap:Body>
		</soap:Envelope>
         */
		// SOAP Body
		SOAPBody soapBody = envelope.getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement("OAuthLogon", "","http://www.twinfield.com/");
		SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("clientToken");
		soapBodyElem1.addTextNode(token.getConsumerKey());
		SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("clientSecret");
		soapBodyElem2.addTextNode(token.getConsumerSecret());
		SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("accessToken");
		soapBodyElem3.addTextNode(token.getAccessKey());
		SOAPElement soapBodyElem4 = soapBodyElem.addChildElement("accessSecret");
		soapBodyElem4.addTextNode(token.getAccessSecret());
		soapMessage.saveChanges();

		/* Print the request message */
		System.out.print("Request SOAP Message = ");
		soapMessage.writeTo(System.out);
		System.out.println();

		return soapMessage;
	}

	/**
	 * Method used to print the SOAP Response
	 */
	private static String printSOAPResponse(SOAPMessage soapResponse) throws Exception {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		Source sourceContent = soapResponse.getSOAPPart().getContent();
		System.out.print("\nResponse SOAP Message = ");
		StreamResult result = new StreamResult(System.out);
		transformer.transform(sourceContent, result);
		return null;
	}
}
