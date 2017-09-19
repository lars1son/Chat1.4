package com.chat.my.socket;

import com.chat.beans.Dialog;
import com.chat.beans.Message;
import com.chat.beans.XmlSocketBox;
import com.chat.my.dao.FileDAO;
import com.chat.my.generics.DialogsMap;
import com.chat.my.model.DialogEntity;
import com.chat.my.service.DialogService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.*;

@ServerEndpoint(value = "/room")

public class DialogSocketManager {

    @Autowired
    private DialogService dialogService;

    @OnOpen
    public synchronized void open(Session session) {
        if (getDialog(session).getUser1Obj().getSession() == null) {
            getDialog(session).getUser1Obj().setSession(session);
            System.out.println("Установили сессию для юзера1");

        } else {
            getDialog(session).getUser2Obj().setSession(session);
            System.out.println("Установили сессию для юзера2");

        }System.out.println("Проверка сессий на активность");
        System.out.println(getDialog(session).getUser1Obj().getSession());
        System.out.println(getDialog(session).getUser2Obj().getSession());
        System.out.println(getDialog(session).getUser1Obj().getUsername());
        System.out.println(getDialog(session).getUser2Obj().getUsername());
        String u1 = getDialog(session).getUser1Obj().getUsername();
        String u2 = getDialog(session).getUser2Obj().getUsername();
        if (getDialog(session).getUser1Obj().getSession() != null && getDialog(session).getUser2Obj().getSession() != null) {
            DialogEntity dialogEntity = dialogService.findByUser1User2(u1, u2);


            if (dialogEntity.getFile() != null) {
                FileInputStream fin = null;
                try {
                    fin = new FileInputStream(dialogEntity.getFile().getPath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    ObjectInputStream ois = new ObjectInputStream(fin);
                    try {
                        Dialog dialog = (Dialog) ois.readObject();
                        try {
                            getDialog(session).getUser1Obj().getSession().getBasicRemote().sendText(dialog.dequeToXmlString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            getDialog(session).getUser2Obj().getSession().getBasicRemote().sendText(dialog.dequeToXmlString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    @OnClose
    public void close(Session session) {
        closeQuietly(session);
    }

    @OnError
    public void onError(Throwable error) {
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        Document doc = XmlSocketBox.instanse.parseXmlFromString(message);

        if (session.getRequestParameterMap().get("whos").get(0).equals("onewhocalls")) {
            getDialog(session).getMessages().addFirst(new Message(getDialog(session).getUser1Obj().getUsername(), doc.getElementsByTagName("message").item(0).getAttributes().getNamedItem("textofthemessage").getNodeValue()));
        }
        if (session.getRequestParameterMap().get("whos").get(0).equals("thecallee")) {
            getDialog(session).getMessages().addFirst(new Message(getDialog(session).getUser2Obj().getUsername(), doc.getElementsByTagName("message").item(0).getAttributes().getNamedItem("textofthemessage").getNodeValue()));
        }

        try {
            getDialog(session).getUser1Obj().getSession().getBasicRemote().sendText(getDialog(session).dequeToXmlString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            getDialog(session).getUser2Obj().getSession().getBasicRemote().sendText(getDialog(session).dequeToXmlString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Dialog getDialog(Session session) {
        return DialogsMap.dialogsUserMap.get(session.getRequestParameterMap().get("user1").get(0) + session.getRequestParameterMap().get("user2").get(0));
    }

    public void closeQuietly(Session session) {
        try {
            if (DialogsMap.dialogsUserMap.containsKey(session.getRequestParameterMap().get("user1").get(0) + session.getRequestParameterMap().get("user2").get(0))) {
                DialogEntity dialogEntity = dialogService.findByUser1User2(getDialog(session).getUser1Obj().getUsername(), getDialog(session).getUser2Obj().getUsername());

                String newfilename = "dialog_" + getDialog(session).getUser1Obj().getUsername() + "_" + getDialog(session).getUser2Obj().getUsername() + ".txt";
                String path = "C:\\Users\\Артем.Артем-ПК\\Documents\\Учебники\\Java\\MyChat1.3\\src\\main\\webapp\\resources\\images\\loaded\\" + newfilename;


                File file = new File(path);
                FileOutputStream fout = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fout);
                oos.writeObject(getDialog(session));
                fout.close();
                oos.close();
                if (dialogEntity.getFile() == null) {
                    com.chat.my.model.File newfile = new com.chat.my.model.File(path);
                    dialogEntity.setFile(newfile);
                } else dialogEntity.getFile().setPath(path);
                dialogService.save(dialogEntity);

            }

            getDialog(session).getMessages().addFirst(new Message("SERVER", "Your interlocutor has already left the dialog :("));
        } catch (Exception e) {
            //NOP
        }
        try {
            getDialog(session).getUser1Obj().getSession().getBasicRemote().sendText(getDialog(session).dequeToXmlString());
        } catch (Exception e) {
            //NOP
        }
        try {
            getDialog(session).getUser2Obj().getSession().getBasicRemote().sendText(getDialog(session).dequeToXmlString());
        } catch (Exception e) {
            //NOP
        }
        DialogsMap.dialogsUserMap.remove(session.getRequestParameterMap().get("user1").get(0) + session.getRequestParameterMap().get("user2").get(0));
    }
}

