package com.phoenix.security.interceptor;

import com.phoenix.security.property.RsaKeyProperties;
import com.phoenix.security.service.user.impl.UserDetailsServiceImpl;
import com.phoenix.security.util.JwtUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PhoenixAuthSuccessFilter implements AuthenticationSuccessHandler {
    private UserDetailsServiceImpl userDetailsService;
    private RsaKeyProperties rsaKeyProperties;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails authUser = (UserDetails) authentication.getPrincipal();
        UserDetails userInfoDto = userDetailsService.loadUserByUsername(authUser.getUsername());

        String token = JwtUtils.generateTokenExpireInMinutes(userInfoDto, rsaKeyProperties.getPrivateKey(), 3600*24);
        //将token写入header
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Authorization", "Bearer " + token);
    }
}
