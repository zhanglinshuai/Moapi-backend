package com.mo.moapibackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mo.moapibackend.exception.BusinessException;
import com.mo.moapibackend.exception.ErrorCode;

import com.mo.moapibackend.service.InterfaceInfoService;
import com.mo.moapibackend.service.UserInterfaceinfoService;
import com.mo.moapibackend.mapper.UserInterfaceinfoMapper;
import com.mo.moapibackend.service.UserService;
import com.mo.moapicommon.model.entity.InterfaceInfo;
import com.mo.moapicommon.model.entity.User;
import com.mo.moapicommon.model.entity.UserInterfaceinfo;
import com.mo.moapicommon.model.request.Page.PageRequestParams;
import com.mo.moapicommon.model.request.userInterfaceInfo.AddUserInterfaceInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.tags.ParamTag;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
* @author 86175
* @description 针对表【user_interfaceinfo(用户接口信息表)】的数据库操作Service实现
* @createDate 2024-09-05 20:15:31
*/
@Service
public class UserInterfaceinfoServiceImpl extends ServiceImpl<UserInterfaceinfoMapper, UserInterfaceinfo>
    implements UserInterfaceinfoService{

    @Resource
    private UserInterfaceinfoMapper userInterfaceinfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    @Override
    public int addUserInterfaceInfo(AddUserInterfaceInfo addUserInterfaceInfo) {
        if (addUserInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Integer interfaceInfoId = addUserInterfaceInfo.getInterfaceInfoId();
        Integer status = addUserInterfaceInfo.getStatus();
        Integer userId = addUserInterfaceInfo.getUserId();
        Integer totalNumber = addUserInterfaceInfo.getTotalNumber();
        Integer residualNumber = addUserInterfaceInfo.getResidualNumber();
        if (interfaceInfoId==null || status==null || userId==null || totalNumber==null|| residualNumber==null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (interfaceInfoId<=0 || status<=0 || userId<=0 || totalNumber<=0 || residualNumber<=0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //校验interface是否存在
        InterfaceInfo byId = interfaceInfoService.getById(interfaceInfoId);
        if (byId==null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口信息不存在");
        }
        //校验user是否存在
        User user = userService.getById(userId);
        if (user==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户不存在");
        }
        UserInterfaceinfo userInterfaceinfo = new UserInterfaceinfo();
        userInterfaceinfo.setInterfaceInfoId(interfaceInfoId);
        userInterfaceinfo.setStatus(status);
        userInterfaceinfo.setUserId(userId);
        userInterfaceinfo.setTotalNumber(totalNumber);
        userInterfaceinfo.setResidualNumber(residualNumber);
        int insert = userInterfaceinfoMapper.insert(userInterfaceinfo);
        if (insert!=1){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"插入错误");
        }
        return userInterfaceinfo.getId();
    }

    @Override
    public int deleteUserInterfaceInfo(Integer userId, Integer interfaceId) {
        if (userId == null || interfaceId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userId<=0 || interfaceId<=0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceinfo> userInterfaceinfoQueryWrapper = new QueryWrapper<>();
        userInterfaceinfoQueryWrapper.eq("userId", userId);
        userInterfaceinfoQueryWrapper.eq("interfaceInfoId", interfaceId);

        return userInterfaceinfoMapper.delete(userInterfaceinfoQueryWrapper);
    }

    @Override
    public UserInterfaceinfo updateUserInterfaceInfo(Integer userId, Integer interfaceId,Integer totalNumber,Integer residualNumber) {
        if (userId == null || interfaceId == null || totalNumber==null || residualNumber==null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userId<=0 || interfaceId<=0 || totalNumber<=0 || residualNumber<=0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceinfo> userInterfaceinfoQueryWrapper = new QueryWrapper<>();
        userInterfaceinfoQueryWrapper.eq("userId", userId);
        userInterfaceinfoQueryWrapper.eq("interfaceInfoId", interfaceId);
        UserInterfaceinfo userInterfaceinfo = userInterfaceinfoMapper.selectOne(userInterfaceinfoQueryWrapper);
        if (userInterfaceinfo==null) {
           //如果用户调用接口的信息不存在就免费给体验3次
            AddUserInterfaceInfo addUserInterfaceInfo = new AddUserInterfaceInfo();
            addUserInterfaceInfo.setUserId(userId);
            addUserInterfaceInfo.setInterfaceInfoId(interfaceId);
            addUserInterfaceInfo.setTotalNumber(3);
            addUserInterfaceInfo.setResidualNumber(3);
            addUserInterfaceInfo.setStatus(1);
            int interfaceInfoId = addUserInterfaceInfo(addUserInterfaceInfo);
            return userInterfaceinfoMapper.selectById(interfaceInfoId);
        }
        //如果用户调用接口的信息存在就设置剩余调用次数
        userInterfaceinfo.setResidualNumber(residualNumber-1);
        return userInterfaceinfo;
    }

    @Override
    public UserInterfaceinfo getUserInterfaceInfo(Integer userId, Integer interfaceId) {
        if (userId==null || interfaceId==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userId<=0 || interfaceId<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceinfo> userInterfaceinfoQueryWrapper = new QueryWrapper<UserInterfaceinfo>()
                .eq("userId", userId)
                .eq("interfaceInfoId", interfaceId);
        return userInterfaceinfoMapper.selectOne(userInterfaceinfoQueryWrapper);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<UserInterfaceinfo> getUserInterfaceInfoList(PageRequestParams params, Integer userId) {
        if (userId==null || params==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(userId);
        if (user==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (!user.getUserRole().equals("管理员")){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int currentPage = params.getCurrentPage();
        int pageSize = params.getPageSize();
        Page<UserInterfaceinfo> userInterfaceinfoPage = new Page<>(currentPage, pageSize);
        Page<UserInterfaceinfo> page = this.page(userInterfaceinfoPage);
        if (page==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return page;
    }

    @Override
    public boolean InvokeCount(long interfaceId, long userId) {
        if (interfaceId<=0 || userId<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // update user_interfaceInfo set userId = {} && interafaceInfoId = {} where residualNumber  = residualNumber-1
        UpdateWrapper<UserInterfaceinfo> userInterfaceinfoQueryWrapper = new UpdateWrapper<>();
        userInterfaceinfoQueryWrapper.eq("interfaceInfoId", interfaceId);
        userInterfaceinfoQueryWrapper.eq("userId", userId);
        userInterfaceinfoQueryWrapper.setSql("residualNumber  = residualNumber-1");
        return this.update(userInterfaceinfoQueryWrapper);
    }
}




