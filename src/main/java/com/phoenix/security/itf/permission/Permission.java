package com.phoenix.security.itf.permission;

import com.phoenix.security.dao.PermissionDao;

import java.util.List;

public interface Permission {
    List<?> getPermList();
    Long createOrUpdatePerm(PermissionDao permissionDao);
    List<?> getPermListByUri(String Uri);
}
