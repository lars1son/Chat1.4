package com.chat.my.service;


import com.chat.my.model.Image;
import com.chat.my.model.UserEntity;

public interface UserService {


    void save(UserEntity user,Boolean upd);
    UserEntity findByUsername(String username);
}
