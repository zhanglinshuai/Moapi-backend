package com.mo.moapibackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mo.moapibackend.model.entity.User;
import com.mo.moapibackend.service.UserService;
import com.mo.moapibackend.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 86175
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-08-26 21:21:06
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




