package com.phoenix.security.service.permission.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.security.dao.PermissionDao;
import com.phoenix.security.dao.RolePermissionDao;
import com.phoenix.security.dto.permission.PidRidsDto;
import com.phoenix.security.itf.permission.Permission;
import com.phoenix.security.itf.permission.PermissionRole;
import com.phoenix.security.mapper.PermissionMapper;
import com.phoenix.security.mapper.RolePermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements Permission, PermissionRole {

  @Autowired
  private PermissionMapper permissionMapper;
  @Autowired
  private RolePermissionMapper rolePermissionMapper;

  @Override
  public List<PermissionDao> getPermList() {
    return permissionMapper.selectList(null);
  }

  @Override
  @Transactional
  public Boolean createPermRoleRelationship(PidRidsDto pidRidsDto) {
    Long pid = pidRidsDto.getPid();
    List<Long> rids = pidRidsDto.getRid();
    for (Long rid : rids) {
      RolePermissionDao rolePermissionDao = new RolePermissionDao();
      rolePermissionDao.setPid(pid);
      rolePermissionDao.setRid(rid);
      rolePermissionMapper.insert(rolePermissionDao);
    }
    return true;
  }

  @Override
  public List<?> getPermListByRole(String rid) {
    return null;
  }

  @Override
  public Long createOrUpdatePerm(PermissionDao permissionDao) {
    return null;
  }

  @Override
  @Cacheable(value = "permission", key = "#uri")
  public List<Long> getPermListByUri(String uri) {
    PermissionDao permissionDao =
        permissionMapper.selectOne(new QueryWrapper<PermissionDao>().eq("resource", uri));
    List<RolePermissionDao> rolePermissionDaoList =
        rolePermissionMapper.selectList(
            new QueryWrapper<RolePermissionDao>().eq("pid", permissionDao.getPid()));
    return rolePermissionDaoList.stream()
            .map(rolePermissionDao -> rolePermissionDao.getRid())
            .collect(Collectors.toList());
  }
}
