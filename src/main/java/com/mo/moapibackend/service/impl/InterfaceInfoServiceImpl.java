package com.mo.moapibackend.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mo.moapibackend.exception.BusinessException;
import com.mo.moapibackend.exception.ErrorCode;
import com.mo.moapibackend.mapper.InterfaceInfoMapper;
import com.mo.moapibackend.model.entity.InterfaceInfo;
import com.mo.moapibackend.model.entity.User;
import com.mo.moapibackend.model.request.Page.PageRequestParams;
import com.mo.moapibackend.model.request.interfaceInfo.OffLineInterfaceRequestParams;
import com.mo.moapibackend.model.request.interfaceInfo.OnLineInterfaceRequestParams;
import com.mo.moapibackend.model.request.interfaceInfo.QueryInterfaceInfoRequestParams;
import com.mo.moapibackend.model.request.interfaceInfo.UpdateInterfaceInfoRequestParams;
import com.mo.moapibackend.service.InterfaceInfoService;
import com.mo.moapisdk.client.MoapiClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.mo.moapibackend.commons.UserConstants.LOGIN_STATUS;

/**
* @author 86175
* @description 针对表【interface_info(接口信息表)】的数据库操作Service实现
* @createDate 2024-08-30 22:14:05
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService{


    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;


    @Override
    public int onLineInterfaceInfo(OnLineInterfaceRequestParams onLineInterfaceRequestParams, HttpServletRequest request) {
        //参数非空判断
        if (onLineInterfaceRequestParams==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"上线接口的参数为空");
        }
        String interfaceDescription = onLineInterfaceRequestParams.getInterfaceDescription();
        String interfaceName = onLineInterfaceRequestParams.getInterfaceName();
        String interfaceResponseHeader = onLineInterfaceRequestParams.getInterfaceResponseHeader();
        String interfaceParams = onLineInterfaceRequestParams.getInterfaceParams();
        Integer interfaceStatus = onLineInterfaceRequestParams.getInterfaceStatus();
        String interfaceType = onLineInterfaceRequestParams.getInterfaceType();
        String interfaceUrl = onLineInterfaceRequestParams.getInterfaceUrl();
        String interfaceRequestHeader = onLineInterfaceRequestParams.getInterfaceRequestHeader();
        Long userId = onLineInterfaceRequestParams.getUserId();
        if (StringUtils.isAnyEmpty(interfaceRequestHeader,interfaceDescription,interfaceName,interfaceResponseHeader,interfaceUrl,interfaceType)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口信息为空");
        }
        if (interfaceStatus==null || interfaceStatus<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口状态错误");
        }
        if (request==null){
           throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Object attribute = request.getSession().getAttribute(LOGIN_STATUS);
        if (attribute==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //用户是管理员或者当前登录用户与传入的用户id相同
        User user = (User) attribute;
        if (!user.getUserRole().equals("管理员") || !user.getId().equals(userId)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR,"该用户不是管理员!");
        }
        // todo 测试调用接口
        MoapiClient moapiClient = new MoapiClient(user.getAccessKey(), user.getSecretKey());
        String userNamePost = moapiClient.getUserNamePost("zhang");
        System.out.println("interfaceInfo" + userNamePost);
        //测试通了，将状态改为上线
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setUserId(onLineInterfaceRequestParams.getUserId());
        interfaceInfo.setInterfaceName(onLineInterfaceRequestParams.getInterfaceName());
        interfaceInfo.setInterfaceDescription(onLineInterfaceRequestParams.getInterfaceDescription());
        interfaceInfo.setInterfaceUrl(onLineInterfaceRequestParams.getInterfaceUrl());
        interfaceInfo.setInterfaceType(onLineInterfaceRequestParams.getInterfaceType());
        interfaceInfo.setInterfaceParams(onLineInterfaceRequestParams.getInterfaceParams());
        interfaceInfo.setInterfaceRequestHeader(onLineInterfaceRequestParams.getInterfaceRequestHeader());
        interfaceInfo.setInterfaceResponseHeader(onLineInterfaceRequestParams.getInterfaceResponseHeader());
        interfaceInfo.setInterfaceStatus(1);

        //将接口信息插入到数据库中
        int insert = interfaceInfoMapper.insert(interfaceInfo);
        if (insert<=0){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"插入异常");
        }
        return interfaceInfo.getId();
    }

    @Override
    public int offLineInterfaceInfo(OffLineInterfaceRequestParams offLineInterfaceRequestParams, HttpServletRequest request) {
        if (offLineInterfaceRequestParams==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String interfaceDescription = offLineInterfaceRequestParams.getInterfaceDescription();
        String interfaceParams = offLineInterfaceRequestParams.getInterfaceParams();
        String interfaceName = offLineInterfaceRequestParams.getInterfaceName();
        String interfaceUrl = offLineInterfaceRequestParams.getInterfaceUrl();
        String interfaceType = offLineInterfaceRequestParams.getInterfaceType();
        if (StringUtils.isAnyEmpty(interfaceDescription,interfaceName,interfaceUrl,interfaceType)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口参数为空");
        }
        //从数据库中查询接口是否存在
        QueryWrapper<InterfaceInfo> interfaceInfoQueryWrapper = new QueryWrapper<InterfaceInfo>().eq("interfaceDescription", interfaceDescription)
                .eq("interfaceName", interfaceName)
                .eq("interfaceUrl", interfaceUrl)
                .eq("interfaceType", interfaceType)
                .eq("interfaceParams", interfaceParams);
        InterfaceInfo interfaceInfo = interfaceInfoMapper.selectOne(interfaceInfoQueryWrapper);
        if (interfaceInfo==null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"没找到此接口");
        }
        //将接口的状态改成0
        interfaceInfo.setInterfaceStatus(0);
        //更新数据库
        int update = interfaceInfoMapper.updateById(interfaceInfo);
        if (update<=0){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"更新数据库错误");
        }
        return update;
    }

    @Override
    public boolean updateInterfaceInfo(UpdateInterfaceInfoRequestParams updateInterfaceInfoRequestParams, HttpServletRequest request) {
        if (updateInterfaceInfoRequestParams ==null || request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        String interfaceName = updateInterfaceInfoRequestParams.getInterfaceName();
        String interfaceDescription = updateInterfaceInfoRequestParams.getInterfaceDescription();
        String interfaceUrl = updateInterfaceInfoRequestParams.getInterfaceUrl();
        String interfaceType = updateInterfaceInfoRequestParams.getInterfaceType();
        String interfaceParams = updateInterfaceInfoRequestParams.getInterfaceParams();
        InterfaceInfo interfaceInfo = updateInterfaceInfoRequestParams.getInterfaceInfo();
        if (interfaceInfo==null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"未填写更新接口信息");
        }
        Long userId = updateInterfaceInfoRequestParams.getInterfaceInfo().getUserId();
        if (StringUtils.isAnyEmpty(interfaceDescription,interfaceName,interfaceUrl,interfaceType,interfaceParams)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"传递参数为空");
        }
        //校验是否为创建接口的userId或者为管理员
        Object attribute = request.getSession().getAttribute(LOGIN_STATUS);
        if (attribute==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = (User) attribute;
        Long loginUserId = user.getId();
        if (!userId.equals(loginUserId) || !user.getUserRole().equals("管理员")){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR,"该用户没有权限修改接口信息");
        }
        //根据interfaceName,interfaceDescription,interfaceUrl,interfaceType,interfaceParams来查询唯一的接口
        QueryWrapper<InterfaceInfo> interfaceInfoQueryWrapper = new QueryWrapper<InterfaceInfo>()
                .eq("interfaceName", interfaceName)
                .eq("interfaceDescription", interfaceDescription)
                .eq("interfaceUrl", interfaceUrl)
                .eq("interfaceType", interfaceType)
                .eq("interfaceParams", interfaceParams);
        InterfaceInfo updatedInterfaceInfo = interfaceInfoMapper.selectOne(interfaceInfoQueryWrapper);
        if (updatedInterfaceInfo==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口不存在");
        }
        //如果接口存在，将传过来的接口信息赋值给updatedInterfaceInfo
        updatedInterfaceInfo.setInterfaceName(interfaceInfo.getInterfaceName());
        updatedInterfaceInfo.setInterfaceDescription(interfaceInfo.getInterfaceDescription());
        updatedInterfaceInfo.setInterfaceUrl(interfaceInfo.getInterfaceUrl());
        updatedInterfaceInfo.setInterfaceType(interfaceInfo.getInterfaceType());
        updatedInterfaceInfo.setInterfaceParams(interfaceInfo.getInterfaceParams());
        updatedInterfaceInfo.setInterfaceStatus(interfaceInfo.getInterfaceStatus());
        updatedInterfaceInfo.setInterfaceRequestHeader(interfaceInfo.getInterfaceRequestHeader());
        updatedInterfaceInfo.setInterfaceResponseHeader(interfaceInfo.getInterfaceResponseHeader());
        //更新数据库
        int result = interfaceInfoMapper.updateById(updatedInterfaceInfo);
        if (result<=0){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"更新失败");
        }
        return true;
    }

    @Override
    public List<InterfaceInfo> queryInterfaceInfo(QueryInterfaceInfoRequestParams queryInterfaceInfoRequestParams) {
        if (queryInterfaceInfoRequestParams==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String interfaceName = queryInterfaceInfoRequestParams.getInterfaceName();
        QueryWrapper<InterfaceInfo> interfaceInfoQueryWrapper = new QueryWrapper<>();
        interfaceInfoQueryWrapper.eq("interfaceName", interfaceName);
        List<InterfaceInfo> interfaceInfos = interfaceInfoMapper.selectList(interfaceInfoQueryWrapper);
        if (CollUtil.isEmpty(interfaceInfos)){
            return new ArrayList<>();
        }
        return interfaceInfos;
    }

    @Override
    public List<InterfaceInfo> getAllUsableInterfaceInfo(HttpServletRequest request) {
        if (request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> interfaceInfoQueryWrapper = new QueryWrapper<>();
        //select * from interfaceInfo where interfaceStatus = 1;
        interfaceInfoQueryWrapper.last("interfaceStatus = 0");
        List<InterfaceInfo> UsableInterfaceList = interfaceInfoMapper.selectList(interfaceInfoQueryWrapper);
        if (CollUtil.isEmpty(UsableInterfaceList)){
            return new ArrayList<>();
        }
        return UsableInterfaceList;
    }

    @Override
    public Page<InterfaceInfo> getAllInterfaceInfo(PageRequestParams params, HttpServletRequest request) {
        if (params==null || request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //校验用户是不是管理员
        Object attribute = request.getSession().getAttribute(LOGIN_STATUS);
        if (attribute==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户未登录");
        }
        User user = (User) attribute;
        if (!user.getUserRole().equals("管理员")){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR,"该用户不是管理员");
        }
        //如果是管理员
        int currentPage = params.getCurrentPage();
        int pageSize = params.getPageSize();
        Page<InterfaceInfo> interfaceInfoPage = new Page<>(currentPage,pageSize);
        Page<InterfaceInfo> pageList = this.page(interfaceInfoPage);
        if (pageList.getSize()<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口列表为空");
        }
        return pageList;
    }
}




