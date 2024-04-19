package com.anicaaz.springplayground.controller;

import com.anicaaz.springplayground.service.UserService;
import org.myspringframework.annotation.Autowire;
import org.myspringframework.annotation.Controller;

@Controller//(value = "aaa")
public class UserController {

    @Autowire (value = "aaa")
    UserService userService; //其实是可以写UserServiceImpl userService的

    public void serviceMethod() {
        userService.add();
    }
}
