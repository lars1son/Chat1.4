package com.chat.my.model;

import javax.persistence.*;

/**
 * Created by Артем on 14.09.2017.
 */
@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "path")
    private String path;

    @OneToOne(mappedBy = "image")
    private UserEntity user;

    public Image() {
    }


    public Image(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
