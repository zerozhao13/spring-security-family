package com.phoenix.security.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.security.dao.OpenIdRoleDao;
import com.phoenix.security.dao.RoleDao;
import com.phoenix.security.dao.UserDao;
import com.phoenix.security.dao.UserOpenIdDao;
import com.phoenix.security.dto.BaseUserDto;
import com.phoenix.security.dto.UserInfoDto;
import com.phoenix.security.enums.UserRespMsg;
import com.phoenix.security.itf.role.Role;
import com.phoenix.security.itf.user.PnxUser;
import com.phoenix.security.mapper.OpenIdRoleMapper;
import com.phoenix.security.mapper.UserMapper;
import com.phoenix.security.mapper.UserOpenIdMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLDataException;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, PnxUser, Role {
  @Autowired private UserMapper userMapper;
  @Autowired private UserOpenIdMapper userOpenIdMapper;
  @Autowired private OpenIdRoleMapper openIdRoleMapper;

  @Override
  public List<RoleDao> getUserRoles(String openId) {
    return null;
  }

  @Override
  public Long createOrUpdateRole(RoleDao role) {
    return null;
  }

  @Override
  public void createOrUpdateRoles(List<RoleDao> roleList) {

  }

  @Override
  public UserDao getPnxUserByName(String username) {
    return null;
  }

  @Override
  public UserDao getPnxUserById(Long uid) {
    return null;
  }

  @Override
  @Transactional(rollbackFor = SQLDataException.class)
  public Long createOrUpdateUser(BaseUserDto user) {
    return null;
  }

  @Override
  @Cacheable(value = "userInfo", key = "#username")
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserDao userDao = userMapper.selectOne(new QueryWrapper<UserDao>().eq("username", username));
    if (null == userDao) {
      throw new UsernameNotFoundException(UserRespMsg.NOT_EXIST.getMsg());
    }
    UserOpenIdDao userOpenIdDao =
        userOpenIdMapper.selectOne(new QueryWrapper<UserOpenIdDao>().eq("uid", userDao.getUid()));
    List<OpenIdRoleDao> openIdRoleDaoList =
        openIdRoleMapper.selectList(
            new QueryWrapper<OpenIdRoleDao>().eq("openId", userOpenIdDao.getOpenId()));
    return new UserInfoDto(userDao, userOpenIdDao, openIdRoleDaoList);
  }
}
