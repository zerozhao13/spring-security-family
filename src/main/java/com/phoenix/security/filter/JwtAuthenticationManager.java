package com.phoenix.security.filter;

import com.phoenix.security.dto.UserClaim;
import lombok.AllArgsConstructor;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Zero
 * @version 1.0
 * @title: JwtAuthenticationManager
 * @projectName security
 * @description: TODO
 * @date 2020/10/2517:00
 */
@AllArgsConstructor
@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        Boolean isAuthentication = false;

        isAuthentication = isAccessiable(authentication);
        authentication.setAuthenticated(isAuthentication);
        return Mono.just(authentication);
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
            Authentication authentication) {
        if (null == authentication) {
            return false;
        }
        UserClaim userClaim = (UserClaim) authentication.getPrincipal();
        List<Long> urList = userClaim.getRoles();
        List<String> userRidList =
                urList.stream()
                        .map(auth -> auth.toString())
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList());
        List<String> uriRidList =
                authentication.getAuthorities().stream()
                        .map(configAttribute -> configAttribute.getAuthority())
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

}
