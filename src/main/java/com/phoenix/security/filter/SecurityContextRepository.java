package com.phoenix.security.filter;

import com.phoenix.security.dto.UserClaim;
import com.phoenix.security.entity.PayLoad;
import com.phoenix.security.property.RsaKeyProperties;
import com.phoenix.security.service.permission.impl.PermissionServiceImpl;
import com.phoenix.security.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Zero
 * @version 1.0
 * @title: SecurityContextRepository
 * @projectName security
 * @description: TODO
 * @date 2020/10/2516:58
 */
public class SecurityContextRepository implements ServerSecurityContextRepository {
  @Autowired private JwtAuthenticationManager jwtAuthenticationManager;
  @Autowired private RsaKeyProperties rsaKeyProperties;
  @Autowired private PermissionServiceImpl permissionService;

  @Override
  public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
    return null;
  }

  @Override
  public Mono<SecurityContext> load(ServerWebExchange exchange) {
    PayLoad<UserClaim> payload =
        JwtUtils.getInfoFromToken(
            exchange.getAttribute("token"), rsaKeyProperties.getPublicKey(), UserClaim.class);
    UserClaim userInfo = payload.getUserInfoDto();

    List<String> configList = getConfigAttributeList(exchange.getRequest().getURI().getPath());

    if (null == userInfo) {
      return null;
    }
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
        new UsernamePasswordAuthenticationToken(
            userInfo,
            null,
            configList.stream()
                .map(role -> new SimpleGrantedAuthority(role.toString()))
                .collect(Collectors.toList()));

    return jwtAuthenticationManager
        .authenticate(usernamePasswordAuthenticationToken)
        .map(SecurityContextImpl::new);
  }

  /**
   * @param requestURI
   * @return
   */
  private List<String> getConfigAttributeList(String requestURI) {
    if (isMatchWhiteList(requestURI)) {
      return null;
    }
    List<Long> ridList = permissionService.getPermListByUri(requestURI);
    if (0 == ridList.size()) {
      List<String> cfgList = new ArrayList<>();
      cfgList.add("ROLE_DENIED");
      return cfgList;
    }
    return ridList.stream().map(rid -> rid.toString()).collect(Collectors.toList());
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
}
