package com.github.weijj0528.example.base.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.weijj0528.example.base.BaseExampleApplicationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * example-base 集成测试：用 MockMvc 验证统一响应封装、全局异常处理与安全过滤，
 * 不依赖外部服务（Redis/MySQL）。
 *
 * @author William
 */
public class ExampleControllerTest extends BaseExampleApplicationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Resource
    protected MockMvc mockMvcResource;

    @Test
    @DisplayName("开放接口 /example/hello 返回成功封装 code=20000")
    public void hello_returnsSuccessEnvelope() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/example/hello"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        JSONObject resultObj = JSON.parseObject(mvcResult.getResponse().getContentAsString());
        // Result 成功码约定为 20000
        assertEquals(20000, resultObj.getIntValue("code"), "开放接口应返回成功码 20000");
        assertNotNull(resultObj.getString("msg"), "msg 不应为空");
    }

    @Test
    @DisplayName("受保护接口 /example/exception 未携带 Token 时返回 401")
    public void exception_withoutToken_returns401() throws Exception {
        // /example/exception 不在 open-apis 中，未认证应被安全过滤器拦截为 401
        mockMvc.perform(MockMvcRequestBuilders.get("/example/exception"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
