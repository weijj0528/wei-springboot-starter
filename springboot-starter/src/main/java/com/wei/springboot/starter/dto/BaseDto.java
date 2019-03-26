package com.wei.springboot.starter.dto;


import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

/**
 * @author William
 * @Date 2019/3/19
 * @Description 基础数据传输对象
 */
public abstract class BaseDto<Model> {

    private Class<Model> clazz;

    public BaseDto() {
        clazz = (Class<Model>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Model toModel() {
        Model model = null;
        try {
            model = clazz.newInstance();
            BeanUtils.copyProperties(this, model);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return model;
    }

    public <T extends BaseDto> T copyModel(Model model) {
        // 重置对象属性为默认值后Copy
        try {
            Field[] declaredFields = getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                Class<?> type = field.getType();
                if (type == int.class) {
                    field.setInt(this, 0);
                } else if (type == long.class) {
                    field.setLong(this, 0);
                } else if (type == float.class) {
                    field.setFloat(this, 0);
                } else if (type == double.class) {
                    field.setDouble(this, 0);
                } else if (type == char.class) {
                    field.setChar(this, '\u0000');
                } else if (type == byte.class) {
                    field.setByte(this, (byte) 0);
                } else if (type == boolean.class) {
                    field.setBoolean(this, false);
                } else if (type == short.class) {
                    field.setShort(this, (short) 0);
                } else {
                    field.set(this, null);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        BeanUtils.copyProperties(model, this);
        return (T) this;
    }

}
