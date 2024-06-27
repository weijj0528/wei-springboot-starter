package com.github.weijj0528.example.base.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.weijj0528.example.base.BaseExampleApplicationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

public class ExampleControllerTest extends BaseExampleApplicationTest {

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Resource
    protected MockMvc mockMvc;


    @Test
    public void hello() {
        try {
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/example/hello"))
                    .andExpect(MockMvcResultMatchers.status().is(200))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            String result = mvcResult.getResponse().getContentAsString();
            JSONObject resultObj = JSON.parseObject(result);
            // 判断接口返回json中success字段是否为true
            Assertions.assertEquals(resultObj.getIntValue("code"), 20000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}