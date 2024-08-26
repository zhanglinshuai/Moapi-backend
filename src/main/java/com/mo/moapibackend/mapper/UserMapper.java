package com.mo.moapibackend.mapper;

import com.mo.moapibackend.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86175
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2024-08-26 21:21:06
* @Entity com.mo.moapibackend.model.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




