package com.phoenix.security.itf.permission;

import com.phoenix.security.dto.permission.PidRidsDto;

import java.util.List;

public interface PermissionRole {
    Boolean createPermRoleRelationship(PidRidsDto pidRidsDto);
    List<?> getPermListByRole(String rid);
}
