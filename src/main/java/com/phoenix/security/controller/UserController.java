package com.phoenix.security.controller;

import com.phoenix.security.dao.UserDao;
import com.phoenix.security.dto.OpenIdRolesDto;
import com.phoenix.security.dto.UserInfoDto;
import com.phoenix.security.dto.trans.RespDto;
import com.phoenix.security.service.user.impl.UserDetailsServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController {

  private UserDetailsServiceImpl userDetailsService;

  @GetMapping("")
  List<UserInfoDto> getUserList(
      @RequestParam(name = "fields", required = false) String fields,
      @RequestParam(name = "sort", required = false) String sort,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "10") int size) {
    return null;
  }

  @GetMapping(path = "/{uid}")
  RespDto<UserDao> getUserInfo(@PathVariable Long uid) {
    return RespDto.builder()
            .appid("1")
            .serial("111111")
            .code("200")
            .msg("成功")
            .respData(userDetailsService.getPnxUserById(uid))
            .build();
  }

  /**
   * 这里的路径中带有create不是一个好的例子，我们可以在权限里面更细致地加入请求方式，既路径+请求方式来确定资源权限。
   * @param openId
   * @param openIdRolesDto
   * @return
   */
  @PostMapping(path = "/{openId}/roles/create")
  RespDto<UserDao> createUserRoles(@PathVariable String openId, OpenIdRolesDto openIdRolesDto) {
    return RespDto.builder()
            .appid("1")
            .serial("111111")
            .code("200")
            .msg("成功")
            .respData(userDetailsService.createUserRoles(openIdRolesDto))
            .build();
  }
}
