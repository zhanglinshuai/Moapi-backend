package com.mo.moapibackend.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mo.moapibackend.exception.BaseResponse;
import com.mo.moapibackend.exception.BusinessException;
import com.mo.moapibackend.exception.ErrorCode;
import com.mo.moapibackend.exception.ResultUtils;
import com.mo.moapibackend.model.entity.InterfaceInfo;
import com.mo.moapibackend.model.request.Page.PageRequestParams;
import com.mo.moapibackend.model.request.interfaceInfo.*;
import com.mo.moapibackend.service.InterfaceInfoService;
import com.mo.moapibackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 接口管理
 */
@RestController
@RequestMapping("/interfaceInfo")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private UserService userService;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @PostMapping("/onLine")
    public BaseResponse<Integer> onLineInterfaceInfo(@RequestBody OnLineInterfaceRequestParams onLineInterfaceRequestParams, HttpServletRequest request) {
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
    @GetMapping("/page")
    public BaseResponse<Page<InterfaceInfo>> getAllInterfaceInfo(PageRequestParams params, HttpServletRequest request){
        if (params==null || request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<InterfaceInfo> allInterfaceInfo = interfaceInfoService.getAllInterfaceInfo(params, request);
        if (allInterfaceInfo.getSize()<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口列表为空");
        }
        return ResultUtils.success(allInterfaceInfo);
    }

    @DeleteMapping("/deleteBulk")
    public BaseResponse<Boolean> deleteInterfaceInfo( List<Integer> interfaceInfoIds, HttpServletRequest request){
        if ( CollUtil.isEmpty(interfaceInfoIds)|| request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = interfaceInfoService.DeleteInterfaceInfo(interfaceInfoIds, request);
        if (!result){
            return ResultUtils.success(false);
        }
        return ResultUtils.success(true);
    }

    @GetMapping("/selectById")
    public BaseResponse<InterfaceInfo> selectInterfaceById(Integer id, HttpServletRequest request){
        if (id==null || request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfoById = interfaceInfoService.getInterfaceInfoById(id, request);
        if (interfaceInfoById==null){
            return ResultUtils.success(null);
        }
        return ResultUtils.success(interfaceInfoById);
    }

    @GetMapping("/length")
    public BaseResponse<Integer> getInterfaceInfoSize(){
        List<InterfaceInfo> list = interfaceInfoService.list();
        int size = list.size();
        return ResultUtils.success(size);
    }


    @GetMapping("/invoke")
    public BaseResponse<Object> invokeInterfaceInfo(InvokeInterfaceInfoRequestParams params, HttpServletRequest request){
        if (params==null || request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Integer id = params.getId();
        String interfaceParams = params.getInterfaceParams();
        if (id==null || interfaceParams==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Object result = interfaceInfoService.invokeInterfaceInfo(params, request);
        if (result==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"调用失败");
        }
        return ResultUtils.success(result);
    }

}
