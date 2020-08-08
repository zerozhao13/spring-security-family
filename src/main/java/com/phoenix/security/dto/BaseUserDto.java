package com.phoenix.security.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@ApiModel
@Component
public class BaseUserDto {
    @ApiModelProperty("应用id")
    Long appId;
    @ApiModelProperty("用户名")
    String username;
}
