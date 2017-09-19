package com.chat.my.dao;


import com.chat.my.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);

}
