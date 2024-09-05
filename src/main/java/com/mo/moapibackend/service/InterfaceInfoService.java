package com.mo.moapibackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mo.moapibackend.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mo.moapibackend.model.request.Page.PageRequestParams;
import com.mo.moapibackend.model.request.interfaceInfo.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 86175
* @description 针对表【interface_info(接口信息表)】的数据库操作Service
* @createDate 2024-08-30 22:14:05
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 创建接口
     * @param onLineInterfaceRequestParams
     * @return
     */
    int onLineInterfaceInfo(OnLineInterfaceRequestParams onLineInterfaceRequestParams, HttpServletRequest request);

    /**
     * 下线接口
     * @param offLineInterfaceRequestParams
     * @param request
     * @return
     */
    int offLineInterfaceInfo(OffLineInterfaceRequestParams offLineInterfaceRequestParams, HttpServletRequest request);

    /**
     * 更新接口
     * @param updateInterfaceInfoRequestParams
     * @return
     */
    boolean updateInterfaceInfo(UpdateInterfaceInfoRequestParams updateInterfaceInfoRequestParams, HttpServletRequest request);

    /**
     * 查询接口信息
     * @param queryInterfaceInfoRequestParams
     * @return
     */
    List<InterfaceInfo> queryInterfaceInfo(QueryInterfaceInfoRequestParams queryInterfaceInfoRequestParams);

    /**
     * 获取所有 接口状态为0的接口
     * @param request
     * @return
     */
    List<InterfaceInfo> getAllUsableInterfaceInfo(HttpServletRequest request);

    /**
     * 分页查询所有的接口信息
     * @param request
     * @param params
     * @return
     */
    Page<InterfaceInfo> getAllInterfaceInfo(PageRequestParams params,HttpServletRequest request);

    /**
     * 删除接口
     * @param interfaceInfoIds
     * @param request
     * @return
     */
    boolean DeleteInterfaceInfo(List<Integer> interfaceInfoIds, HttpServletRequest request);

    /**
     * 根据接口id获取接口信息
     * @param id
     * @param request
     * @return
     */
    InterfaceInfo getInterfaceInfoById(Integer id, HttpServletRequest request);

    /**
     * 在线调用测试
     * @param params
     * @param request
     * @return
     */
    Object invokeInterfaceInfo(InvokeInterfaceInfoRequestParams params, HttpServletRequest request);
}
