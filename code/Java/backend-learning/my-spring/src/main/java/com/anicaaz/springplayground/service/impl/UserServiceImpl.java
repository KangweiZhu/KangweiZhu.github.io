package com.anicaaz.springplayground.service.impl;

import com.anicaaz.springplayground.dao.UserDao;
import com.anicaaz.springplayground.service.UserService;
import org.myspringframework.annotation.Autowire;
import org.myspringframework.annotation.Service;

@Service(value = "aaa")
public class UserServiceImpl implements UserService {

    @Autowire
    UserDao userDao;

    @Override
    public int add() {
        System.out.println("UserService add");
        userDao.addUser();
        return 0;
    }
}
