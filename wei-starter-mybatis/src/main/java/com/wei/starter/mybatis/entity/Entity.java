package com.wei.starter.mybatis.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 基础实体定义
 *
 * @param <T> the type parameter
 * @author Weijj0528
 */
public class Entity<T> implements Serializable {

    public static final String ID = "id";

    public static final String TENANT = "tenant";

    public static final String VERSION = "version";

    public static final String DELETED = "deleted";

    public static final String UPDATER = "updater";

    public static final String UTIME = "utime";

    public static final String CREATOR = "creator";

    public static final String CTIME = "ctime";

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private T id;
    /**
     * 租户ID
     */
    private Long tenant;
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
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updater;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date utime;
    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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
        // 不同子类即使 id 相同也不相等，避免跨类型误判
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Entity<?> entity = (Entity<?>) o;
        // id 为 null 时退化为引用相等，避免两个未持久化实体被判为相等
        T id = getId();
        if (id == null || entity.getId() == null) {
            return this == o;
        }
        // 实体相等性仅基于主键 id，可变业务字段不参与比较
        return Objects.equals(id, entity.getId());
    }

    @Override
    public int hashCode() {
        T id = getId();
        // id 为 null 时使用身份哈希，避免所有未持久化实体落入同一桶
        return id == null ? System.identityHashCode(this) : Objects.hashCode(id);
    }
}
