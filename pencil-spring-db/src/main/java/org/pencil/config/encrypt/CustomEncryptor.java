package org.pencil.config.encrypt;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import mybatis.mate.annotation.Algorithm;
import mybatis.mate.config.EncryptorProperties;
import mybatis.mate.encrypt.IEncryptor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author pencil
 * @Date 24/07/19
 */
public class CustomEncryptor implements IEncryptor {

    @Resource
    private EncryptorProperties encryptorProperties;

    private RSA rsa;

    @PostConstruct
    private void init() {
        rsa = SecureUtil.rsa(encryptorProperties.getPrivateKey(), encryptorProperties.getPublicKey());
    }

    /**
     * 加密
     *
     * @param algorithm  算法
     * @param password   密码（对称加密算法密钥）
     * @param plaintext  明文
     * @param publicKey  非对称加密算法（公钥）
     * @param metaObject {@link org.apache.ibatis.reflection.MetaObject}
     * @return
     */
    @Override
    public String encrypt(Algorithm algorithm, String password, String publicKey, String plaintext, Object metaObject) {
        // 这里可以对plaintext进行自定义加密操作,并将结果返回
        byte[] encrypt = rsa.encrypt(plaintext, KeyType.PrivateKey);
        return Base64Encoder.encode(encrypt);
    }

    /**
     * 解密
     *
     * @param algorithm  算法
     * @param password   密码（对称加密算法密钥）
     * @param encrypt    密文
     * @param privateKey 非对称加密算法（私钥）
     * @param metaObject {@link org.apache.ibatis.reflection.MetaObject}
     * @return
     */
    @Override
    public String decrypt(Algorithm algorithm, String password, String privateKey, String encrypt, Object metaObject) {
        // 这里可以对encrypt进行自定义解密操作,并将结果返回
        byte[] decode = Base64Decoder.decode(encrypt);
        byte[] decrypt = rsa.decrypt(decode, KeyType.PrivateKey);
        return new String(decrypt, StandardCharsets.UTF_8);
    }
}
