package com.phoenix.security.dto.permission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class PidRidsDto {
    @ApiModelProperty("权限id")
    private Long pid;
    @ApiModelProperty("一组角色id")
    private List<Long> rid;
}
