package com.phoenix.security.itf.permission;

import com.phoenix.security.dao.PermissionDao;

import java.util.List;

public interface Permission {
    List<?> getPermListByRole(String rid);
    Long createOrUpdatePerm(PermissionDao permissionDao);
    List<?> getPermListByUri(String Uri);
}
