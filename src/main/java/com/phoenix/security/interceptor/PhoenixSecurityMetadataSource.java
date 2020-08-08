package com.phoenix.security.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phoenix.security.dto.UserClaim;
import com.phoenix.security.entity.PayLoad;
import com.phoenix.security.property.RsaKeyProperties;
import com.phoenix.security.service.permission.impl.PermissionServiceImpl;
import com.phoenix.security.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PhoenixSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
  @Autowired private RsaKeyProperties rsaKeyProperties;
  @Autowired private PermissionServiceImpl permissionService;

  @Override
  public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

    FilterInvocation filterInvocation = (FilterInvocation) object;
    HttpServletRequest request = filterInvocation.getRequest();
    String token = request.getHeader("Authorization");
    //TODO: 这里为了方便登录，没有token默认不拦截，其实应该将没有token与白名单一起校验。
    if (null == token || !token.startsWith("Bearer ")) {
      return null;
    }
    try {
      UsernamePasswordAuthenticationToken upt = getUserAuthToken(token.replace("Bearer ", ""));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return getConfigAttributeList(request.getRequestURI());
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
    return ridList.stream()
        .map(rid -> new SecurityConfig(rid.toString()))
        .collect(Collectors.toList());
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
  private UsernamePasswordAuthenticationToken getUserAuthToken(String token)
      throws JsonProcessingException {
    PayLoad<UserClaim> payload =
        JwtUtils.getInfoFromToken(token, rsaKeyProperties.getPublicKey(), UserClaim.class);
    UserClaim userInfo = payload.getUserInfoDto();
    if (null == userInfo) {
      return null;
    }
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
        new UsernamePasswordAuthenticationToken(
            userInfo,
            null,
            userInfo.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.toString()))
                .collect(Collectors.toList()));
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
