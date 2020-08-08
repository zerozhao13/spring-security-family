package com.phoenix.security.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.stereotype.Component;

public class PhoenixPostProcessor implements ObjectPostProcessor<FilterSecurityInterceptor> {
  @Autowired private PhoenixAccessDecisionManager phoenixAccessDecisionManager;
  @Autowired private PhoenixSecurityMetadataSource phoenixSecurityMetadataSource;

  @Override
  public <O extends FilterSecurityInterceptor> O postProcess(O object) {
    object.setSecurityMetadataSource(phoenixSecurityMetadataSource);
    object.setAccessDecisionManager(phoenixAccessDecisionManager);
    return object;
  }
}
