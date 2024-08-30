package com.mo.moapibackend.service;

import com.mo.moapibackend.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mo.moapibackend.model.request.interfaceInfo.OffLineInterfaceRequestParams;
import com.mo.moapibackend.model.request.interfaceInfo.OnLineInterfaceRequestParams;
import com.mo.moapibackend.model.request.interfaceInfo.QueryInterfaceInfoRequestParams;
import com.mo.moapibackend.model.request.interfaceInfo.UpdateInterfaceInfoRequestParams;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 86175
* @description 针对表【interface_info(接口信息表)】的数据库操作Service
* @createDate 2024-08-30 22:14:05
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {


    /**
     * 验证请求参数
     * @param params
     * @param request
     * @return
     */
    boolean verifyRequestParams(OnLineInterfaceRequestParams params, HttpServletRequest request);

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
}
