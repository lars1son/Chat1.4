package com.chat.beans;

import java.io.Serializable;

public class Message implements Serializable{
    private String chatterNick;
    private String message;
    private String time;

    public Message(String nick, String message) {
        this.chatterNick = nick;
        this.message = message;
        this.time = createTime();
    }

    public String getChatterNick() {
        return chatterNick;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    private String createTime() {
        long time = System.currentTimeMillis();
        int hours = (int) (time / 3600000) % 24 + 2; //+2 for Ukraine
        int minutes = (int) (time % 3600000) / 60000;
        int seconds = (int) ((time % 3600000) % 60000) / 1000;
        StringBuilder s = new StringBuilder();
        if (hours < 10) {
            s.append("0" + hours);
        } else {
            s.append(hours);
        }
        s.append(':');
        if (minutes < 10) {
            s.append("0" + minutes);
        } else {
            s.append(minutes);
        }
        s.append(':');
        if (seconds < 10) {
            s.append("0" + seconds);
        } else {
            s.append(seconds);
        }
        String result = s.toString();
        return result;
    }

}
