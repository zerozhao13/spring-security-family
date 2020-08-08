package com.phoenix.security.controller.permission;

import com.phoenix.security.dao.PermissionDao;
import com.phoenix.security.dto.permission.PidRidsDto;
import com.phoenix.security.dto.trans.RespDto;
import com.phoenix.security.service.permission.impl.PermissionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/v1/permissions")
public class PermissionController {

  @Autowired
  private PermissionServiceImpl permissionService;

  @GetMapping("")
  RespDto<List<PermissionDao>> getPermList() {
    return RespDto.builder()
        .appid("1")
        .serial("111111")
        .code("200")
        .msg("成功")
        .respData(permissionService.getPermList())
        .build();
  }

  @PostMapping("/create")
  RespDto<PermissionDao> createPermission(HttpServletRequest req, PermissionDao perm) {
    //TODO: 这里有一个问题，这类数据需要谁做了操作，而这个谁需要来自于登录用户，那么这个数据从哪里来呢？从token
    //UserInfoDto userInfoDto = req.getHeader("Authorization");
    return RespDto.builder()
            .appid("1")
            .serial("111111")
            .code("200")
            .msg("成功")
            .respData(permissionService.createOrUpdatePerm(perm))
            .build();
  }

  @PostMapping("/roles/create")
  RespDto<Boolean> createPermRolesRelationship(HttpServletRequest req, PidRidsDto pidRidsDto) {
    return RespDto.builder()
            .appid("1")
            .serial("111111")
            .code("200")
            .msg("成功")
            .respData(permissionService.createPermRoleRelationship(pidRidsDto))
            .build();
  }
}
