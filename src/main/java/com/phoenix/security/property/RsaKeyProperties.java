package com.phoenix.security.property;

import com.phoenix.security.util.RsaKeyUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

/**
 * @Author zero
 * 当存在属性phoenix.rsa.enableKey时，返回获取密钥地址
 */
@ConfigurationProperties(prefix = "phoenix.rsa")
@ConditionalOnProperty("phoenix.rsa.enableKey")
@Getter
@Setter
public class RsaKeyProperties {
    Boolean enableKey;
    String pubKeyLoc;
    String priKeyLoc;

    PublicKey publicKey;
    PrivateKey privateKey;

    @PostConstruct
    public void getRsaKeys() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        publicKey = RsaKeyUtil.getPublicKeyFromFile(pubKeyLoc);
        privateKey = RsaKeyUtil.getPrivateKeyFromFile(priKeyLoc);
    }
}
