package com.phoenix.security.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.security.dao.OpenIdRoleDao;
import com.phoenix.security.dao.RoleDao;
import com.phoenix.security.dao.UserDao;
import com.phoenix.security.dao.UserOpenIdDao;
import com.phoenix.security.dto.BaseUserDto;
import com.phoenix.security.dto.RegisterDto;
import com.phoenix.security.dto.RoleDto;
import com.phoenix.security.itf.role.Role;
import com.phoenix.security.itf.user.PnxUser;
import com.phoenix.security.mapper.OpenIdRoleMapper;
import com.phoenix.security.mapper.UserMapper;
import com.phoenix.security.mapper.UserOpenIdMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegisterServiceImpl implements PnxUser, Role {

  @Autowired private UserMapper userMapper;
  @Autowired private UserOpenIdMapper userOpenIdMapper;
  @Autowired private OpenIdRoleMapper openIdRoleMapper;

  @Override
  public UserDao getPnxUserByName(String username) {
    return userMapper.selectOne(new QueryWrapper<UserDao>().eq("username", username));
  }

  @Override
  public UserDao getPnxUserById(Long uid) {
    return null;
  }

  @Override
  @Transactional
  public Long createOrUpdateUser(BaseUserDto user) {
    RegisterDto registerDto = (RegisterDto) user;
    UserDao userDao = getPnxUserByName(registerDto.getUsername());
    if (null != userDao) {
      throw new RuntimeException("这个世界不能存在分身，请重新设置用户名。");
    }
    userDao = new UserDao();
    userDao.setUsername(registerDto.getUsername());
    userDao.setPassword(new BCryptPasswordEncoder().encode(registerDto.getPassword()));
    userMapper.insert(userDao);
    String openid = createOpenId(userDao.getUid(), registerDto.getAppId());
    createOrUpdateUserRoles(openid, getRoleDaos(registerDto.getRoleDtos()));
    return userDao.getUid();
  }

  private List<RoleDao> getRoleDaos(List<RoleDto> roleDtos) {
    return roleDtos.stream().map(roleDto -> (RoleDao) roleDto).collect(Collectors.toList());
  }

  private String createOpenId(Long uid, Long appid) {
    UserOpenIdDao userOpenIdDao = new UserOpenIdDao();
    userOpenIdDao.setUid(uid);
    userOpenIdDao.setAppId(appid);
    userOpenIdMapper.insert(userOpenIdDao);
    return userOpenIdDao.getOpenId();
  }

  @Override
  public List<RoleDao> getUserRoles(String openId) {
    return null;
  }

  @Override
  public Long createOrUpdateRole(RoleDao role) {
    return null;
  }

  @Override
  public void createOrUpdateRoles(List<RoleDao> roleList) {}

  @Override
  public void createOrUpdateUserRoles(String openId, List<RoleDao> roleList) {
    roleList.stream()
        .map(
            roleDao -> {
              return openIdRoleMapper.insert(
                  OpenIdRoleDao.builder().rid(roleDao.getRid()).openId(openId).build());
            });
  }
}
