-- INIT DB

create table `user_auth_phone`
(
    `id`        int primary key auto_increment,
    `tenant_id` int                                not null comment '租户ID',
    `user_id`   int                                not null comment '用户ID',
    `phone`     varchar(16)                        not null comment '手机号',
    `pwd`       varchar(255)                       not null comment '密码',
    `is_del`    tinyint  default 0                 not null comment '是否删除',
    `updater`   varchar(255)                       null comment '更新人',
    `utime`     datetime default current_timestamp not null comment '更新时间',
    `creater`   varchar(255)                       not null comment '创建人',
    `ctime`     datetime default current_timestamp not null comment '创建时间'
);