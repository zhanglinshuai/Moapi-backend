package com.mo.moapibackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mo.moapibackend.model.entity.InterfaceInfo;
import com.mo.moapibackend.service.InterfaceInfoService;
import com.mo.moapibackend.mapper.InterfaceInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author 86175
* @description 针对表【interface_info(接口信息表)】的数据库操作Service实现
* @createDate 2024-08-30 22:14:05
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService{

}




