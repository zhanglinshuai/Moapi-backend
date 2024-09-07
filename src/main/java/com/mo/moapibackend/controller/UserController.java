package com.mo.moapibackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mo.moapibackend.annotation.UserLoginToken;
import com.mo.moapibackend.exception.BaseResponse;
import com.mo.moapibackend.exception.BusinessException;
import com.mo.moapibackend.exception.ErrorCode;
import com.mo.moapibackend.exception.ResultUtils;
import com.mo.moapibackend.service.UserService;
import com.mo.moapicommon.model.entity.User;
import com.mo.moapicommon.model.request.Page.PageRequestParams;
import com.mo.moapicommon.model.request.user.UpdatePasswordParams;
import com.mo.moapicommon.model.request.user.UpdateUserInfo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {

    @Resource
    private UserService userService;


    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public BaseResponse<Long> userRegister(String userAccount,String userPassword,String checkPassword){
        //参数为空判断
        if (StringUtils.isAnyEmpty(userAccount,userPassword,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        //调用业务
        Long userId = userService.userRegister(userAccount, userPassword, checkPassword);
        if (userId==null || userId<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户信息错误");
        }
        return ResultUtils.success(userId);
    }
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public BaseResponse<User> userLogin(String userAccount, String userPassword, HttpServletRequest request, HttpServletResponse response){
        //参数非空判断
        if (StringUtils.isAnyEmpty(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        //调用业务
        User user = userService.userLogin(userAccount, userPassword, request,response);
        if (user==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"没有该用户信息，请先注册");
        }
        return ResultUtils.success(user);
    }

    @GetMapping("/current")
    @Operation(summary = "获取当前登录用户")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        if (request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User currentUser = userService.getCurrentUser(request);
        if (currentUser==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return ResultUtils.success(currentUser);
    }


    @PostMapping("/exit")
    @Operation(summary = "用户退出登录")
    public BaseResponse<Boolean> userExit(HttpServletRequest request,HttpServletResponse response){
        if (request==null|| response==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.userExit(request,response);
        if (!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"退出登录失败");
        }
        return ResultUtils.success(result);
    }

    @UserLoginToken
    @GetMapping("/list")
    public BaseResponse<Page<User>> getUserList(PageRequestParams params, HttpServletRequest request){
        if (request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<User> userList = userService.getUserList(params, request);

        return ResultUtils.success(userList);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updatePassword(UpdatePasswordParams updatePasswordParams, HttpServletRequest request){
        if (updatePasswordParams==null || request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String oldPassword = updatePasswordParams.getOldPassword();
        String newPassword = updatePasswordParams.getNewPassword();
        String checkPassword = updatePasswordParams.getCheckPassword();
        if (StringUtils.isAnyEmpty(oldPassword,newPassword,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        boolean result = userService.updatePassword(updatePasswordParams, request);
        if (!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"更新错误");
        }
        return ResultUtils.success(result);
    }

    @PostMapping("/update/userInfo")
    public BaseResponse<User> updateUserInfo(@RequestBody UpdateUserInfo updateUserInfo, HttpServletRequest request){
        if (updateUserInfo==null ||request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.updateUserInfo(updateUserInfo, request);
        if (user==null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(user);
    }
}
