package com.confession.back.util.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * jwt生成校验工具类
 */
public class JwtUtil {

    /**
     * 校验token
     *
     * @param token
     * @param number
     * @param pwd
     * @return
     */
    public static boolean verify(String token, String number, String pwd) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(pwd);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("number",number)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getNumber(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            String userName= jwt.getClaim("number").asString();
            return userName;
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名
     * @param number 用户登录账号
     * @param pwd  用户的密码
     * @return 加密的token
     */
    public static String sign(String number, String pwd) {
        try {
            Date date = new Date(System.currentTimeMillis() + 1440 * 60 * 1000);
            Algorithm algorithm = Algorithm.HMAC256(pwd);
            // 附带username信息
            return JWT.create()
                    .withClaim("number",number)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            return null;
        }
    }
}
