package com.phoenix.security.util;

import com.phoenix.security.constant.ConstantsInterface;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class RsaKeyUtil {

  private static final int DEFAULT_KEY_SIZE = 2048;
  private static final String KEY_INS = "RSA";

  /**
   * 从文件中读取公钥
   *
   * @param filename 公钥保存路径，相对于classpath
   * @return 公钥对象
   * @throws Exception
   */
  public static PublicKey getPublicKeyFromFile(String filename)
      throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
    byte[] bytes = readFile(filename);
    bytes = Base64.getDecoder().decode(bytes);
    X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
    KeyFactory factory = KeyFactory.getInstance(KEY_INS);
    return factory.generatePublic(spec);
  }

  /**
   * 从文件中读取密钥
   *
   * @param filename 私钥保存路径，相对于classpath
   * @return 私钥对象
   * @throws Exception
   */
  public static PrivateKey getPrivateKeyFromFile(String filename)
      throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
    byte[] bytes = readFile(filename);
    bytes = Base64.getDecoder().decode(bytes);
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
    KeyFactory factory = KeyFactory.getInstance(KEY_INS);
    return factory.generatePrivate(spec);
  }

  /**
   * 根据密文，生存rsa公钥和私钥,并写入指定文件
   *
   * @param publicKeyFilename 公钥文件路径
   * @param privateKeyFilename 私钥文件路径
   * @param secret 生成密钥的密文
   */
  public static void generateKeyFiles(
      String publicKeyFilename, String privateKeyFilename, String secret, int keySize)
      throws IOException, NoSuchAlgorithmException {
    KeyPair keyPair = generateKeyPair(secret, keySize);

    // 将公钥写入文件
    writeFile(publicKeyFilename, keyPair.getPublic().getEncoded());
    // 将私钥写入文件
    writeFile(privateKeyFilename, keyPair.getPrivate().getEncoded());
  }

  /**
   * 获取RSA密钥对字符串
   *
   * @param secret
   * @param keySize
   * @return
   * @throws NoSuchAlgorithmException
   */
  public static Map<String, String> generateKeyPairMap(String secret, int keySize)
      throws NoSuchAlgorithmException {
    KeyPair keyPair = generateKeyPair(secret, keySize);
    Map<String, String> keyPairMap = new HashMap<String, String>();
    keyPairMap.put(ConstantsInterface.RSA_PUB_KEY, keyPair.getPublic().toString());
    keyPairMap.put(ConstantsInterface.RSA_PRI_KEY, keyPair.getPrivate().toString());
    return keyPairMap;
  }
  /**
   * 根据密钥及key长度生成密钥对
   *
   * @param secret
   * @param keySize
   * @return KeyPair 密钥对
   * @throws NoSuchAlgorithmException
   */
  public static KeyPair generateKeyPair(String secret, int keySize)
      throws NoSuchAlgorithmException {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_INS);
    SecureRandom secureRandom = new SecureRandom(secret.getBytes());
    keyPairGenerator.initialize(Math.max(keySize, DEFAULT_KEY_SIZE), secureRandom);
    return keyPairGenerator.genKeyPair();
  }

  private static byte[] readFile(String fileName) throws IOException {
    return Files.readAllBytes(new File(fileName).toPath());
  }

  /**
   * 将RSA密钥对写入文件
   *
   * @param filePath
   * @param keyBytes
   * @throws IOException
   */
  private static void writeFile(String filePath, byte[] keyBytes) throws IOException {
    File dest = new File(filePath);
    if (!dest.exists()) {
      boolean result = dest.createNewFile();
      if (result) {
        keyBytes = Base64.getEncoder().encode(keyBytes);
        Files.write(dest.toPath(), keyBytes);
      }
    }
  }
}
