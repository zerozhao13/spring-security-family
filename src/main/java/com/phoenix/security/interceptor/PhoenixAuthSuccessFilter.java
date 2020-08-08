package com.phoenix.security.interceptor;

import com.phoenix.security.dto.UserClaim;
import com.phoenix.security.dto.UserInfoDto;
import com.phoenix.security.property.RsaKeyProperties;
import com.phoenix.security.service.user.impl.UserDetailsServiceImpl;
import com.phoenix.security.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class PhoenixAuthSuccessFilter implements AuthenticationSuccessHandler {
  @Autowired private UserDetailsServiceImpl userDetailsService;
  @Autowired private RsaKeyProperties rsaKeyProperties;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    UserInfoDto authUser = (UserInfoDto) authentication.getPrincipal();
    // UserInfoDto userInfoDto = (UserInfoDto)
    // userDetailsService.loadUserByUsername(authUser.getUser().getUsername());
    UserClaim claims = getClaimUserInfo(authUser);
    String token =
        JwtUtils.generateTokenExpireInMinutes(claims, rsaKeyProperties.getPrivateKey(), 3600 * 24);
    // 将token写入header
    response.setContentType("application/json;charset=utf-8");
    response.setStatus(HttpServletResponse.SC_OK);
    response.addHeader("Authorization", "Bearer " + token);
  }

  private UserClaim getClaimUserInfo(UserInfoDto authUser) {
    UserClaim claims = new UserClaim();
    claims.setOpenId(authUser.getOpenId());
    claims.setUsername(authUser.getUser().getUsername());
    claims.setRoles(authUser.getRoles());
    return claims;
  }
}
