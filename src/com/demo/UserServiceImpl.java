package com.demo;

import com.demo.dao.UserDao;
import com.spring.AutoWried;
import com.spring.Component;
import com.spring.PostConstruct;
import com.spring.Scope;

/**
 * @version 1.0
 * @author: shore
 * @date: 2023/1/3 10:57
 */
@Component
@Scope("singleton")
public class UserServiceImpl implements UserService {

    @AutoWried
    private UserDao userDao;


    @PostConstruct
    public void init(){
        System.out.println("postConstruct注解修饰的方法, 执行啦~~~~~");
    }


    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
