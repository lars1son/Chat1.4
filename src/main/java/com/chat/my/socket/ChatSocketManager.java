package com.chat.my.socket;

import com.chat.beans.Message;
import com.chat.beans.XmlSocketBox;
import com.chat.my.generics.DialogsMap;
import com.chat.my.generics.MessagesDeque;
import com.chat.my.generics.OnlineUsersMap;

import com.chat.my.model.User;
import com.chat.my.model.UserEntity;
import org.apache.taglibs.standard.lang.jstl.test.PageContextImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.w3c.dom.Document;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.security.Principal;

/////WebSocket for common chat page
@ServerEndpoint(value = "/chat")
public class ChatSocketManager {

    @OnOpen
    public void open(Session session) {
        OnlineUsersMap.onlineUserMap.get(session.getUserPrincipal().getName()).setSession(session);
        refreshOnlineUsers();
        MessagesDeque.messagesDeque.addFirst(new Message(String.valueOf(session.getUserPrincipal().getName()), " joined us."));
        refreshMessages();
    }

    @OnClose
    public void close(Session session) {
        MessagesDeque.messagesDeque.addFirst(new Message(session.getUserPrincipal().getName(), "left us."));
        OnlineUsersMap.onlineUserMap.remove(session.getUserPrincipal().getName());
        refreshOnlineUsers();
        refreshMessages();
    }

    @OnError
    public void onError(Throwable error) {
    }
/////Get message in Server
    @OnMessage
    public void handleMessage(String message, Session session) {
        Document doc = XmlSocketBox.instanse.parseXmlFromString(message);
        /////Check if it's message
        if (!doc.getElementsByTagName("message").item(0).getAttributes().getNamedItem("textofthemessage").getNodeValue().equals("")) {
            messagesHandler(session, doc);
        }
        ////check if it's request for dialog
        if (!doc.getElementsByTagName("dialogrequest").item(0).getAttributes().getNamedItem("whos").getNodeValue().equals("")) {
            dialogRequestHandler(session, doc, message);
        }
    }
///// handle message
    private void messagesHandler(Session session, Document doc) {
        ///Message: user's logged out
        if (doc.getElementsByTagName("message").item(0).getAttributes().getNamedItem("logout").getNodeValue().equals("true")) {
            OnlineUsersMap.onlineUserMap.remove(session.getUserPrincipal().getName());
            refreshOnlineUsers();
        }
        //// simple message
        else {

            String textOfTheMessage = doc.getElementsByTagName("message").item(0).getAttributes().getNamedItem("textofthemessage").getNodeValue();
            MessagesDeque.messagesDeque.addFirst(new Message(String.valueOf(session.getUserPrincipal().getName()), textOfTheMessage));
            refreshMessages();
        }
    }
//////method for habdling dialog request
    private void dialogRequestHandler(Session session, Document doc, String message) {
        String oneWhoCalls = doc.getElementsByTagName("dialogrequest").item(0).getAttributes().getNamedItem("onewhocalls").getNodeValue();
        String theCallee = doc.getElementsByTagName("dialogrequest").item(0).getAttributes().getNamedItem("thecallee").getNodeValue();

    //////if the message from one who send dialog request
        if (doc.getElementsByTagName("dialogrequest").item(0).getAttributes().getNamedItem("whos").getNodeValue().equals("onewhocalls")) {
            try {
                OnlineUsersMap.onlineUserMap.get(theCallee).getSession().getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //////if the message from one who answer to dialog request
        if (doc.getElementsByTagName("dialogrequest").item(0).getAttributes().getNamedItem("whos").getNodeValue().equals("thecallee")) {
            ////answer os OK
            if (doc.getElementsByTagName("dialogrequest").item(0).getAttributes().getNamedItem("requestanswer").getNodeValue().equals("true")) {
                try {
                    OnlineUsersMap.onlineUserMap.get(oneWhoCalls).getSession().getBasicRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ///answer is NO
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
///////update massage list
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
///////update user list
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