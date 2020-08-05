package com.phoenix.security.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDao extends BaseDao {
  @TableId(type = IdType.ASSIGN_ID)
  Long uid;
  String username;
  String password;
  boolean expired = true;
  boolean locked = false;
  boolean enabled = true;
}
