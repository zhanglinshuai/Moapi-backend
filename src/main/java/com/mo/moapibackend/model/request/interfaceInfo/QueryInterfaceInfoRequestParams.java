package com.mo.moapibackend.model.request.interfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询接口参数
 */
@Data
public class QueryInterfaceInfoRequestParams implements Serializable {

    private static final long serialVersionUID = 8993149220411416941L;

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
    private String interfaceType;

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
}
