#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model;

import java.util.Date;
import java.io.Serializable;
import javax.persistence.*;

/**
 * (Hello)实体类
 *
 * @author EasyCode
 * @since 2024-06-26 11:50:49
 */
@Table(name = "HELLO")
public class Hello implements Serializable {
    private static final long serialVersionUID = -99040535432383690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 租户ID
     */
    @Column(name = "TENANT_ID")
    private Integer tenantId;
    /**
     * 手机号
     */
    @Column(name = "NAME")
    private String name;
    /**
     * 是否删除
     */
    @Column(name = "IS_DEL")
    private Integer isDel;
    /**
     * 更新人
     */
    @Column(name = "UPDATER")
    private String updater;
    /**
     * 更新时间
     */
    @Column(name = "UTIME")
    private Date utime;
    /**
     * 创建人
     */
    @Column(name = "CREATER")
    private String creater;
    /**
     * 创建时间
     */
    @Column(name = "CTIME")
    private Date ctime;

    public static final String ID = "id";

    public static final String DB_ID = "ID";

    public static final String TENANT_ID = "tenantId";

    public static final String DB_TENANT_ID = "TENANT_ID";

    public static final String NAME = "name";

    public static final String DB_NAME = "NAME";

    public static final String IS_DEL = "isDel";

    public static final String DB_IS_DEL = "IS_DEL";

    public static final String UPDATER = "updater";

    public static final String DB_UPDATER = "UPDATER";

    public static final String UTIME = "utime";

    public static final String DB_UTIME = "UTIME";

    public static final String CREATER = "creater";

    public static final String DB_CREATER = "CREATER";

    public static final String CTIME = "ctime";

    public static final String DB_CTIME = "CTIME";


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
