#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import cn.hutool.core.util.IdUtil;
import com.wei.starter.security.Principal;
import com.wei.starter.security.WeiToken;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import javax.servlet.*;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;

/**
 * The type Application test.
 */
@Slf4j
@SpringBootTest(classes = {Application.class, ApplicationTest.TestConfig.class})
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

    @BeforeAll
    public static void testAuth() {
        Principal user = new Principal() {
            @Override
            public String getName() {
                return "Test";
            }

            @Override
            public String getTenant() {
                return BigInteger.ZERO.toString();
            }

            @Override
            public Collection<String> getAuthorities() {
                return Collections.emptyList();
            }
        };
        String token = IdUtil.objectId();
        SecurityContextHolder.getContext().setAuthentication(new WeiToken(token, user));
        log.info("test auth!");
    }

    /**
     * 测试配置类
     */
    @TestConfiguration
    public static class TestConfig {

        /**
         * 测试授权过滤器注册
         *
         * @return the filter registration bean
         */
        @Bean
        public FilterRegistrationBean<TestAuthFilter> testAuthFilterRegistration() {
            FilterRegistrationBean<TestAuthFilter> registration = new FilterRegistrationBean<>();
            registration.setFilter(new TestAuthFilter());
            // 设置过滤器的URL模式
            registration.addUrlPatterns("/*");
            return registration;
        }

    }

    /**
     * 测试授权过滤器
     */
    public static class TestAuthFilter implements Filter {
        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            testAuth();
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
