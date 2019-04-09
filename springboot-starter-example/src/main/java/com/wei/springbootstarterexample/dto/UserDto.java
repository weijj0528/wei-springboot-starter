package com.wei.springbootstarterexample.dto;

import com.wei.springboot.starter.dto.BaseDto;
import com.wei.springbootstarterexample.model.User;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
/**
 * @author
 * @createTime 2019-04-09 10:07:46
 * @description
 */
@Data
public class UserDto extends BaseDto<User> implements Serializable {


    /**
     * 
     */
    private String id;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private Integer age;

}
