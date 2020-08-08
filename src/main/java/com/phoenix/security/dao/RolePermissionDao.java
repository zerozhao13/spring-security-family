package com.phoenix.security.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("role_permission")
public class RolePermissionDao extends BaseDao {
  private Long rid;
  private Long pid;
}
