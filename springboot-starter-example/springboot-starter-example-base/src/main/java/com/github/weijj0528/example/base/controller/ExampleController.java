package com.github.weijj0528.example.base.controller;

import com.github.weijj0528.example.base.dto.QueryDto;
import com.wei.starter.base.bean.CodeEnum;
import com.wei.starter.base.bean.Result;
import com.wei.starter.base.exception.ErrorMsgException;
import com.wei.starter.base.util.WeiWebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author William
 * @Date 2019/3/27
 * @Description 示例控制器
 */
@Slf4j
@RestController
@RequestMapping("/example")
public class ExampleController {

    /**
     * Hello world!
     *
     * @param query
     * @return
     */
    @GetMapping("/hello")
    public Result<QueryDto> hello(QueryDto query) {
        Result<QueryDto> resultBean = new Result<>();
        resultBean.setCode("20000");
        resultBean.setMsg("Hello world!");
        resultBean.setData(query);
        return resultBean;
    }

    /**
     * 异常演示
     *
     * @param request
     * @param query
     * @return
     */
    @GetMapping("/exception")
    public Result<Void> exception(HttpServletRequest request, QueryDto query) {
        String clientIp = WeiWebUtils.getClientIp(request);
        log.info("clientIp:{}", clientIp);
        throw new ErrorMsgException(CodeEnum.ERROR_SERVER.getMsg());
    }
}
