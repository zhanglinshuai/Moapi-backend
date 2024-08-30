package com.mo.moapibackend.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 接口信息表
 * @TableName interface_info
 */
@TableName(value ="interface_info")
@Data
public class InterfaceInfo implements Serializable {
    /**
     * 接口id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 创建接口的用户id
     */
    private Integer userId;

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 接口描述
     */
    private String interfaceDescription;

    /**
     * 接口路径
     */
    private String interfaceUrl;

    /**
     * 接口请求类型
     */
    private Integer interfaceType;

    /**
     * 接口参数
     */
    private String interfaceParams;

    /**
     * 接口状态 0-关闭  1-打开
     */
    private Integer interfaceStatus;

    /**
     * 接口请求头
     */
    private String interfaceRequestHeader;

    /**
     * 接口响应头
     */
    private String interfaceResponseHeader;

    /**
     * 是否删除 0-不删除 1-删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}