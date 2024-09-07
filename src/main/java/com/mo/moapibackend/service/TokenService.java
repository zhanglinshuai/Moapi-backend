package com.mo.moapibackend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mo.moapicommon.model.entity.User;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.mo.moapibackend.commons.UserConstants.TOKEN_SALT;

/**
 * @author ：Mr.ZJW
 * @date ：Created 2022/2/28 10:20
 * @description：
 */
@Service
public class TokenService {

    public String getToken(User user) {
        Date start = new Date();
        long currentTime = System.currentTimeMillis() + 60* 60 * 1000;//一小时有效时间
        Date end = new Date(currentTime);
        String token = "";
        token = JWT.create()
                .withAudience(String.valueOf(user.getId()))
                .withIssuedAt(start)
                .withExpiresAt(end)
                .sign(Algorithm.HMAC256(TOKEN_SALT));
        return token;
    }
}



