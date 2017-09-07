package com.chat.my.service;


import com.chat.my.model.User;

public interface UserService {

    void save(User user);

    User findByUsername(String username);
}
