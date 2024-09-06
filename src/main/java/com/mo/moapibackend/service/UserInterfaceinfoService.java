package com.mo.moapibackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mo.moapibackend.model.entity.UserInterfaceinfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mo.moapibackend.model.request.Page.PageRequestParams;
import com.mo.moapibackend.model.request.userInterfaceInfo.AddUserInterfaceInfo;

import java.util.List;

/**
* @author 86175
* @description 针对表【user_interfaceinfo(用户接口信息表)】的数据库操作Service
* @createDate 2024-09-05 20:15:31
*/
public interface UserInterfaceinfoService extends IService<UserInterfaceinfo> {

    /**
     * 添加用户调用接口信息
     * @param addUserInterfaceInfo
     * @return
     */
    int addUserInterfaceInfo(AddUserInterfaceInfo addUserInterfaceInfo);

    /**
     * 删除用户调用接口信息
     * @param userId
     * @param interfaceId
     * @return
     */
    int deleteUserInterfaceInfo(Integer userId,Integer interfaceId);

    /**
     * 更新用户调用接口信息
     * @param userId
     * @param interfaceId
     * @param totalNumber
     * @param ResidualNumber
     * @return
     */
    UserInterfaceinfo updateUserInterfaceInfo(Integer userId,Integer interfaceId,Integer totalNumber,Integer ResidualNumber);

    /**
     * 获取当前用户调用的当前接口的用户接口信息
     * @param userId
     * @param interfaceId
     * @return
     */
    UserInterfaceinfo getUserInterfaceInfo(Integer userId,Integer interfaceId);

    /**
     * 获取所有用户调用接口信息（仅管理员）
     * @param userId
     * @param params
     * @return
     */
    Page<UserInterfaceinfo> getUserInterfaceInfoList(PageRequestParams params,Integer userId);
}
