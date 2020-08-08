package com.phoenix.security.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserClaim {
    private String openId;
    private String username;
    private List<Long> roles;
}
