package com.mo.moapibackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mo.moapibackend.exception.BaseResponse;
import com.mo.moapibackend.exception.BusinessException;
import com.mo.moapibackend.exception.ErrorCode;
import com.mo.moapibackend.exception.ResultUtils;

import com.mo.moapibackend.service.UserInterfaceinfoService;
import com.mo.moapicommon.model.entity.UserInterfaceinfo;
import com.mo.moapicommon.model.request.Page.PageRequestParams;
import com.mo.moapicommon.model.request.userInterfaceInfo.AddUserInterfaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/userInterfaceInfo")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@Slf4j
public class UserInterfaceInfoController {

    @Resource
    private UserInterfaceinfoService userInterfaceinfoService;

    @PostMapping("/add")
    public BaseResponse<Integer> addUserInterfaceInfo(AddUserInterfaceInfo addUserInterfaceInfo){
        if(addUserInterfaceInfo == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int UserInterfaceInfoId = userInterfaceinfoService.addUserInterfaceInfo(addUserInterfaceInfo);
        if (UserInterfaceInfoId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(UserInterfaceInfoId);
    }

    @PostMapping("/delete")
    public BaseResponse<Integer> deleteUserInterfaceInfo(Integer userId,Integer interfaceId){
        if(userId == null || interfaceId == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int UserInterfaceInfoId = userInterfaceinfoService.deleteUserInterfaceInfo(userId,interfaceId);
        if (UserInterfaceInfoId == 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(UserInterfaceInfoId);
    }


    @PostMapping("/update")
    public BaseResponse<UserInterfaceinfo> updateUserInterfaceInfo(Integer userId, Integer interfaceId, Integer totalNumber, Integer ResidualNumber){
        if(userId == null || interfaceId == null || totalNumber == null || ResidualNumber == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userId<=0 || interfaceId<=0 || totalNumber<=0 || ResidualNumber<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceinfo userInterfaceinfo = userInterfaceinfoService.updateUserInterfaceInfo(userId, interfaceId, totalNumber, ResidualNumber);
        return ResultUtils.success(userInterfaceinfo);
    }

    @GetMapping("/get")
    public BaseResponse<UserInterfaceinfo> getUserInterfaceInfo(Integer userId,Integer interfaceId){
        if(userId == null || interfaceId == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceinfo userInterfaceinfo = userInterfaceinfoService.getUserInterfaceInfo(userId, interfaceId);

        return ResultUtils.success(userInterfaceinfo);

    }
    @GetMapping("/page")
    public BaseResponse<Page<UserInterfaceinfo>> getUserInterfaceInfoPage(Integer userId, PageRequestParams params){
        if (userId == null || params == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<UserInterfaceinfo> userInterfaceInfoList = userInterfaceinfoService.getUserInterfaceInfoList(params, userId);
        if (userInterfaceInfoList==null){
           return null;
        }
        return ResultUtils.success(userInterfaceInfoList);
    }

    @GetMapping("/length")
    public BaseResponse<Integer> getUserInterfaceInfoLength(){
        List<UserInterfaceinfo> list = userInterfaceinfoService.list();
        return ResultUtils.success(list.size());
    }
}
