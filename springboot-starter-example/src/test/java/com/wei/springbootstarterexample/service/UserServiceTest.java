package com.wei.springbootstarterexample.service;

import com.wei.springbootstarterexample.SpringbootStarterExampleApplicationTests;
import com.wei.springbootstarterexample.model.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class UserServiceTest extends SpringbootStarterExampleApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    public void Test(){
        List<User> users = userService.selectAll();
        System.out.println(users);
    }

}