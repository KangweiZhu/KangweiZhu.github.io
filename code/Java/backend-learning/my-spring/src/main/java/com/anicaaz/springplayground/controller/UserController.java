package com.anicaaz.springplayground.controller;

import com.anicaaz.springplayground.service.UserService;
import org.myspringframework.annotation.Autowire;
import org.myspringframework.annotation.Controller;

@Controller
public class UserController {

    @Autowire
    UserService userService;

    public void serviceMethod() {
        userService.add();
    }
}
