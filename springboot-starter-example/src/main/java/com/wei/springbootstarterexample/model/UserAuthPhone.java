package com.wei.springbootstarterexample.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "user_auth_phone")
public class UserAuthPhone implements Serializable {
    @Id
    private Long id;

    private String phone;

    private String pwd;

    @Column(name = "user_id")
    private Long userId;

    private Date ctime;

    private static final long serialVersionUID = 1L;

    public static final String ID = "id";

    public static final String PHONE = "phone";

    public static final String PWD = "pwd";

    public static final String USER_ID = "userId";

    public static final String CTIME = "ctime";
}