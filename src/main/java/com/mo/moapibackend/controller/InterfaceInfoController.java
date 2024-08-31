package com.mo.moapibackend.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.mo.moapibackend.exception.BaseResponse;
import com.mo.moapibackend.exception.BusinessException;
import com.mo.moapibackend.exception.ErrorCode;
import com.mo.moapibackend.exception.ResultUtils;
import com.mo.moapibackend.model.entity.InterfaceInfo;
import com.mo.moapibackend.model.request.interfaceInfo.OffLineInterfaceRequestParams;
import com.mo.moapibackend.model.request.interfaceInfo.OnLineInterfaceRequestParams;
import com.mo.moapibackend.model.request.interfaceInfo.QueryInterfaceInfoRequestParams;
import com.mo.moapibackend.model.request.interfaceInfo.UpdateInterfaceInfoRequestParams;
import com.mo.moapibackend.service.InterfaceInfoService;
import com.mo.moapibackend.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 接口管理
 */
@RestController
@RequestMapping("/interfaceInfo")
public class InterfaceInfoController {

    @Resource
    private UserService userService;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @PostMapping("/onLine")
    public BaseResponse<Integer> onLineInterfaceInfo(OnLineInterfaceRequestParams onLineInterfaceRequestParams, HttpServletRequest request) {
        if (onLineInterfaceRequestParams == null || request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int interfaceInfoId = interfaceInfoService.onLineInterfaceInfo(onLineInterfaceRequestParams, request);
        if (interfaceInfoId<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"上线接口错误");
        }
        return ResultUtils.success(interfaceInfoId);
    }

    @PostMapping("/offLine")
    public BaseResponse<Integer> offLineInterfaceInfo(OffLineInterfaceRequestParams offLineInterfaceRequestParams, HttpServletRequest request) {
        if (offLineInterfaceRequestParams == null || request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int offLineResult = interfaceInfoService.offLineInterfaceInfo(offLineInterfaceRequestParams, request);
        if (offLineResult<=0){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"下线接口失败");
        }
        return ResultUtils.success(offLineResult);
    }
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(UpdateInterfaceInfoRequestParams updateInterfaceInfoRequestParams,HttpServletRequest request){
        if (updateInterfaceInfoRequestParams==null || request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = interfaceInfoService.updateInterfaceInfo(updateInterfaceInfoRequestParams, request);
        if (!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"更新接口信息失败");
        }
        return ResultUtils.success(result);
    }
    @GetMapping("/query")
    public BaseResponse<List<InterfaceInfo>> queryInterfaceInfo(QueryInterfaceInfoRequestParams queryInterfaceInfoRequestParams){
        if (queryInterfaceInfoRequestParams==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.queryInterfaceInfo(queryInterfaceInfoRequestParams);
        if (CollectionUtil.isEmpty(interfaceInfoList)){
            return ResultUtils.success(new ArrayList<>());
        }
        return ResultUtils.success(interfaceInfoList);
    }
    @GetMapping("/usable")
    public BaseResponse<List<InterfaceInfo>> getAllUsableInterfaceInfo(HttpServletRequest request){
        if (request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<InterfaceInfo> allUsableInterfaceInfo = interfaceInfoService.getAllUsableInterfaceInfo(request);
        if (CollUtil.isEmpty(allUsableInterfaceInfo)){
            return ResultUtils.success(new ArrayList<>());
        }
        return ResultUtils.success(allUsableInterfaceInfo);
    }
}
