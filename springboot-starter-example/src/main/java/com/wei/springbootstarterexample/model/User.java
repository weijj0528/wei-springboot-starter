package com.wei.springbootstarterexample.model;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "user")
public class User implements Serializable {
    @Id
    private String id;

    private String name;

    private Integer age;

    private static final long serialVersionUID = 1L;

    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String AGE = "age";
}