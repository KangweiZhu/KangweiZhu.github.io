package com.anicaaz.springplayground.service.impl;

import com.anicaaz.springplayground.service.UserService;
import org.myspringframework.annotation.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public int add() {
        System.out.println("UserService add");
        return 0;
    }
}
