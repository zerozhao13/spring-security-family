package com.phoenix.security.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenix.security.dto.UserInfoDto;
import com.phoenix.security.entity.PayLoad;
import io.jsonwebtoken.*;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/** 生成token以及校验token相关方法 */
public class JwtUtils {

  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final String JWT_PAYLOAD_USERINFO = "user";

  /**
   * 私钥加密token
   *
   * @param userInfo 载荷中的数据
   * @param privateKey 私钥
   * @param expire 过期时间，单位分钟
   * @return JWT
   */
  public static String generateTokenExpireInMinutes(
      Object userInfo, PrivateKey privateKey, int expire) throws JsonProcessingException {
    return Jwts.builder()
        .claim(JWT_PAYLOAD_USERINFO, objectMapper.writeValueAsString(userInfo))
        .setId(String.valueOf(UUID.randomUUID()))
        .setExpiration(
            Date.from(
                LocalDateTime.now().plusMinutes(expire).atZone(ZoneId.systemDefault()).toInstant()))
        .signWith(privateKey, SignatureAlgorithm.RS256)
        .compressWith(CompressionCodecs.GZIP)
        .compact();
  }

  /**
   * 公钥解析token
   *
   * @param token 用户请求中的token
   * @param publicKey 公钥
   * @return Jws<Claims>
   */
  private static Jws<Claims> parserToken(String token, PublicKey publicKey) {
    return Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token);
  }

  private static String createJTI() {
    return new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes()));
  }

  /**
   * 获取token中的用户信息
   *
   * @param token 用户请求中的令牌
   * @param publicKey 公钥
   * @return 用户信息
   */
  public static <T> PayLoad<T> getInfoFromToken(
      String token, PublicKey publicKey, Class<T> userType) throws JsonProcessingException {
    Jws<Claims> claimsJws = parserToken(token, publicKey);
    Claims body = claimsJws.getBody();
    PayLoad<T> claims = new PayLoad<>();
    claims.setId(body.getId());
    claims.setUserInfoDto(objectMapper.readValue(body.get(JWT_PAYLOAD_USERINFO).toString(), userType));
    claims.setExpiration(body.getExpiration());
    return claims;
  }

  /**
   * 获取token中的载荷信息
   *
   * @param token 用户请求中的令牌
   * @param publicKey 公钥
   * @return 用户信息
   */
  public static <T> PayLoad<T> getInfoFromToken(String token, PublicKey publicKey) {
    Jws<Claims> claimsJws = parserToken(token, publicKey);
    Claims body = claimsJws.getBody();
    PayLoad<T> claims = new PayLoad<>();
    claims.setId(body.getId());
    claims.setExpiration(body.getExpiration());
    return claims;
  }
}
