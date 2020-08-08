package com.phoenix.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.phoenix.security.dao.OpenIdRoleDao;
import com.phoenix.security.dao.UserDao;
import com.phoenix.security.dao.UserOpenIdDao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoDto implements UserDetails {
  @ApiModelProperty("open id")
  private String openId;

  @ApiModelProperty("用户数据")
  private UserDao user;

  @ApiModelProperty("一组角色id")
  private List<Long> roles;

  public UserInfoDto(
      UserDao userDao, UserOpenIdDao userOpenIdDao, List<OpenIdRoleDao> roleDaoList) {
    this.openId = userOpenIdDao.getOpenId();
    this.user = userDao;
    this.roles = getRoleString(roleDaoList);
  }

  private List<Long> getRoleString(List<OpenIdRoleDao> roleDaoList) {
    return roleDaoList.stream().map(OpenIdRoleDao::getRid).collect(Collectors.toList());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles.stream()
        .map(role -> new SimpleGrantedAuthority(role.toString()))
        .collect(Collectors.toList());
  }

  public UserDao getUser() {
    return user;
  }

  public void setUser(UserDao user) {
    this.user = user;
  }

  @Override
  public String getPassword() {
    return this.user.getPassword();
  }

  @Override
  public String getUsername() {
    return this.user.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return this.user.isExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return this.user.isLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return this.user.isExpired();
  }

  @Override
  public boolean isEnabled() {
    return this.user.isEnabled();
  }
}
