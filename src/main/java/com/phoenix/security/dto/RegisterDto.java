package com.phoenix.security.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RegisterDto extends BaseUserDto {
    String password;
    List<RoleDto> roleDtos;
}
