package com.github.weijj0528.example.base.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.weijj0528.example.base.BaseExampleApplicationTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class ExampleControllerTest extends BaseExampleApplicationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;


    //方法执行前初始化数据

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


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
            Assert.assertEquals(resultObj.getIntValue("code"), 200);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}