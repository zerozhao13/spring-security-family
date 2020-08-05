package com.phoenix.security.interceptor;

import com.phoenix.security.dto.UserInfoDto;
import com.phoenix.security.entity.PayLoad;
import com.phoenix.security.property.RsaKeyProperties;
import com.phoenix.security.service.permission.impl.PermissionServiceImpl;
import com.phoenix.security.util.JwtUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PhoenixSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

  private RsaKeyProperties rsaKeyProperties;
  private PermissionServiceImpl permissionService;

  @Override
  public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

    FilterInvocation filterInvocation = (FilterInvocation) object;
    HttpServletRequest request = filterInvocation.getRequest();
    String token = request.getHeader("Authorization");
    if (null == token || !token.startsWith("Bearer ")) {
      return null;
    }
    UsernamePasswordAuthenticationToken upt = getUserAuthToken(token.replace("Bearer ", ""));
    List<ConfigAttribute> attrs = getConfigAttributeList(request.getRequestURI());
    return attrs;
  }

  /**
   * @param requestURI
   * @return
   */
  private List<ConfigAttribute> getConfigAttributeList(String requestURI) {
    if (isMatchWhiteList(requestURI)) {
      return null;
    }
    List<Long> ridList = permissionService.getPermListByUri(requestURI);
    if (0 == ridList.size()) {
      List<ConfigAttribute> cfgList = new ArrayList<>();
      cfgList.add(new SecurityConfig("ROLE_DENIED"));
      return cfgList;
    }
    List<ConfigAttribute> cfgList =
        ridList.stream()
            .map(rid -> new SecurityConfig(rid.toString()))
            .collect(Collectors.toList());
    return cfgList;
  }

  /**
   * check whether this path in the white list.
   *
   * @param requestURI
   * @return
   */
  private boolean isMatchWhiteList(String requestURI) {
    return false;
  }

  /**
   * @param token
   * @return
   */
  private UsernamePasswordAuthenticationToken getUserAuthToken(String token) {
    PayLoad<UserInfoDto> payload =
        JwtUtils.getInfoFromToken(token, rsaKeyProperties.getPublicKey(), UserInfoDto.class);
    UserInfoDto userInfo = payload.getUserInfoDto();
    if (null == userInfo) {
      return null;
    }
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
        new UsernamePasswordAuthenticationToken(userInfo, null, userInfo.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    return usernamePasswordAuthenticationToken;
  }

  @Override
  public Collection<ConfigAttribute> getAllConfigAttributes() {
    return null;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return FilterInvocation.class.isAssignableFrom(clazz);
  }
}
