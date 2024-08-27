package com.mo.moapibackend.controller;

import com.mo.moapibackend.exception.BaseResponse;
import com.mo.moapibackend.exception.BusinessException;
import com.mo.moapibackend.exception.ErrorCode;
import com.mo.moapibackend.exception.ResultUtils;
import com.mo.moapibackend.model.dto.UserDTO;
import com.mo.moapibackend.model.entity.User;
import com.mo.moapibackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
@Slf4j
@CrossOrigin(origins = "http://localhost:8000", allowCredentials = "true")
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
    public BaseResponse<UserDTO> userLogin(String userAccount, String userPassword, HttpServletRequest request){
        //参数非空判断
        if (StringUtils.isAnyEmpty(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        //调用业务
        UserDTO userDTO = userService.userLogin(userAccount, userPassword,request);
        if (userDTO==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"没有该用户信息，请先注册");
        }
        return ResultUtils.success(userDTO);
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
}
