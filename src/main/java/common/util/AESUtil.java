/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package common.util;

import java.security.Key;
import java.security.SecureRandom;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AESUtil {

	/**
	 * 统一字符编码集
	 */
	public static final String CHARSET_NAME = "UTF-8";

	/**
	 * 秘钥算法
	 */
	public static final String KEY_ALG = "AES";

	/**
	 * 加密/解密算法 工作模式 填充方式
	 */
	public static final String CIPHER_ALG = "AES/ECB/PKCS5Padding";

	/**
	 * 转换密钥
	 *
	 * @param key
	 *            二进制密钥
	 * @return
	 */
	private static Key toKey(byte[] key) {
		return new SecretKeySpec(key, KEY_ALG);
	}

	/**
	 * 解密
	 *
	 * @param data
	 *            待解密数据
	 * @param key
	 *            密钥
	 * @return 解密数据
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, byte[] key) {
		try {
			Key k = toKey(key);
			Cipher cipher = Cipher.getInstance(CIPHER_ALG);
			cipher.init(Cipher.DECRYPT_MODE, k);
			return cipher.doFinal(data);
		} catch (Exception e) {
			System.err.println(e);
		}
		return null;
	}

	/**
	 * 解密
	 *
	 * @param cipherText
	 *            密文，待解密数据(Base64编码后的字符串数据)
	 * @param key
	 *            密钥
	 * @return 解密后字符串数据
	 * @throws Exception
	 */
	public static String decrypt(String cipherText, String key) {
		try {
			byte[] decryptData = decrypt(Base64.decodeBase64(cipherText), key.getBytes(CHARSET_NAME));
			return new String(decryptData, CHARSET_NAME);
		} catch (Exception e) {
			System.err.println(e);
		}
		return null;
	}

	/**
	 * 加密
	 *
	 * @param plainText
	 *            明文，待加密数据
	 * @param key
	 *            密钥
	 * @return 加密数据
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] plainText, byte[] key) {
		try {
			Key k = toKey(key);
			Cipher cipher = Cipher.getInstance(CIPHER_ALG);
			cipher.init(Cipher.ENCRYPT_MODE, k);
			return cipher.doFinal(plainText);
		} catch (Exception e) {
			System.err.println(e);
		}
		return null;
	}

	/**
	 * 加密
	 *
	 * @param plainText
	 *            明文，待加密数据
	 * @param key
	 *            密钥
	 * @return 加密数据
	 * @throws Exception
	 */
	public static String encrypt(String plainText, String key) {
		try {
			byte[] result = encrypt(plainText.getBytes(CHARSET_NAME), key.getBytes(CHARSET_NAME));

			return Base64.encodeBase64String(result);
		} catch (Exception e) {
			System.err.println(e);
		}
		return null;

	}

	/**
	 * 生成密钥
	 * <p/>
	 * 秘钥长度为：128位
	 *
	 * @return
	 */
	public static String generateKey() {
		String uuid = UUID.randomUUID().toString();

		String uuidWithoutBar = uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18)
				+ uuid.substring(19, 23) + uuid.substring(24);

		return uuidWithoutBar.substring(0, 16);
	}

	public static void main(String[] args) {
		// 卡号
		String plainText = "6130123456789";
		// 秘钥
		String key = "6475ff6c37cf4751";

		// 加密
		String cipherText = AESUtil.encrypt(plainText, key);
		System.out.println("加密后:" + cipherText);

		System.out.println(AESUtil.decrypt(cipherText, key));

		System.out.println(generateKey());

	}

}
