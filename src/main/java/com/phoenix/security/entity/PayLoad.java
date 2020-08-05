package com.phoenix.security.entity;

import lombok.Data;

import java.util.Date;

@Data
public class PayLoad<T> {
    private String id;
    private String issuer;
    private T userInfoDto;
    private Date expiration;
}
