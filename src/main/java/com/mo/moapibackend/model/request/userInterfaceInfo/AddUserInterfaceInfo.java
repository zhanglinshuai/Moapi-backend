package com.mo.moapibackend.model.request.userInterfaceInfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 添加用户接口的参数类
 */
@Data
public class AddUserInterfaceInfo implements Serializable {

    private static final long serialVersionUID = -3626802717546497704L;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 接口状态 0-禁用 1 -正常
     */
    private Integer status;

    /**
     * 接口id
     */
    private Integer interfaceInfoId;

    /**
     * 总调用次数
     */
    private Integer totalNumber;

    /**
     * 剩余调用次数
     */
    private Integer residualNumber;


}
