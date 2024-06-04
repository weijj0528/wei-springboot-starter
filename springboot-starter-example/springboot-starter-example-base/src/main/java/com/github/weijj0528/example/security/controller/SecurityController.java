package com.github.weijj0528.example.security.controller;

import cn.hutool.core.date.DateUtil;
import com.github.weijj0528.example.security.security.UserToken;
import com.wei.starter.base.bean.Result;
import com.wei.starter.security.TokenService;
import com.wei.starter.security.WeiSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author William
 * @Date 2019/3/27
 * @Description 示例控制器
 */
@Slf4j
@RestController
@RequestMapping("/security")
public class SecurityController {

    @Resource
    private TokenService tokenService;

    @PostMapping("/login")
    public Result<String> login(String username, String password) {
        UserToken userToken = new UserToken();
        userToken.setName("TestUser" + DateUtil.now());
        userToken.setAuthorities(Arrays.asList("Admin", "Tenant", "Customer"));
        String token = tokenService.saveToken(userToken, 60L);
        return Result.success(token);
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        tokenService.deleteToken("");
        return Result.success();
    }

    @GetMapping("/userInfo/{userId}")
    public Result<UserToken> userInfo(@PathVariable String userId) {
        // 获取用户信息
        UserToken userToken = WeiSecurityUtil.getPrincipal();
        return Result.success(userToken);
    }
}
