package com.github.weijj0528.example.security.security.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * The type Permission set vo.
 *
 * @author William.Wei
 */
@Data
public class PermissionSetVO {

    @NotEmpty(message = "用户名不能为空")
    private String username;

    @NotEmpty(message = "权限不能为空")
    private Set<String> permissions;

}
