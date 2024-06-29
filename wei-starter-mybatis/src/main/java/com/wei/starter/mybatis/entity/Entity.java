package com.wei.starter.mybatis.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体定义
 *
 * @param <T> the type parameter
 * @author Weijj0528
 */
public class Entity<T> implements Serializable {

    public static final String ID = "id";

    public static final String TENANT = "tenant";

    public static final String NAME = "name";

    public static final String VERSION = "version";

    public static final String DELETED = "deleted";

    public static final String UPDATER = "updater";

    public static final String UTIME = "utime";

    public static final String CREATER = "creater";

    public static final String CTIME = "ctime";

    /**
     * 主键ID
     */
    @TableId
    private T id;
    /**
     * 租户ID
     */
    private Long tenant;
    /**
     * 名称
     */
    private String name;
    /**
     * 版本
     */
    @Version
    @TableField(fill = FieldFill.INSERT)
    private Long version;
    /**
     * 是否删除
     */
    @TableLogic(value = "0", delval = "1")
    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted;
    /**
     * 更新人
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updater;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date utime;
    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String creater;
    /**
     * 创建时间
     */
    @OrderBy
    @TableField(fill = FieldFill.INSERT)
    private Date ctime;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public Date getUtime() {
        return utime;
    }

    public void setUtime(Date utime) {
        this.utime = utime;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Entity)) {
            return false;
        }
        Entity<?> entity = (Entity<?>) o;
        return Objects.equal(getId(), entity.getId()) && Objects.equal(getTenant(), entity.getTenant()) && Objects.equal(getName(), entity.getName()) && Objects.equal(getVersion(), entity.getVersion()) && Objects.equal(getDeleted(), entity.getDeleted()) && Objects.equal(getUpdater(), entity.getUpdater()) && Objects.equal(getUtime(), entity.getUtime()) && Objects.equal(getCreater(), entity.getCreater()) && Objects.equal(getCtime(), entity.getCtime());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getTenant(), getName(), getVersion(), getDeleted(), getUpdater(), getUtime(), getCreater(), getCtime());
    }
}
