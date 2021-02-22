package com.github.weijj0528.example.cache.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserInfo implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 编号
     */
    private String no;

    /**
     * 会员名
     */
    private String name;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别 0/1 女/男
     */
    private Integer gender;

    /**
     * 手机
     */
    private String phone;

    /**
     * qq号
     */
    private String qq;

    /**
     * 微信号
     */
    private String wechat;

    /**
     * 会员积分
     */
    private Integer point;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 用户生日
     */
    private Date birthday;

    /**
     * 省
     */
    private Integer province;

    /**
     * 市
     */
    private Integer city;

    /**
     * 来源 1/2/3/4 pc/android/weixin/ios
     */
    private Integer source;

    /**
     * 实名
     */
    private String fullname;

    /**
     * 身份证号码
     */
    private String idnumber;

    /**
     * 审核状态 0/1/2/3 初始/审核中/审核成功/审核失败
     */
    private Integer audit;

    /**
     * 审核备注
     */
    private String auditComment;

    /**
     * 是否有签到，每天3点重置
     */
    private Integer isSign;

    /**
     * 冻结0/1 正常/冻结
     */
    private Integer freeze;

    /**
     * 最后登录IP
     */
    private String lastLoginIp;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;

    /**
     * 状态 0/1 有效/删除
     */
    private Integer isDel;

    /**
     * 更新时间
     */
    private Date utime;

    /**
     * 创建时间
     */
    private Date ctime;

    private static final long serialVersionUID = 1L;

    public static final String ID = "id";

    public static final String NO = "no";

    public static final String NAME = "name";

    public static final String NICKNAME = "nickname";

    public static final String GENDER = "gender";

    public static final String PHONE = "phone";

    public static final String QQ = "qq";

    public static final String WECHAT = "wechat";

    public static final String POINT = "point";

    public static final String AVATAR = "avatar";

    public static final String SIGNATURE = "signature";

    public static final String BIRTHDAY = "birthday";

    public static final String PROVINCE = "province";

    public static final String CITY = "city";

    public static final String SOURCE = "source";

    public static final String FULLNAME = "fullname";

    public static final String IDNUMBER = "idnumber";

    public static final String AUDIT = "audit";

    public static final String AUDIT_COMMENT = "auditComment";

    public static final String IS_SIGN = "isSign";

    public static final String FREEZE = "freeze";

    public static final String LAST_LOGIN_IP = "lastLoginIp";

    public static final String LAST_LOGIN_TIME = "lastLoginTime";

    public static final String IS_DEL = "isDel";

    public static final String UTIME = "utime";

    public static final String CTIME = "ctime";
}