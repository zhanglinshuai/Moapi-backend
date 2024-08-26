package com.mo.moapibackend.service;

import com.mo.moapibackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
* @author 86175
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-08-26 21:21:06
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param password
     * @param userAccount
     * @param checkPassword
     * @return
     */
    Long userRegister(String password,String userAccount,String checkPassword);

    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @return
     */
    User userLogin(String userAccount,String userPassword);
}
