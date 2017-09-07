package com.chat.beans;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.dom.DOMSource;
import java.io.IOException;
import java.io.StringWriter;

public class XmlSocketBox {

    public static XmlSocketBox instanse = new XmlSocketBox();

    public Document createSocketBox(Document doc) {
        Element socketBox = doc.createElement("socketbox");
        doc.appendChild(socketBox);
        doc = appendMessage(doc);
        doc = appendDialogRequest(doc);
        doc = appendOnlineUsers(doc);
        return doc;
    }

    public Document createSocketBoxForManyMessages(Document doc) {
        Element socketBox = doc.createElement("socketbox");
        doc.appendChild(socketBox);
        doc = appendDialogRequest(doc);
        doc = appendOnlineUsers(doc);
        return doc;
    }

    public Document appendMessage(Document doc) {
        Element message = doc.createElement("message");
        message.setAttribute("login", null);
        message.setAttribute("time", null);
        message.setAttribute("textofthemessage", null);
        doc.getElementsByTagName("socketbox").item(0).appendChild(message);
        return doc;
    }

    public Document appendDialogRequest(Document doc) {
        Element dialogRequest = doc.createElement("dialogrequest");
        dialogRequest.setAttribute("onewhocalls", null);
        dialogRequest.setAttribute("thecallee", null);
        dialogRequest.setAttribute("requestanswer", null);
        dialogRequest.setAttribute("whos", null);
        doc.getElementsByTagName("socketbox").item(0).appendChild(dialogRequest);
        return doc;
    }

    public Document appendOnlineUsers(Document doc) {
        Element onlineUsers = doc.createElement("onlineusers");
        onlineUsers.setAttribute("logins", null);
        doc.getElementsByTagName("socketbox").item(0).appendChild(onlineUsers);
        return doc;
    }

    public Document newDocument() {
        Document docResult = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        docResult = documentBuilder.newDocument();
        return docResult;
    }

    public String generateStringFromXml(Document doc) {
        OutputFormat format = new OutputFormat(doc);
        StringWriter stringOut = new StringWriter();
        XMLSerializer serial = new XMLSerializer(stringOut, format);
        try {
            serial.serialize(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringOut.toString();
    }

    public Document setMessage(Document doc, Message message) {
        doc = setMessage(doc, message, 0);
        return doc;
    }

    public Document setMessage(Document doc, Message message, int index) {
        doc.getElementsByTagName("message").item(index).getAttributes().getNamedItem("login").setTextContent(message.getChatterNick());
        doc.getElementsByTagName("message").item(index).getAttributes().getNamedItem("time").setTextContent(message.getTime());
        doc.getElementsByTagName("message").item(index).getAttributes().getNamedItem("textofthemessage").setTextContent(message.getMessage());
        return doc;
    }

    public Document setDialogRequest(Document doc, DialogRequest dialogRequest) {
        doc.getElementsByTagName("dialogrequest").item(0).getAttributes().getNamedItem("onewhocalls").setTextContent(dialogRequest.getOneWhoCalls());
        doc.getElementsByTagName("dialogrequest").item(0).getAttributes().getNamedItem("thecallee").setTextContent(dialogRequest.getRequestAnswer());
        doc.getElementsByTagName("dialogrequest").item(0).getAttributes().getNamedItem("requestanswer").setTextContent(dialogRequest.getTheCallee());
        doc.getElementsByTagName("dialogrequest").item(0).getAttributes().getNamedItem("whos").setTextContent(dialogRequest.getWhos());
        return doc;
    }

    public Document setOnlineUsers(Document doc, String onlineUsers) {
        doc.getElementsByTagName("onlineusers").item(0).getAttributes().getNamedItem("logins").setTextContent(onlineUsers);
        return doc;
    }

    public Document parseXmlFromString(String xml) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        StringBuilder xmlStringBuilder = new StringBuilder();
        xmlStringBuilder.append(xml);
        ByteArrayInputStream input = null;
        try {
            input = new ByteArrayInputStream(
                    xmlStringBuilder.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc = builder.parse(input);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return doc;
    }

}

