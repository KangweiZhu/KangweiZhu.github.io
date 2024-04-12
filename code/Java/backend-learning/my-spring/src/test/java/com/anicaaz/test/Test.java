package com.anicaaz.test;

import org.myspringframework.factory.ClassPathXmlApplicationContext;

public class Test {

    @org.junit.jupiter.api.Test
    public void test() {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
    }
}
