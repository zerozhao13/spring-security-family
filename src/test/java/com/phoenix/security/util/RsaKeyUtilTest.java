package com.phoenix.security.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@SpringBootTest
class RsaKeyUtilTest {

  private final static String PRI_KEY_PATH = "D:\\rsa-pri.key";
  private final static String PUB_KEY_PATH = "D:\\rsa-pub.pub";
  private final static String SECURE = "abcd1234";
  private final static int KEY_SIZE = 2048;

  @Test
  void getPublicKeyFromFile() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
    System.out.println(RsaKeyUtil.getPublicKeyFromFile(PUB_KEY_PATH));
  }

  @Test
  void getPrivateKeyFromFile() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
    System.out.println(RsaKeyUtil.getPrivateKeyFromFile(PRI_KEY_PATH));
  }

  @Test
  void generateKeyFiles() throws IOException, NoSuchAlgorithmException {
    RsaKeyUtil.generateKeyFiles(PUB_KEY_PATH, PRI_KEY_PATH, SECURE, KEY_SIZE);
  }

  @Test
  void generateKeyPairMap() {}

  @Test
  void generateKeyPair() {}
}