package com.phoenix.security.dao;

import lombok.Data;

@Data
public class RolePermissionDao extends BaseDao {
    private Long rid;
    private Long pid;
}
