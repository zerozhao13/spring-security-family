package com.phoenix.security.interceptor;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PhoenixAccessDecisionManager implements AccessDecisionManager {
  /**
   * 判断用户是否由权限进行资源访问 如果权限不足则抛出AccessDeniedException
   *
   * @param authentication
   * @param object
   * @param configAttributes
   * @throws AccessDeniedException
   * @throws InsufficientAuthenticationException
   */
  @Override
  public void decide(
      Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
      throws AccessDeniedException, InsufficientAuthenticationException {
    if (!isAccessiable(authentication, configAttributes)) {
      throw new AccessDeniedException("这不是开往幼儿园的车");
    }
  }

  /**
   * 根据auth里取出来的用户role与配置中的uri对应roles进行匹配
   * authentication与configAttributes中数据来自于PhoenixSecurityMetadataSource
   *
   * @param authentication
   * @param configAttributes
   * @return
   */
  private boolean isAccessiable(
      Authentication authentication, Collection<ConfigAttribute> configAttributes) {
    if (null == authentication) {
      return false;
    }
    List<String> userRidList =
        authentication.getAuthorities().stream()
            .map(auth -> auth.getAuthority())
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    List<String> uriRidList =
        configAttributes.stream()
            .map(configAttribute -> configAttribute.getAttribute())
            .distinct()
            .sorted()
            .collect(Collectors.toList());

    for (String userRid : userRidList) {
      if (uriRidList.contains(userRid)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean supports(ConfigAttribute attribute) {
    return false;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return false;
  }
}
