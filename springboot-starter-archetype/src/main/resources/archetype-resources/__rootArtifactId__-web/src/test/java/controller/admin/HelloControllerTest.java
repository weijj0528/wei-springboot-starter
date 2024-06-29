#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.controller.admin;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.wei.starter.base.bean.Code;
import com.wei.starter.base.bean.Page;
import com.wei.starter.base.bean.Result;
import com.wei.starter.base.util.WeiJsonUtils;
import ${package}.ApplicationTest;
import ${package}.dto.HelloDTO;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


/**
 * (Hello)表控制层
 *
 * @author EasyCode
 * @since 2024-06-26 11:54:31
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HelloControllerTest extends ApplicationTest {

    private Long testId = 1L;

    @Test
    @Order(1)
    public void save() {
        try {
            String api = "/admin/hello";
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(api);
            HelloDTO saveDTO = new HelloDTO();
            saveDTO.setTenant(1L);
            saveDTO.setName("save");
            requestBuilder.contentType(ContentType.APPLICATION_JSON.toString());
            requestBuilder.content(JSON.toJSONString(saveDTO));
            MvcResult mvcResult = mvcResult(requestBuilder);
            String body = mvcResult.getResponse().getContentAsString();
            Result<HelloDTO> result = WeiJsonUtils.parse(body, new TypeReference<Result<HelloDTO>>() {
            });
            testId = result.getData().getId();
            // 判断接口返回json中code字段是否为20000
            Assertions.assertEquals(result.getCode(), Code.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(2)
    public void update() {
        try {
            String api = "/admin/hello/" + testId;
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(api);
            HelloDTO updateDTO = new HelloDTO();
            updateDTO.setName("update");
            requestBuilder.contentType(ContentType.APPLICATION_JSON.toString());
            requestBuilder.content(JSON.toJSONString(updateDTO));
            MvcResult mvcResult = mvcResult(requestBuilder);
            String body = mvcResult.getResponse().getContentAsString();
            Result<HelloDTO> result = WeiJsonUtils.parse(body, new TypeReference<Result<HelloDTO>>() {
            });
            // 判断接口返回json中code字段是否为20000
            Assertions.assertEquals(result.getCode(), Code.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(4)
    public void delete() {
        try {
            String api = "/admin/hello/del/" + testId;
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(api);
            MvcResult mvcResult = mvcResult(requestBuilder);
            String body = mvcResult.getResponse().getContentAsString();
            Result<HelloDTO> result = WeiJsonUtils.parse(body, new TypeReference<Result<HelloDTO>>() {
            });
            // 判断接口返回json中code字段是否为20000
            Assertions.assertEquals(result.getCode(), Code.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(3)
    public void details() {
        try {
            String api = "/admin/hello/" + testId;
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(api);
            MvcResult mvcResult = mvcResult(requestBuilder);
            String body = mvcResult.getResponse().getContentAsString();
            Result<HelloDTO> result = WeiJsonUtils.parse(body, new TypeReference<Result<HelloDTO>>() {
            });
            // 判断接口返回json中code字段是否为20000
            Assertions.assertEquals(result.getCode(), Code.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(5)
    public void page() {
        try {
            String api = "/admin/hello";
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(api);
            // requestBuilder.queryParam("id", "1");
            MvcResult mvcResult = mvcResult(requestBuilder);
            String body = mvcResult.getResponse().getContentAsString();
            Result<Page<HelloDTO>> result = WeiJsonUtils.parse(body, new TypeReference<Result<Page<HelloDTO>>>() {
            });
            // 判断接口返回json中code字段是否为20000
            Assertions.assertEquals(result.getCode(), Code.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
