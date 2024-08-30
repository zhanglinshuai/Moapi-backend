package com.mo.moapibackend.model.request.interfaceInfo;

import com.mo.moapibackend.model.entity.InterfaceInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * 更新接口的参数
 */
@Data
public class UpdateInterfaceInfoRequestParams implements Serializable {

    private static final long serialVersionUID = -5905871775574000397L;

    private InterfaceInfo interfaceInfo;

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

}
