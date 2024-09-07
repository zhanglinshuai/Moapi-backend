package com.mo.moapibackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mo.moapibackend.service.UserInterfaceinfoService;
import com.mo.moapicommon.model.entity.UserInterfaceinfo;
import com.mo.moapicommon.service.InnerUserInterfaceinfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
@DubboService
public class InnerUserInterfaceinfoServiceImpl implements InnerUserInterfaceinfoService {

    @Resource
    private UserInterfaceinfoService userInterfaceinfoService;

    @Override
    public boolean InvokeCount(long interfaceInfoId, long userId) {
        return userInterfaceinfoService.InvokeCount(interfaceInfoId,userId);
    }

    @Override
    public boolean saveBatch(Collection<UserInterfaceinfo> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<UserInterfaceinfo> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<UserInterfaceinfo> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(UserInterfaceinfo entity) {
        return false;
    }

    @Override
    public UserInterfaceinfo getOne(Wrapper<UserInterfaceinfo> queryWrapper, boolean throwEx) {
        return null;
    }

    @Override
    public Map<String, Object> getMap(Wrapper<UserInterfaceinfo> queryWrapper) {
        return Collections.emptyMap();
    }

    @Override
    public <V> V getObj(Wrapper<UserInterfaceinfo> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public BaseMapper<UserInterfaceinfo> getBaseMapper() {
        return null;
    }

    @Override
    public Class<UserInterfaceinfo> getEntityClass() {
        return null;
    }
}
