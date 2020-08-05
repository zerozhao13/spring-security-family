package com.phoenix.security.service.user.impl;


import com.phoenix.security.dao.RoleDao;
import com.phoenix.security.dao.UserDao;
import com.phoenix.security.dto.BaseUserDto;
import com.phoenix.security.itf.role.Role;
import com.phoenix.security.itf.user.PnxUser;

import java.util.List;

public class RegisterServiceImpl implements PnxUser, Role {
    @Override
    public UserDao getPnxUserByName(String username) {
        return null;
    }

    @Override
    public UserDao getPnxUserById(Long uid) {
        return null;
    }

    @Override
    public Long createOrUpdateUser(BaseUserDto user) {
        return null;
    }

    @Override
    public List<RoleDao> getUserRoles(String openId) {
        return null;
    }

    @Override
    public Long createOrUpdateRole(RoleDao role) {
        return null;
    }

    @Override
    public void createOrUpdateRoles(List<RoleDao> roleList) {

    }
}
