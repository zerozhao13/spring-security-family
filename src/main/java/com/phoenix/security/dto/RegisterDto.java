package com.phoenix.security.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@Getter
@ApiModel
@Component
public class RegisterDto extends BaseUserDto {
  @ApiModelProperty("密码")
  String password;

  @ApiModelProperty("一组角色数据")
  List<RoleDto> roleDtos;
}
