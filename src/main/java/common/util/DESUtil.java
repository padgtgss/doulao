package common.util;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DESUtil {

	private static Log logger = LogFactory.getLog(DESUtil.class);

	public static byte[] encrypt(String data, String key) {
		try {
			return encrypt(data.getBytes("UTF-8"), key.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 加密函数
	 *
	 * @param data
	 *            加密数据
	 * @param key
	 *            密钥
	 * @return 加密后的数据
	 */
	public static byte[] encrypt(byte[] data, byte[] key) {
		try {
			// DES算法要求有一个可信任的随机数源
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(key);

			// 从原始密钥数据创建DESKeySpec对象
			DESKeySpec dks = new DESKeySpec(key);

			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			// 一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(dks);

			// using DES in ECB mode
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, secureRandom);

			// 执行加密操作
			byte encryptedData[] = cipher.doFinal(data);

			return encryptedData;
		} catch (Exception e) {
			logger.error("encrypt error:" + e);
			e.printStackTrace();
		}

		return null;
	}

	public static byte[] decrypt(String data, String key) {
		try {
			return decrypt(data.getBytes("UTF-8"), key.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密函数
	 *
	 * @param data
	 *            解密数据
	 * @param key
	 *            密钥
	 * @return 解密后的数据
	 */
	public static byte[] decrypt(byte[] data, byte[] key) {
		try {
			// DES算法要求有一个可信任的随机数源
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(key);

			// 从原始密匙数据创建一个DESKeySpec对象
			DESKeySpec dks = new DESKeySpec(key);

			// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
			// 一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(dks);

			// using DES in ECB mode
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

			// 用密匙初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, secretKey, secureRandom);

			// 正式执行解密操作
			byte decryptedData[] = cipher.doFinal(data);

			return decryptedData;
		} catch (Exception e) {
			logger.error("decrypt error:" + e);
			e.printStackTrace();
		}

		return null;
	}
}
