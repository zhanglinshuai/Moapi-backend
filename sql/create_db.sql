
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
    isDelete      tinyint  default 0                 not null comment '逻辑删除  0-不删除  1-删除',
    userAvatar    varchar(1024)                      null comment '用户头像'
)
    comment '用户表';

-- auto-generated definition
create table interface_info
(
    id                      int auto_increment comment '接口id'
        primary key,
    userId                  int                                null comment '创建接口的用户id',
    interfaceName           varchar(256)                       null comment '接口名称',
    interfaceDescription    varchar(512)                       null comment '接口描述',
    interfaceUrl            varchar(512)                       null comment '接口路径',
    interfaceType           int                                null comment '接口请求类型',
    interfaceParams         varchar(512)                       null comment '接口参数',
    interfaceStatus         int                                null comment '接口状态 0-关闭  1-打开',
    interfaceRequestHeader  varchar(256)                       null comment '接口请求头',
    interfaceResponseHeader varchar(256)                       null comment '接口响应头',
    isDelete                tinyint  default 0                 not null comment '是否删除 0-不删除 1-删除',
    createTime              datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime              datetime default CURRENT_TIMESTAMP not null comment '更新时间'
)
    comment '接口信息表';
