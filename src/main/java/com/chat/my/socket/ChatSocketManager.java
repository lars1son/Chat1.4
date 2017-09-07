package com.chat.my.socket;

import com.chat.beans.Message;
import com.chat.beans.XmlSocketBox;
import com.chat.my.generics.DialogsMap;
import com.chat.my.generics.MessagesDeque;
import com.chat.my.generics.OnlineUsersMap;

import com.chat.my.model.User;
import org.w3c.dom.Document;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.security.Principal;

@ServerEndpoint(value = "/chat")
public class ChatSocketManager {

    @OnOpen
    public void open( Session session) {
        String login = session.getRequestParameterMap().get("username").get(0);
        OnlineUsersMap.onlineUserMap.get(login).setSession(session);
        refreshOnlineUsers();
        MessagesDeque.messagesDeque.addFirst(new Message(String.valueOf(session.getRequestParameterMap().get("username").get(0)), " joined us."));
        refreshMessages();
    }

    @OnClose
    public void close(Session session) {
        MessagesDeque.messagesDeque.addFirst(new Message(session.getRequestParameterMap().get("username").get(0), "left us."));
        OnlineUsersMap.onlineUserMap.remove(session.getRequestParameterMap().get("username").get(0));
        refreshOnlineUsers();
        refreshMessages();
    }

    @OnError
    public void onError(Throwable error){}

    @OnMessage
    public void handleMessage(String message, Session session) {
        Document doc = XmlSocketBox.instanse.parseXmlFromString(message);
        if (!doc.getElementsByTagName("message").item(0).getAttributes().getNamedItem("textofthemessage").getNodeValue().equals("")) {
            messagesHandler(session, doc);
        }
        if (!doc.getElementsByTagName("dialogrequest").item(0).getAttributes().getNamedItem("whos").getNodeValue().equals("")) {
            dialogRequestHandler(session, doc, message);
        }
    }

    private void messagesHandler(Session session, Document doc) {
        if (doc.getElementsByTagName("message").item(0).getAttributes().getNamedItem("logout").getNodeValue().equals("true")) {
            OnlineUsersMap.onlineUserMap.remove(session.getRequestParameterMap().get("login").get(0));
            refreshOnlineUsers();
        } else {
            String textOfTheMessage = doc.getElementsByTagName("message").item(0).getAttributes().getNamedItem("textofthemessage").getNodeValue();
            MessagesDeque.messagesDeque.addFirst(new Message(String.valueOf(session.getRequestParameterMap().get("login").get(0)), textOfTheMessage));
            refreshMessages();
        }
    }

    private void dialogRequestHandler(Session session, Document doc, String message) {
        String oneWhoCalls = doc.getElementsByTagName("dialogrequest").item(0).getAttributes().getNamedItem("onewhocalls").getNodeValue();
        String theCallee = doc.getElementsByTagName("dialogrequest").item(0).getAttributes().getNamedItem("thecallee").getNodeValue();
        if (doc.getElementsByTagName("dialogrequest").item(0).getAttributes().getNamedItem("whos").getNodeValue().equals("onewhocalls")) {
            try {
                OnlineUsersMap.onlineUserMap.get(theCallee).getSession().getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (doc.getElementsByTagName("dialogrequest").item(0).getAttributes().getNamedItem("whos").getNodeValue().equals("thecallee")) {
            if (doc.getElementsByTagName("dialogrequest").item(0).getAttributes().getNamedItem("requestanswer").getNodeValue().equals("true")) {
                try {
                    OnlineUsersMap.onlineUserMap.get(oneWhoCalls).getSession().getBasicRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (doc.getElementsByTagName("dialogrequest").item(0).getAttributes().getNamedItem("requestanswer").getNodeValue().equals("false")) {
                DialogsMap.dialogsUserMap.remove(oneWhoCalls + theCallee);
                try {
                    OnlineUsersMap.onlineUserMap.get(oneWhoCalls).getSession().getBasicRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void refreshMessages() {
        for (String i : OnlineUsersMap.onlineUserMap.keySet()) {
            User temp = OnlineUsersMap.onlineUserMap.get(i);
            try {
                temp.getSession().getBasicRemote().sendText(MessagesDeque.dequeToXmlString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshOnlineUsers() {
        for (String i : OnlineUsersMap.onlineUserMap.keySet()) {
            User temp = OnlineUsersMap.onlineUserMap.get(i);
            try {
                Document doc = XmlSocketBox.instanse.newDocument();
                doc = XmlSocketBox.instanse.createSocketBox(doc);
                doc = XmlSocketBox.instanse.setOnlineUsers(doc, OnlineUsersMap.getOnlineUserLoginsThroughTheSpaceLastSpaceIncluding());
                temp.getSession().getBasicRemote().sendText(XmlSocketBox.instanse.generateStringFromXml(doc));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}