package com.phoenix.security.config;

import com.phoenix.security.interceptor.PhoenixAuthFailHandler;
import com.phoenix.security.interceptor.PhoenixAuthSuccessFilter;
import com.phoenix.security.interceptor.PhoenixPostProcessor;
import com.phoenix.security.property.RsaKeyProperties;
import com.phoenix.security.service.user.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class PhoenixWebConfig extends WebSecurityConfigurerAdapter {

  private UserDetailsServiceImpl userDetailsService;
  private PhoenixAuthSuccessFilter phoenixAuthSuccessFilter;
  private PhoenixAuthFailHandler phoenixAuthFailHandler;
  private RsaKeyProperties rsaKeyProperties;
  private PhoenixPostProcessor phoenixPostProcessor;

  public PhoenixWebConfig() {
  }

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
        .antMatchers("login", "signup")
        .permitAll()
        .and()
        .formLogin()
        .successHandler(phoenixAuthSuccessFilter)
        .failureHandler(phoenixAuthFailHandler).withObjectPostProcessor(phoenixPostProcessor);
        //.addFilter(new JwtAuthenticationFilter(authenticationManager(), rsaKeyProperties));
  }
}
