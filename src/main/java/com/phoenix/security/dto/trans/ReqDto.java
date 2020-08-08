package com.phoenix.security.dto.trans;

import lombok.Getter;

@Getter
public class ReqDto<T> extends TransDto {
    private T reqData;
}
