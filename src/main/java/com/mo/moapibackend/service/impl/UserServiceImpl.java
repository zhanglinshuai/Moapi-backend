package com.mo.moapibackend.service.impl;
import java.util.Date;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SignUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mo.moapibackend.exception.BusinessException;
import com.mo.moapibackend.exception.ErrorCode;
import com.mo.moapibackend.exception.ResultUtils;
import com.mo.moapibackend.model.dto.UserDTO;
import com.mo.moapibackend.model.entity.User;
import com.mo.moapibackend.service.UserService;
import com.mo.moapibackend.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mo.moapibackend.commons.UserConstants.*;

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
        if (user!=null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号已存在，无法重复");
        }
        //如果userAccount不存在
        //对密码进行加密
        String safetyPassword = DigestUtil.md5Hex((PASSWORD_SALT + userPassword).getBytes());
        //给用户分配ak sk
        String accessKey = DigestUtil.md5Hex((userAccount + ACCESS_KEY + RandomUtil.randomNumbers(4)).getBytes());
        String secretKey = DigestUtil.md5Hex((userAccount + SECRET_KEY + RandomUtil.randomNumbers(6)).getBytes());
        //新建用户，插入用户
        User insertUser = new User();
        insertUser.setUserAccount(userAccount);
        insertUser.setUserPassword(safetyPassword);
        insertUser.setAccessKey(accessKey);
        insertUser.setSecretKey(secretKey);
        boolean save = this.save(insertUser);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"插入失败");
        }
        return insertUser.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword,HttpServletRequest request) {
        //非空判断
        if (StringUtils.isAnyEmpty(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号或密码为空");
        }
        if (request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //参数合理判断
        if (userAccount.length() < 4 || userAccount.length() > 16) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号长度不合理");
        }
        if (userPassword.length()<6){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码长度不合理");
        }
        //校验userAccount中是否含有特殊字符
        String regEx="[\\u00A0\\s\"`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern compile = Pattern.compile(regEx);
        Matcher matcher = compile.matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号中含有特殊字符");
        }
        //对密码进行加密
        String safetyPassword = DigestUtil.md5Hex((PASSWORD_SALT + userPassword).getBytes());
        //判断数据库中是否含有此账号
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount", userAccount);
        userQueryWrapper.eq("userPassword", safetyPassword);
        User user = userMapper.selectOne(userQueryWrapper);
        Long userId = user.getId();
        if (userId == null || userId<=0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"没有该用户信息，请先注册");
        }
        User safetyUser = getSafetyUser(user);
        //将用户登录态存储到session中
        request.getSession().setAttribute(LOGIN_STATUS,safetyUser);
        return safetyUser;
    }

    @Override
    public User getCurrentUser(HttpServletRequest request) {
        if (request==null){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR,"用户未登录");
        }
        Object attribute = request.getSession().getAttribute(LOGIN_STATUS);
        if (attribute==null){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR,"用户未登录");
        }
        User loginUser = (User) attribute;
        Long id = loginUser.getId();
        User user = userMapper.selectById(id);
        if (user==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户信息错误");
        }
        return getSafetyUser(user);

    }

    @Override
    public User getSafetyUser(User originUser) {
        if (originUser==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户信息为空");
        }
        User user = new User();
        user.setId(originUser.getId());
        user.setUserAccount(originUser.getUserAccount());
        user.setUserName(originUser.getUserName());
        user.setAccessKey(originUser.getAccessKey());
        user.setSecretKey(originUser.getSecretKey());
        user.setUserIntroduce(originUser.getUserIntroduce());
        user.setUserRole(originUser.getUserRole());
        user.setCreateTime(originUser.getCreateTime());
        user.setUpdateTime(originUser.getUpdateTime());
        user.setIsDelete(originUser.getIsDelete());
        return user;
    }

    @Override
    public boolean userExit(HttpServletRequest request) {
        if (request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        request.getSession().removeAttribute(LOGIN_STATUS);
        return true;
    }
}




