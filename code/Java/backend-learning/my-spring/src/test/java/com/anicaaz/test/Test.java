package com.anicaaz.test;

import com.anicaaz.springplayground.controller.UserController;
import com.anicaaz.springplayground.service.UserService;
import com.anicaaz.springplayground.service.impl.UserServiceImpl;
import org.myspringframework.factory.ClassPathXmlApplicationContext;

public class Test {

    @org.junit.jupiter.api.Test
    public void test() {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        //byName
        UserService userServiceImpl = (UserService) classPathXmlApplicationContext.getBeanByValue("aaa");
        userServiceImpl.add();
        //byType
        UserController userController = (UserController) classPathXmlApplicationContext.getBeanByType(UserController.class);
        userController.serviceMethod();
    }
}
