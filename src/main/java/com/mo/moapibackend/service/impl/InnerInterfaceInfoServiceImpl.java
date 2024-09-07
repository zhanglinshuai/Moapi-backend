package com.mo.moapibackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mo.moapibackend.exception.BusinessException;
import com.mo.moapibackend.exception.ErrorCode;
import com.mo.moapibackend.mapper.InterfaceInfoMapper;
import com.mo.moapicommon.model.entity.InterfaceInfo;
import com.mo.moapicommon.model.request.interfaceInfo.QueryInterfaceInfoRequestParams;
import com.mo.moapicommon.service.InnerInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public InterfaceInfo getInterfaceInfoById(QueryInterfaceInfoRequestParams params) {
       if (params==null){
           throw new BusinessException(ErrorCode.PARAMS_ERROR);
       }
        String interfaceUrl = params.getInterfaceUrl();
        String interfaceType = params.getInterfaceType();
        QueryWrapper<InterfaceInfo> interfaceInfoQueryWrapper = new QueryWrapper<>();
        interfaceInfoQueryWrapper.eq("interfaceUrl", interfaceUrl);
        interfaceInfoQueryWrapper.eq("interfaceType", interfaceType);
        return interfaceInfoMapper.selectOne(interfaceInfoQueryWrapper);
    }

    @Override
    public boolean saveBatch(Collection<InterfaceInfo> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<InterfaceInfo> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<InterfaceInfo> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(InterfaceInfo entity) {
        return false;
    }

    @Override
    public InterfaceInfo getOne(Wrapper<InterfaceInfo> queryWrapper, boolean throwEx) {
        return null;
    }

    @Override
    public Map<String, Object> getMap(Wrapper<InterfaceInfo> queryWrapper) {
        return Collections.emptyMap();
    }

    @Override
    public <V> V getObj(Wrapper<InterfaceInfo> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public BaseMapper<InterfaceInfo> getBaseMapper() {
        return null;
    }

    @Override
    public Class<InterfaceInfo> getEntityClass() {
        return null;
    }
}
