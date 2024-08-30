package com.mo.moapibackend.model.request.interfaceInfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 下线接口的参数
 */
@Data
public class OffLineInterfaceRequestParams extends OnLineInterfaceRequestParams implements Serializable {

    private static final long serialVersionUID = 2130976778686990832L;

    /**
     * 接口id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

}
