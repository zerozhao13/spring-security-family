package com.phoenix.security.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDao extends BaseDao {
  @TableId(type = IdType.ASSIGN_ID)
  Long rid;
  String roleName;
  String des;
}
