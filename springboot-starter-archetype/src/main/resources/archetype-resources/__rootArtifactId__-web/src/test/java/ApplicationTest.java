#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

/**
 * The type Application test.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTest {

    /**
     * The Web application context.
     */
    @Resource
    protected WebApplicationContext webApplicationContext;

    /**
     * The Mock mvc.
     */
    @Resource
    protected MockMvc mockMvc;

    /**
     * Mvc result mvc result.
     * 测试请求方法
     *
     * @param requestBuilder the request builder
     * @return mvc result
     * @throws Exception the exception
     */
    protected MvcResult mvcResult(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

}
