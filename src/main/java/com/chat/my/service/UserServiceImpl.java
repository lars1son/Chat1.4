package com.chat.my.service;

import com.chat.my.dao.RoleDao;
import com.chat.my.dao.UserDao;
import com.chat.my.model.Image;
import com.chat.my.model.Role;

import com.chat.my.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public void save(UserEntity user, Boolean update) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if (!update) {
            Set<Role> roles = new HashSet<>();
            roles.add(roleDao.getOne(1L));
            user.setRoles(roles);
        }
        userDao.save(user);
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
