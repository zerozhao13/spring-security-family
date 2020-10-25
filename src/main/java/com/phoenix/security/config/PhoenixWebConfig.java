package com.phoenix.security.config;

import com.phoenix.security.filter.JwtAuthFilter;
import com.phoenix.security.filter.SecurityContextRepository;
import com.phoenix.security.service.user.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class PhoenixWebConfig {

  @Autowired private UserDetailsServiceImpl userDetailsService;
  @Autowired private JwtAuthFilter jwtAuthFilter;
  @Autowired private SecurityContextRepository securityContextRepository;

  /**
   * 指定密码加密算法
   *
   * @return
   */
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * 指定Auth的UserDetailsService实现类以及密码加密方法
   *
   * @param auth
   * @throws Exception
   */
/*
  @Bean
  public ReactiveAuthenticationManager authenticationManager (UserDetailsServiceImpl userDetailsService) throws Exception {
    // super.configure(auth);
    return new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
  }
*/

  /**
   * 重写configure方法 关闭csrf及cors拦截，因为我们的请求是来自于其他系统的，不是一个前后一体系统
   * 对于一般请求，通过自定义后置处理器写入配置信息 @PhoenixSecurityMetadataSource
   * 在决策处理器中，根据配置信息判断请求是否拦截 @PhoenixAccessDecisionManager
   *
   * @param http
   * @throws Exception
   * @return
   */
  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
    // super.configure(http);
    return http.cors()
        .and()
        .csrf()
        .disable()
        .authorizeExchange()
        .pathMatchers("/login", "/signup", "/swagger-ui.html", "/v2/api-doc")
        .permitAll()
        .anyExchange()
        .authenticated()
        .and()
        .addFilterAfter(jwtAuthFilter, SecurityWebFiltersOrder.FIRST)
        .securityContextRepository(securityContextRepository)
        .formLogin().and().build();
    // .addFilter(new JwtAuthenticationFilter(authenticationManager(), rsaKeyProperties));
  }
}
