/*
package com.phoenix.security.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenix.security.dto.UserInfoDto;
import com.phoenix.security.property.RsaKeyProperties;
import com.phoenix.security.service.user.impl.UserDetailsServiceImpl;
import com.phoenix.security.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RsaKeyProperties rsaKeyProperties;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, RsaKeyProperties rsaKeyProperties) {
        this.authenticationManager = authenticationManager;
        this.rsaKeyProperties = rsaKeyProperties;
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            //从输入流中获取用户名和密码，而不是表单
            UserInfoDto userInfoDto = new ObjectMapper().readValue(request.getInputStream(), UserInfoDto.class);
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(userInfoDto.getUsername(), userInfoDto.getPassword());
            return authenticationManager.authenticate(authRequest);
        } catch (Exception e) {
            try {
                //处理失败请求
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                PrintWriter out = response.getWriter();
//                Map map = new HashMap<>();
//                map.put("code", HttpServletResponse.SC_UNAUTHORIZED);
//                map.put("msg", "用户名或者密码错误");
//                out.write(new ObjectMapper().writeValueAsString(map));
//                out.flush();
//                out.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            throw new RuntimeException(e);
        }
    }

    //重写用户名密码授权成功操作----返回token凭证
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //从authResult获取认证成功的用户信息
        UserInfoDto authUser = (UserInfoDto) authResult.getPrincipal();
        UserDetails userInfoDto = userDetailsService.loadUserByUsername(authUser.getUsername());

        String token = JwtUtils.generateTokenExpireInMinutes(userInfoDto, rsaKeyProperties.getPrivateKey(), 3600*24);
        //将token写入header
        response.addHeader("Authorization", "Bearer " + token);
        try {
            //登录成功時，返回json格式进行提示
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
//            PrintWriter out = response.getWriter();
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("code", HttpServletResponse.SC_OK);
//            map.put("message", "登陆成功！");
//            out.write(new ObjectMapper().writeValueAsString(map));
//            out.flush();
//            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/
