package com.mo.moapibackend.commons;

import java.io.Serializable;

/**
 * 用户常量类
 * @author lengmo
 */
public class UserConstants implements Serializable {

    /**
     * 密码盐值
     */
    public static final String PASSWORD_SALT = "API";

    public static final String LOGIN_STATUS = "loginStatus";
    private static final long serialVersionUID = 8080839610555774012L;

}
