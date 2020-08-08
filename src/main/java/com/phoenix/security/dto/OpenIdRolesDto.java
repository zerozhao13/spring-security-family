package com.phoenix.security.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class OpenIdRolesDto {
    @ApiModelProperty("用户渠道id")
    private String openId;
    @ApiModelProperty("一组角色id")
    private List<Long> rids;
}
