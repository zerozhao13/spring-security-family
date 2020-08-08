package com.phoenix.security.controller;

import com.phoenix.security.dto.RegisterDto;
import com.phoenix.security.dto.trans.RespDto;
import com.phoenix.security.service.user.impl.RegisterServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("用户注册、登录、注销")
public class SigninController {
  @Autowired
  private RegisterServiceImpl registerService;

/*  @PostMapping("/login")
  RespDto<Long> login(RegisterDto registerDto) {
    return RespDto.builder()
        .appid("111")
        .code("0000")
        .msg("成功")
        .serial("123456")
        .respData(registerService.createOrUpdateUser(registerDto))
        .build();
  }*/

  @ApiOperation("用户注册")
  @ApiImplicitParam(name = "registerDto", dataType = "RegisterDto")
  @PostMapping("/signup")
  RespDto<Long> signup(@RequestBody RegisterDto registerDto) {
    return RespDto.builder()
        .appid("111")
        .code("0000")
        .msg("成功")
        .serial("123456")
        .respData(registerService.createOrUpdateUser(registerDto))
        .build();
  }
}
