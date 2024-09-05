package com.mo.moapibackend.model.Vo;

import lombok.Data;

/**
 * 原用户信息的视图数据
 */
@Data
public class UserVo {

    private String userName;

    private String userAccount;

    private String userIntroduction;

    private String userAvatar;
}
