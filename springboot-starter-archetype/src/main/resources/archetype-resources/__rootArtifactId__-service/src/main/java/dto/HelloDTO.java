#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.dto;

import java.util.Date;
import java.io.Serializable;

/**
 * (Hello)实体类
 *
 * @author EasyCode
 * @since 2024-06-26 11:50:53
 */
public class HelloDTO implements Serializable {
    private static final long serialVersionUID = 727550530960573641L;

    private Long id;
    /**
     * 租户ID
     */
    private Integer tenantId;
    /**
     * 手机号
     */
    private String name;
    /**
     * 是否删除
     */
    private Integer isDel;
    /**
     * 更新人
     */
    private String updater;
    /**
     * 更新时间
     */
    private Date utime;
    /**
     * 创建人
     */
    private String creater;
    /**
     * 创建时间
     */
    private Date ctime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
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

}

