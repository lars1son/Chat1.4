package com.chat.my.model;

import javax.websocket.Session;
import java.io.Serializable;

/**
 * Created by Артем on 07.09.2017.
 */
public class User extends UserEntity implements Comparable , Serializable {

    private boolean isOnline;
    private String sex;
    private int age;

    private Session session;

    public User(String login, String password) {
        this.sex = null;



    }

    public User(User user) {
        this.username=user.getUsername();



    }

    public User() {
    }

    public void createUserFromUserEntity(UserEntity userEntity) {
        this.username = userEntity.getUsername();
        this.password = userEntity.getPassword();
        this.id  =  userEntity.getId();
    }

    public User(String login, String password, boolean isOnline, String sex, int age, String comment, String email) {
        this.sex = sex;
        this.age = age;


    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }


    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }



    public boolean equals(Object obj) {
        User user = (User) obj;
        if (user.getPassword().equals(this.password) && user.getUsername().equals(this.username)) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public int compareTo(Object o) {
        User user = (User) o;
        if (user.getPassword().equals(this.password) && user.getUsername().equals(this.username)) {
            return 0;
        } else if (user.getAge() > this.getAge()) {
            return -1;
        } else {
            return 1;
        }
    }
}

