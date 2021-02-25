package com.github.weijj0528.example.mybatis.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AddArgs {

    /**
     * 是否删除
     */
    private Boolean del;

    /**
     * 更新人
     */
    private String updater;

    /**
     * 最后更新时间
     */
    private Date utime;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 记录时间
     */
    private Date ctime;

    public AddArgs() {
        del = false;
        updater = "update";
        utime = new Date();
        creater = "creater";
        ctime = utime;
    }
}
