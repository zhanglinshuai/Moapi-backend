package com.mo.moapibackend.service.impl;
import java.util.Collections;
import java.util.Date;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SignUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.JsonObject;
import com.mo.moapibackend.exception.BusinessException;
import com.mo.moapibackend.exception.ErrorCode;
import com.mo.moapibackend.exception.ResultUtils;
import com.mo.moapibackend.model.dto.UserDTO;
import com.mo.moapibackend.model.entity.User;
import com.mo.moapibackend.model.request.Page.PageRequestParams;
import com.mo.moapibackend.model.request.user.UpdatePasswordParams;
import com.mo.moapibackend.model.request.user.UpdateUserInfo;
import com.mo.moapibackend.service.TokenService;
import com.mo.moapibackend.service.UserService;
import com.mo.moapibackend.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mo.moapibackend.commons.UserConstants.*;

/**
* @author 86175
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-08-26 21:21:06
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    @Resource
    private TokenService tokenService;

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
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request, HttpServletResponse response) {
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
        JSONObject jsonObject = new JSONObject();
        String token = tokenService.getToken(user);
        jsonObject.put("token", token);
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        response.addCookie(cookie);
        request.getSession().setAttribute(LOGIN_STATUS,token);
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
        //获取token
        String token = (String) attribute;
        //解析token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(TOKEN_SALT)).build();
        DecodedJWT verify = jwtVerifier.verify(token);
        List<String> audience = verify.getAudience();
        //获取userId
        String id = audience.get(0);
        User user = userMapper.selectById(id);
        if (user==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
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
    public boolean userExit(HttpServletRequest request,HttpServletResponse response) {
        if (request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        request.getSession().removeAttribute(LOGIN_STATUS);
        return true;
    }



    @Override
    public Page<User> getUserList(PageRequestParams params,HttpServletRequest request) {
        if(request==null || params == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int currentPage = params.getCurrentPage();
        int pageSize = params.getPageSize();
        if (currentPage<=0 || pageSize <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Object attribute = request.getSession().getAttribute(LOGIN_STATUS);
        if (attribute==null){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        User loginUser = (User) attribute;
        if (!loginUser.getUserRole().equals("管理员")){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR,"该用户不是管理员");
        }
        Page<User> userPage = new Page<>(currentPage, pageSize);
        Page<User> page = this.page(userPage);
        if (page==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户列表为空");
        }
        return page;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updatePassword(UpdatePasswordParams updatePasswordParams, HttpServletRequest request) {
        if (updatePasswordParams==null || request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String oldPassword = updatePasswordParams.getOldPassword();
        String newPassword = updatePasswordParams.getNewPassword();
        String checkPassword = updatePasswordParams.getCheckPassword();
        if (StringUtils.isAnyEmpty(oldPassword,newPassword,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        Object attribute = request.getSession().getAttribute(LOGIN_STATUS);
        if (attribute==null){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        User loginUser = (User) attribute;
        //获取当前登录用户id
        Long userId = loginUser.getId();
        if (userId == null || userId<=0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户信息错误");
        }
        if (!newPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"新密码和校验密码不相同");
        }
        User user = this.getById(userId);
        String userPassword = user.getUserPassword();
        String safetyOldPass = DigestUtil.md5Hex((PASSWORD_SALT + oldPassword).getBytes());
        if (!safetyOldPass.equals(userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"原密码错误");
        }
        if (oldPassword.equals(newPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"修改前后密码一致");
        }
        //修改成功，更新数据库
        String safetyPassword = DigestUtil.md5Hex((PASSWORD_SALT + newPassword).getBytes());
        user.setUserPassword(safetyPassword);
        boolean result = this.updateById(user);
        if (!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"更新失败");
        }
        return result;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public User updateUserInfo(UpdateUserInfo updateUserInfo, HttpServletRequest request) {
        if (updateUserInfo==null ||request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userName = updateUserInfo.getUserName();
        String userAccount = updateUserInfo.getUserAccount();
        String userIntroduce = updateUserInfo.getUserIntroduce();
        String userAvatar = updateUserInfo.getUserAvatar();
        Object attribute = request.getSession().getAttribute(LOGIN_STATUS);
        if (attribute==null){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        User loginUser = (User) attribute;
        Long userId = loginUser.getId();
        if (userId == null || userId<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = this.getById(userId);
        user.setUserAccount(userAccount);
        user.setUserName(userName);
        user.setUserIntroduce(userIntroduce);
        user.setUserAvatar(userAvatar);
        //设置数据成功,更新数据库
        boolean result = this.updateById(user);
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount",userAccount);
        Long count = userMapper.selectCount(userQueryWrapper);
        if (count>=2){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号重复");
        }
        if (!result){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return user;
    }
}




