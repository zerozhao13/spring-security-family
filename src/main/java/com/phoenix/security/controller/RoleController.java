package com.phoenix.security.controller;

import com.phoenix.security.dto.RoleDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/role")
public class RoleController {
  @GetMapping("/")
  List<RoleDto> getRoleList(
      @RequestParam(name = "fields", required = false) String fields,
      @RequestParam(name = "sort", required = false) String sort,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "10") int size) {
    return null;
  }
}
