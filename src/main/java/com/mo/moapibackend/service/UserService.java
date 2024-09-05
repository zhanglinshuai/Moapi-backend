package com.mo.moapibackend.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mo.moapibackend.model.dto.UserDTO;
import com.mo.moapibackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mo.moapibackend.model.request.Page.PageRequestParams;
import com.mo.moapibackend.model.request.user.UpdatePasswordParams;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
     * @param request
     * @param response
     * @return
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request, HttpServletResponse response);

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    User getCurrentUser(HttpServletRequest request);

    /**
     * 获取脱敏后的用户
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);

    /**
     * 用户退出登录
     * @param request
     * @return
     */
    boolean userExit(HttpServletRequest request);

    /**
     * 获取所有用户信息（仅管理员）
     * @param request
     * @return
     */
    Page<User> getUserList(PageRequestParams params,HttpServletRequest request);

    /**
     * 修改密码
     * @param updatePasswordParams
     * @param request
     * @return
     */
    boolean updatePassword(UpdatePasswordParams updatePasswordParams, HttpServletRequest request);
}
