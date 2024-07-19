package org.pencil.type;

import lombok.extern.slf4j.Slf4j;
import mybatis.mate.annotation.Algorithm;
import mybatis.mate.config.EncryptorProperties;
import mybatis.mate.encrypt.IEncryptor;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.pencil.exception.PencilException;

import javax.annotation.Resource;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author pencil
 * @Date 24/07/20
 */
@Slf4j
public class RSATypeHandler extends BaseTypeHandler<String> {

    @Resource
    private IEncryptor customEncryptor;

    @Resource
    private EncryptorProperties encryptorProperties;

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        // 正式环境打开一下代码执行 RSA 解密明文查询，如果数据库是密文不适用该逻辑（除非加密密文不变）
        if (null != parameter && customEncryptor.executeEncrypt()) {
            try {
                parameter = customEncryptor.encrypt(Algorithm.RSA, encryptorProperties.getPassword(),
                        encryptorProperties.getPrivateKey(), parameter, null);
                ps.setString(i, parameter);
            } catch (Exception e) {
                log.error("RSA 加密失败", e);
                throw PencilException.of("RSA 加密失败");
            }
        }
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return decryptColumn(rs.getString(columnName));
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return decryptColumn(rs.getString(columnIndex));
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return decryptColumn(cs.getString(columnIndex));
    }

    private String decryptColumn(String encryptVal) {

        if (null != encryptVal && customEncryptor.executeDecrypt()) {
            try {
                return customEncryptor.decrypt(Algorithm.RSA, encryptorProperties.getPassword(),
                        encryptorProperties.getPrivateKey(), encryptVal, null);
            } catch (Exception e) {
                log.error("RSA 解密失败", e);
                throw PencilException.of("RSA 解密失败");
            }
        }
        return encryptVal;
    }
}
