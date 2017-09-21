package com.chat.my.socket;

import com.chat.beans.Dialog;
import com.chat.beans.Message;
import com.chat.beans.XmlSocketBox;
import com.chat.my.dao.FileDAO;
import com.chat.my.generics.DialogsMap;
import com.chat.my.model.DialogEntity;
import com.chat.my.service.DialogService;
import com.chat.my.service.UserService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.SpringConfigurator;
import org.w3c.dom.Document;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.*;
///////WebSocket for dialog
@ServerEndpoint(value = "/room", configurator = SpringConfigurator.class)
@Component
public class DialogSocketManager implements Serializable {
    private static final String relative_path="C:\\Users\\Артем.Артем-ПК\\Documents\\Учебники\\Java\\MyChat1.3\\src\\main\\webapp\\resources\\images\\loaded\\";

    private DialogService dialogService;


    @Autowired
    public DialogSocketManager(DialogService dialogService) {

        this.dialogService = dialogService;
    }

    public DialogSocketManager() {
    }

    @OnOpen
    public synchronized void open(Session session) {
        if (getDialog(session).getUser1Obj().getSession() == null) {
            getDialog(session).getUser1Obj().setSession(session);

        } else {
            getDialog(session).getUser2Obj().setSession(session);

        }
        String u1 = getDialog(session).getUser1Obj().getUsername();
        String u2 = getDialog(session).getUser2Obj().getUsername();

/////load the dialog history if it's exist
        if (getDialog(session).getUser1Obj().getSession() != null && getDialog(session).getUser2Obj().getSession() != null) {
            try {
                DialogEntity dialogEntity = dialogService.findByUser1User2(u1, u2);
                if (dialogEntity.getFile() != null) {
                    readHistoryFromFile(session, dialogEntity);
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

            }
            catch (Exception e){
                e.printStackTrace();
            }



        }
    }

    public void readHistoryFromFile(Session session, DialogEntity dialogEntity) {
        try {
            FileInputStream fis = new FileInputStream(dialogEntity.getFile().getPath());
            ObjectInputStream oin = new ObjectInputStream(fis);
            while (true) {
                try {
                    getDialog(session).getMessages().addLast((Message) oin.readObject());
                } catch (EOFException e) {
                    break;
                }
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClose
    public void close(Session session, CloseReason closeReason) {
        System.out.println("CloseReason: "+closeReason.getReasonPhrase());
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

//////save new history to data base
    private void updateDialogHisttory(Session session) {
        DialogEntity dialogEntity = dialogService.findByUser1User2(getDialog(session).getUser1Obj().getUsername(), getDialog(session).getUser2Obj().getUsername());

        String newfilename = "dialog_" + dialogEntity.getId() + ".txt";
        String path = relative_path + newfilename;
        try {
            File file = new File(path);
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream serial = new ObjectOutputStream(fos);
            for (Message message : getDialog(session).getMessages()) {
                serial.writeObject(message);
            }
            serial.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
        com.chat.my.model.File newfile = new com.chat.my.model.File(path);

        if (dialogEntity.getFile() == null) {
            dialogEntity.setFile(newfile);
        } else
            dialogEntity.getFile().setPath(newfile.getPath());
        dialogService.save(dialogEntity);

    }

    public void closeQuietly(Session session) {
        try {
            if (DialogsMap.dialogsUserMap.containsKey(session.getRequestParameterMap().get("user1").get(0) + session.getRequestParameterMap().get("user2").get(0))) {
                updateDialogHisttory(session);
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

