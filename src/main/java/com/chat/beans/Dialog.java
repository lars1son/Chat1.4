package com.chat.beans;

import com.chat.my.generics.Util;
import com.chat.my.model.DialogEntity;
import com.chat.my.model.User;

import java.io.Serializable;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Dialog extends DialogEntity implements Serializable {
    private User user1;
    private User user2;
    private ConcurrentLinkedDeque<Message> messages;

    public Dialog(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.messages = new ConcurrentLinkedDeque();
    }

    public User getUser1Obj() {
        return user1;
    }

    public User getUser2Obj() {
        return user2;
    }


    public ConcurrentLinkedDeque<Message> getMessages() {
        return messages;
    }
    public String dequeToXmlString() {
        return XmlSocketBox.instanse.generateStringFromXml(Util.instanse.dequeToDocument(this.messages));
    }

}
