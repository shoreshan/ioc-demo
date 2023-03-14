package com.demo;

import com.demo.dao.UserDao;
import com.spring.MyApplicationContext;
import com.spring.ScopeElement;

/**
 * @version 1.0
 * @author: shore
 * @date: 2023/1/3 11:13
 */
public class Test {
    public static void main(String[] args) {
        MyApplicationContext myApplicationContext = new MyApplicationContext(AppConfig.class);
        UserServiceImpl userService = (UserServiceImpl) myApplicationContext.getBean("userServiceImpl");
        System.out.println("userService  " + userService.getUserDao());

        System.out.println("!!!!!!!!!!!!!!!!!!");

        UserDao userDao = (UserDao) myApplicationContext.getBean("userDao");
        System.out.println("userDao  " + userDao);
    }
}
