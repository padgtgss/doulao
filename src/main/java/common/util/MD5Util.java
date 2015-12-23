/*
 * 文件名：MD5Util.java
 * 版权：Copyright (c) 2014, doit.com All Rights Reserved. 
 * 描述：TODO
 * 修改人：Estn
 * 修改时间：2014-3-1 下午3:41:10
 */
package common.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

	/**
	 * MD5普通加密
	 *
	 * @param rawPass
	 *            明文
	 * @return
	 */
	public static String encrypt(String rawPass) {
		return encrypt(rawPass, null);
	}

	/**
	 * * MD5盐值加密
	 *
	 * @param rawPass
	 *            明文
	 * @param salt
	 *            盐值
	 * @return
	 */
	public static String encrypt(String rawPass, Object salt) {
		String saltedPassword = mergePasswordAndSalt(rawPass, salt);
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] digest = messageDigest.digest(saltedPassword.getBytes("UTF-8"));
			return new String(encode(digest));
		} catch (Exception e) {
			return rawPass;
		}
	}

	/**
	 * 拼接密码与盐值
	 *
	 * @param password
	 * @param salt
	 * @return 密码{盐值}
	 */
	private static String mergePasswordAndSalt(String password, Object salt) {
		if (salt == null || "".equals(salt.toString().trim())) {
			return password;
		}
		return password + salt.toString() ;
	}

	/**
	 * encrypt
	 *
	 * @param bytes
	 * @return
	 */
	private static char[] encode(byte[] bytes) {
		char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		int nBytes = bytes.length;
		char[] result = new char[2 * nBytes];
		int j = 0;
		for (byte aByte : bytes) {
			result[j++] = HEX[(0xF0 & aByte) >>> 4];
			result[j++] = HEX[(0x0F & aByte)];
		}
		return result;
	}

	/**
	 * 校验明文加验证MD5后的值是否等于密文
	 *
	 * @param plainText
	 *            明文
	 * @param salt
	 *            盐值
	 * @param cipherText
	 *            密文
	 * @return
	 */
	public static boolean verify(String plainText, String salt, String cipherText) {
		return (MD5Util.encrypt(plainText, salt)).equals(cipherText);
	}

	public static String string2MD5(String inStr){
		MessageDigest md5 = null;
		try{
			md5 = MessageDigest.getInstance("MD5");
		}catch (Exception e){
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++){
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}
	
	/**
	 * MD5加密
	 * @param s
	 * @return
	 */
	public String getMD5(String s){
		//s = s+"X7J28D9CK3ysltllb";
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.update(s.getBytes());
		String ss = new BigInteger(1, md.digest()).toString(16);
		if(ss.length()!=32){
			int sx = 32-ss.length();
			String bl = "";
			for(int i = 0 ; i<sx;i++){
				bl = "0"+bl;
			}
			ss = bl+ss;
		}
		return ss;
	}



	public static void main(String[] args){
		String s = encrypt("123456");
		System.out.print(s);

	}
}