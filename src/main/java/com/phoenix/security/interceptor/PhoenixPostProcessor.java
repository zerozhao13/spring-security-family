package com.phoenix.security.interceptor;

import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

public class PhoenixPostProcessor implements ObjectPostProcessor<FilterSecurityInterceptor> {
    private PhoenixAccessDecisionManager phoenixAccessDecisionManager;
    private PhoenixSecurityMetadataSource phoenixSecurityMetadataSource;
    @Override
    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
        object.setSecurityMetadataSource(phoenixSecurityMetadataSource);
        object.setAccessDecisionManager(phoenixAccessDecisionManager);
        return object;
    }
}
