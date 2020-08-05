package com.phoenix.security.itf.user;

import com.phoenix.security.dao.UserDao;
import com.phoenix.security.dto.BaseUserDto;

public interface PnxUser {
    UserDao getPnxUserByName(String username);
    UserDao getPnxUserById(Long uid);
    Long createOrUpdateUser(BaseUserDto user);
}
