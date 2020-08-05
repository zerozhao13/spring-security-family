package com.phoenix.security.itf.role;

import com.phoenix.security.dao.RoleDao;

import java.util.List;

public interface Role {
    List<RoleDao> getUserRoles(String openId);
    Long createOrUpdateRole(RoleDao role);
    void createOrUpdateRoles(List<RoleDao> roleList);
}
