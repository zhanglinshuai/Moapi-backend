-- auto-generated definition
create table user
(
    id            bigint auto_increment comment '用户id'
        primary key,
    userAccount   varchar(256)                       null comment '用户账号',
    userName      varchar(256)                       null comment '用户名称',
    userPassword  varchar(256)                       null comment '用户密码',
    accessKey     varchar(512)                       null comment 'accessKey',
    secretKey     varchar(512)                       null comment 'secretKey',
    userIntroduce varchar(512)                       null comment '用户介绍',
    userRole      varchar(256)                       null comment '用户角色',
    createTime    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime    datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    isDelete      tinyint  default 0                 not null comment '逻辑删除  0-不删除  1-删除'
)
    comment '用户表';