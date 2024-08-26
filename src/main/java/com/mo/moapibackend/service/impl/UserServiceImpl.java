package com.mo.moapibackend.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mo.moapibackend.exception.BusinessException;
import com.mo.moapibackend.exception.ErrorCode;
import com.mo.moapibackend.model.entity.User;
import com.mo.moapibackend.service.UserService;
import com.mo.moapibackend.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mo.moapibackend.commons.UserConstants.PASSWORD_SALT;

/**
* @author 86175
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-08-26 21:21:06
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;


    @Override
    public Long userRegister(String userAccount, String userPassword,String checkPassword){
        //参数非空校验
        if (StringUtils.isAnyEmpty(checkPassword, userPassword, userAccount)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        //参数合理校验
        if (userAccount.length() < 4 || userAccount.length() > 16) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号长度不合理");
        }
        if (userPassword.length() < 6 || checkPassword.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码或校验密码长度不合理");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码不一致");
        }
        //校验userAccount中是否含有特殊字符
        String regEx="[\\u00A0\\s\"`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern compile = Pattern.compile(regEx);
        Matcher matcher = compile.matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号中含有特殊字符");
        }
        //校验userAccount是否在数据库中已经存在了
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount", userAccount);
        User user = userMapper.selectOne(userQueryWrapper);
        if (user != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号已存在，无法重复");
        }
        //如果userAccount不存在
        //对密码进行加密
        String safetyPassword = DigestUtil.md5Hex((PASSWORD_SALT + userPassword).getBytes());
        //新建用户，插入用户
        User insertUser = new User();
        insertUser.setUserAccount(userAccount);
        insertUser.setUserPassword(safetyPassword);
        boolean save = this.save(insertUser);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"插入失败");
        }
        return insertUser.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword) {
        if (StringUtils.isAnyEmpty(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"")
        }

    }
}




