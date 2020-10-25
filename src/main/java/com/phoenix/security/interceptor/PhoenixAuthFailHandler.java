//package com.phoenix.security.interceptor;
//
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class PhoenixAuthFailHandler implements AuthenticationFailureHandler {
//  /**
//   * 登录失败，返回未认证错误
//   *
//   * @param request
//   * @param response
//   * @param exception
//   * @throws IOException
//   * @throws ServletException
//   */
//  @Override
//  public void onAuthenticationFailure(
//      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
//      throws IOException, ServletException {
//    response.setContentType("application/json;charset=utf-8");
//    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//  }
//}
