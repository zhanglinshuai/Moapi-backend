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
    /**
     * 用户登录态的存储
     */
    public static final String LOGIN_STATUS = "loginStatus";

    /**
     * accessKey的盐值
     */
    public static final String ACCESS_KEY = "access_key";
    /**
     * secretKey的盐值
     */
    public static final String SECRET_KEY = "secret_key";

    private static final long serialVersionUID = 8080839610555774012L;

    /**
     * token盐值
     */
    public static final String TOKEN_SALT = "token";

}
