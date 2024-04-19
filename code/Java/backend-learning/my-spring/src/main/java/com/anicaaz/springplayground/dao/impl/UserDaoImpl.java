package com.anicaaz.springplayground.dao.impl;

import com.anicaaz.springplayground.dao.UserDao;
import org.myspringframework.annotation.Repository;

@Repository
public class UserDaoImpl implements UserDao {
    @Override
    public void addUser() {
        System.out.println("Dao: user add" );
    }
}
