package com.demo.dao;

import com.spring.Component;
import com.spring.Scope;

/**
 * @version 1.0
 * @author: shore
 * @date: 2023/1/3 16:07
 */
@Component
@Scope("singleton")
public class UserDao {

    public UserDao() {
        System.out.println("userDao 被创建了。。。。");
    }
}
