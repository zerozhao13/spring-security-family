package com.phoenix.security.config;

import com.phoenix.security.interceptor.PhoenixAccessDecisionManager;
import com.phoenix.security.interceptor.PhoenixAuthFailHandler;
import com.phoenix.security.interceptor.PhoenixAuthSuccessFilter;
import com.phoenix.security.interceptor.PhoenixSecurityMetadataSource;
import com.phoenix.security.service.user.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class PhoenixWebConfig extends WebSecurityConfigurerAdapter {

  @Autowired private UserDetailsServiceImpl userDetailsService;
  @Autowired private PhoenixAuthSuccessFilter phoenixAuthSuccessFilter;
  @Autowired private PhoenixAuthFailHandler phoenixAuthFailHandler;
  // @Autowired private RsaKeyProperties rsaKeyProperties;
  @Autowired private PhoenixAccessDecisionManager phoenixAccessDecisionManager;
  @Autowired private PhoenixSecurityMetadataSource phoenixSecurityMetadataSource;

  /**
   * 指定密码加密算法
   * @return
   */
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * 指定Auth的UserDetailsService实现类以及密码加密方法
   * @param auth
   * @throws Exception
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // super.configure(auth);
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }

  /**
   * 重写configure方法
   * 关闭csrf及cors拦截，因为我们的请求是来自于其他系统的，不是一个前后一体系统
   * 对于一般请求，通过自定义后置处理器写入配置信息
   * @PhoenixSecurityMetadataSource
   * 在决策处理器中，根据配置信息判断请求是否拦截
   * @PhoenixAccessDecisionManager
   * @param http
   * @throws Exception
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // super.configure(http);
    http.cors()
        .and()
        .csrf()
        .disable()
        .authorizeRequests()
        .withObjectPostProcessor(
            new ObjectPostProcessor<FilterSecurityInterceptor>() {
              @Override
              public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                object.setSecurityMetadataSource(phoenixSecurityMetadataSource);
                object.setAccessDecisionManager(phoenixAccessDecisionManager);
                return object;
              }
            })
        .antMatchers("/login", "/signup", "/swagger-ui.html", "/v2/api-doc")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .formLogin()
        .successHandler(phoenixAuthSuccessFilter)
        .failureHandler(phoenixAuthFailHandler)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    // .addFilter(new JwtAuthenticationFilter(authenticationManager(), rsaKeyProperties));
  }
}
