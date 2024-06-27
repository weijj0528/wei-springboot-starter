#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.controller.admin;

import com.alibaba.fastjson.JSON;
import com.wei.starter.base.bean.CodeEnum;
import com.wei.starter.base.bean.Result;
import com.weijj0528.mall.ApplicationTest;
import com.weijj0528.mall.dto.HelloDTO;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;


/**
 * (Hello)表控制层
 *
 * @author EasyCode
 * @since 2024-06-26 11:54:31
 */
public class HelloControllerTest extends ApplicationTest {

    @Test
    public void save() {
        try {
            String api = "/admin/hello";
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(api);
            HelloDTO saveDTO = new HelloDTO();
            saveDTO.setTenantId(1);
            saveDTO.setName("add");
            saveDTO.setCreater("Test");
            saveDTO.setCtime(new Date());
            requestBuilder.contentType(ContentType.APPLICATION_JSON.toString());
            requestBuilder.content(JSON.toJSONString(saveDTO));
            MvcResult mvcResult = mvcResult(requestBuilder);
            String result = mvcResult.getResponse().getContentAsString();
            Result<?> result1 = JSON.parseObject(result, Result.class);
            // 判断接口返回json中code字段是否为20000
            Assertions.assertEquals(result1.getCode(), CodeEnum.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void update() {
        try {
            String api = "/admin/hello/1";
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(api);
            HelloDTO updateDTO = new HelloDTO();
            updateDTO.setName("update");
            requestBuilder.contentType(ContentType.APPLICATION_JSON.toString());
            requestBuilder.content(JSON.toJSONString(updateDTO));
            MvcResult mvcResult = mvcResult(requestBuilder);
            String result = mvcResult.getResponse().getContentAsString();
            Result<?> result1 = JSON.parseObject(result, Result.class);
            // 判断接口返回json中code字段是否为20000
            Assertions.assertEquals(result1.getCode(), CodeEnum.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void delete() {
        try {
            String api = "/admin/hello/del/1";
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(api);
            MvcResult mvcResult = mvcResult(requestBuilder);
            String result = mvcResult.getResponse().getContentAsString();
            Result<?> result1 = JSON.parseObject(result, Result.class);
            // 判断接口返回json中code字段是否为20000
            Assertions.assertEquals(result1.getCode(), CodeEnum.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void details() {
        try {
            String api = "/admin/hello/1";
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(api);
            MvcResult mvcResult = mvcResult(requestBuilder);
            String result = mvcResult.getResponse().getContentAsString();
            Result<?> result1 = JSON.parseObject(result, Result.class);
            // 判断接口返回json中code字段是否为20000
            Assertions.assertEquals(result1.getCode(), CodeEnum.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void page() {
        try {
            String api = "/admin/hello";
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(api);
            requestBuilder.queryParam("id", "1");
            MvcResult mvcResult = mvcResult(requestBuilder);
            String result = mvcResult.getResponse().getContentAsString();
            Result<?> result1 = JSON.parseObject(result, Result.class);
            // 判断接口返回json中code字段是否为20000
            Assertions.assertEquals(result1.getCode(), CodeEnum.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
