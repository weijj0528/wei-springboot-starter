package com.github.weijj0528.example.security.controller;

import com.github.weijj0528.example.security.config.SecurityConfig;
import com.github.weijj0528.example.security.security.UserToken;
import com.github.weijj0528.example.security.security.vo.PermissionSetVO;
import com.wei.starter.base.bean.Result;
import com.wei.starter.security.TokenService;
import com.wei.starter.security.WeiSecurityConfig;
import com.wei.starter.security.WeiSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * The type Security controller.
 *
 * @author William
 * @Date 2019 /3/27
 * @Description 示例控制器
 */
@Slf4j
@RestController
@RequestMapping("/security")
public class SecurityController {

    @Resource
    private TokenService tokenService;
    @Resource
    private WeiSecurityConfig weiSecurityConfig;
    @Resource
    private SecurityConfig securityConfig;

    /**
     * 授权演示接口
     *
     * @param username the username
     * @param password the password
     * @return result
     */
    @PostMapping("/login")
    public Result<String> login(String username, String password) {
        UserToken userToken = new UserToken();
        userToken.setName(username);
        userToken.setAuthorities(Collections.singletonList(weiSecurityConfig.getRolePrefix() + username));
        String token = tokenService.saveToken(userToken, 60L);
        return Result.success(token);
    }

    /**
     * 退出演示接口
     *
     * @return result
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        tokenService.deleteToken("");
        return Result.success();
    }

    /**
     * 动态接口授权演示
     *
     * @param userId the user id
     * @return result
     */
    @GetMapping("/userInfo/{userId}")
    public Result<UserToken> userInfo(@PathVariable String userId) {
        // 获取用户信息
        UserToken userToken = WeiSecurityUtil.getPrincipal();
        return Result.success(userToken);
    }

    /**
     * 角色权限检查成功演示
     *
     * @return result
     */
    @PreAuthorize("hasRole('Admin')")
    //@PreAuthorize("hasAnyAuthority('Admin')")
    //@PreAuthorize("hasAnyAuthority('Admin')")
    @GetMapping("/roleCheckSuccess")
    public Result<UserToken> roleCheckSuccess() {
        // 获取用户信息
        UserToken userToken = WeiSecurityUtil.getPrincipal();
        return Result.success(userToken);
    }

    /**
     * 角色权限检查失败演示
     *
     * @return result
     */
    @PreAuthorize("hasRole('XXXXXX')")
    @GetMapping("/roleCheckFail")
    public Result<UserToken> roleCheckFail() {
        // 获取用户信息
        UserToken userToken = WeiSecurityUtil.getPrincipal();
        return Result.success(userToken);
    }

    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/permission")
    public Result<Void> permissionSet(@RequestBody @Validated PermissionSetVO vo) {
        // 权限设置
        securityConfig.getUserPermission().put(vo.getUsername(), vo.getPermissions());
        return Result.success();
    }

    @PostMapping("/permissionCheck")
    public Result<Void> permissionCheck() {
        // 权限检查
        return Result.success();
    }
}
