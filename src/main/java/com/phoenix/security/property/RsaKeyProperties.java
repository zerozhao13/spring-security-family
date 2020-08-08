package com.phoenix.security.property;

import com.phoenix.security.util.RsaKeyUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

/** @Author zero 当存在属性phoenix.rsa.enableKey时，返回获取密钥地址 */
//@ConfigurationProperties(prefix = "rsa.key")
// @ConditionalOnProperty("phoenix.rsa.enableKey")
@Data
@Component
public class RsaKeyProperties {
  @Value("${rsa.key.pubKeyLoc}")
  private String pubKeyLoc;
  @Value("${rsa.key.priKeyLoc}")
  private String priKeyLoc;

  private PublicKey publicKey;
  private PrivateKey privateKey;

  @PostConstruct
  public void getRsaKeys() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
    publicKey = RsaKeyUtil.getPublicKeyFromFile(pubKeyLoc);
    privateKey = RsaKeyUtil.getPrivateKeyFromFile(priKeyLoc);
  }
}
