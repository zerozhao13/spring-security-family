package com.phoenix.security.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@TableName("user")
@NoArgsConstructor
@AllArgsConstructor
public class UserDao extends BaseDao {
  @TableId(type = IdType.ASSIGN_ID)
  Long uid;
  String username;
  String password;
  boolean expired = true;
  boolean locked = false;
  boolean enabled = true;

  public UserDao(UserDao userDao) {
    this.uid = userDao.uid;
    this.username = userDao.username;
    this.password = userDao.password;
    this.expired = userDao.expired;
    this.locked = userDao.locked;
    this.enabled = userDao.enabled;
  }
}
