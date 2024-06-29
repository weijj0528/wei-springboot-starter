-- INIT --

create table `hello`
(
    `id`      bigint primary key auto_increment comment '主键ID',
    `tenant`  bigint                                 not null comment '租户ID',
    `name`    varchar(16)                            not null comment '名称',
    `version` bigint       default 1                 not null comment '是否版本',
    `deleted` tinyint      default 0                 not null comment '是否删除',
    `updater` varchar(255) default ''                null comment '更新人',
    `utime`   datetime     default current_timestamp not null comment '更新时间',
    `creater` varchar(255)                           not null comment '创建人',
    `ctime`   datetime     default current_timestamp not null comment '创建时间'
);

create index hello_idx_tenant on HELLO (`tenant`);
create index hello_idx_deleted on HELLO (`deleted`);
create index hello_idx_ctime on HELLO (`ctime`);

insert into `hello` (`tenant`, `name`, `creater`, `ctime`) values (1, 'hello', 'admin', now());
insert into `hello` (`tenant`, `name`, `creater`, `ctime`) values (1, 'world', 'admin', dateadd('second', 1, now()));
