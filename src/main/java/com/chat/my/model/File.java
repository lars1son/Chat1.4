package com.chat.my.model;

import javax.persistence.*;

/**
 * Created by Артем on 19.09.2017.
 */
@Entity
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "path")
    private String path;

    @OneToOne(mappedBy = "file")
    private DialogEntity dialogEntity;

    public File(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public File() {
    }
}
