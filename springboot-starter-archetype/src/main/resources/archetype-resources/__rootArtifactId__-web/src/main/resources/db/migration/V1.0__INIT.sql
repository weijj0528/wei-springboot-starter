-- INIT DB

create table `hello`
(
    `id`        int primary key auto_increment,
    `tenant_id` int                                not null comment '租户ID',
    `name`      varchar(16)                        not null comment '手机号',
    `is_del`    tinyint  default 0                 not null comment '是否删除',
    `updater`   varchar(255) default ''                null comment '更新人',
    `utime`     datetime default current_timestamp not null comment '更新时间',
    `creater`   varchar(255)                       not null comment '创建人',
    `ctime`     datetime default current_timestamp not null comment '创建时间'
);

insert into `hello` (`tenant_id`, `name`, creater, ctime) values (1, 'hello', 'admin', now());
insert into `hello` (`tenant_id`, `name`, creater, ctime) values (1, 'world', 'admin', now());