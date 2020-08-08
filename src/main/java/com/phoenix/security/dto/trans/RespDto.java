package com.phoenix.security.dto.trans;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
@ApiModel
public class RespDto<T> extends TransDto {
  @ApiModelProperty("返回码")
  private String code;

  @ApiModelProperty("返回消息")
  private String msg;

  @ApiModelProperty("返回数据")
  private T respData;

  private RespDto(
      String appid, String serial, Long timestamp, String code, String msg, T respData) {
    super(appid, serial, timestamp);
    this.code = code;
    this.msg = msg;
    this.respData = respData;
  }

  private RespDto() {}

  public static RespDto.RespDtoBuilder builder() {
    return new RespDto.RespDtoBuilder();
  }

  public static class RespDtoBuilder<T> {
    private String appid;
    private String serial;
    private Long timestamp;
    private String code;
    private String msg;
    private T respData;

    public RespDtoBuilder appid(String appid) {
      this.appid = appid;
      return this;
    }

    public RespDtoBuilder serial(String serial) {
      this.serial = serial;
      return this;
    }

    public RespDtoBuilder timestamp(Long timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public RespDtoBuilder code(String code) {
      this.code = code;
      return this;
    }

    public RespDtoBuilder msg(String msg) {
      this.msg = msg;
      return this;
    }

    public RespDtoBuilder respData(T respData) {
      this.respData = respData;
      return this;
    }

    public RespDto build() {
      return new RespDto(appid, serial, timestamp, code, msg, respData);
    }
  }
}
