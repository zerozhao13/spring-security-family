package com.phoenix.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserClaim {
    private String openId;
    private String username;
    private List<Long> roles;
}
