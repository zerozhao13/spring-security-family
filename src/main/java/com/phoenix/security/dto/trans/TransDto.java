package com.phoenix.security.dto.trans;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class TransDto {
    @ApiModelProperty("应用id")
    private String appid;
    @ApiModelProperty("流水号")
    private String serial;
    @ApiModelProperty("请求时间")
    private Long timestamp;
}
