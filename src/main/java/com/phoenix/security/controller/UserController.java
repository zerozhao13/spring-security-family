package com.phoenix.security.controller;

import com.phoenix.security.dto.UserInfoDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController {

  @GetMapping("/")
  List<UserInfoDto> getUserList(
      @RequestParam(name = "fields", required = false) String fields,
      @RequestParam(name = "sort", required = false) String sort,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "10") int size) {
    return null;
  }

  @GetMapping(path = "/{uid}")
  UserInfoDto getUserInfo(@PathVariable String uid) {
    return null;
  }
}
