package com.mo.moapibackend.model.request.user;

import com.mo.moapibackend.model.Vo.UserVo;
import lombok.Data;

import java.io.Serializable;

/**
 * 更新用户信息的参数
 */
@Data
public class UpdateUserInfo implements Serializable {
    private static final long serialVersionUID = -8182580327208729934L;

    private String userName;

    private String userAccount;

    private String userIntroduce;

    private String userAvatar;
}
