package com.mo.moapibackend.model.request.interfaceInfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 下线接口的参数
 */
@Data
public class OffLineInterfaceRequestParams  implements Serializable {

    private static final long serialVersionUID = 2130976778686990832L;

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
