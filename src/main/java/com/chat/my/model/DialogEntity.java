package com.chat.my.model;

import javax.persistence.*;

/**
 * Created by Артем on 17.09.2017.
 */
@Entity
@Table(name = "dialog_config")
public class DialogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(name = "user1")
    protected String user1;

    @Column(name = "user2")
    protected String user2;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id")
    protected File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public DialogEntity() {
    }

    public DialogEntity(String user1, String user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }
}
