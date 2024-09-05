package com.mo.moapibackend.model.request.interfaceInfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mo.moapibackend.model.entity.InterfaceInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * 调用接口的参数
 */
@Data
public class InvokeInterfaceInfoRequestParams implements Serializable {

    private static final long serialVersionUID = -5905871775574000397L;

    /**
     * 接口id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String interfaceParams;

}
