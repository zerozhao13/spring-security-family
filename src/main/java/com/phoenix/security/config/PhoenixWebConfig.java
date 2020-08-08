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
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@Configuration
// @EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 全局
public class PhoenixWebConfig extends WebSecurityConfigurerAdapter {

  @Autowired private UserDetailsServiceImpl userDetailsService;
  @Autowired private PhoenixAuthSuccessFilter phoenixAuthSuccessFilter;
  @Autowired private PhoenixAuthFailHandler phoenixAuthFailHandler;
  // @Autowired private RsaKeyProperties rsaKeyProperties;
  // @Autowired
  // @Resource(name = "PhoenixPostProcessor")
  // private PhoenixPostProcessor phoenixPostProcessor;
  @Autowired private PhoenixAccessDecisionManager phoenixAccessDecisionManager;
  @Autowired private PhoenixSecurityMetadataSource phoenixSecurityMetadataSource;

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // super.configure(auth);
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }

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
        //    .loginPage("/login")
        //    .usernameParameter("username")
        //    .passwordParameter("password")
        .successHandler(phoenixAuthSuccessFilter)
        .failureHandler(phoenixAuthFailHandler)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    // .addFilter(new JwtAuthenticationFilter(authenticationManager(), rsaKeyProperties));
  }
}
