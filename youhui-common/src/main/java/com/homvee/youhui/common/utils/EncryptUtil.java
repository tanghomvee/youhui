package com.homvee.youhui.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Arrays;

public class EncryptUtil {
	private static final String ALGORITHM = "SHA-256";
	private static  final  String DEFAULT_CHARSET = "UTF-8";
	private static Logger LOGGER = LoggerFactory.getLogger(EncryptUtil.class);
	public static String md5(String str) {
		if (StringUtils.isEmpty(str)) {
			return "";
		}

		String value = null;
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			value = HexStringUtil.bytesToHexString(md5.digest(str.getBytes(DEFAULT_CHARSET))).toUpperCase();
		} catch (Exception ex) {
			LOGGER.error("MD5加密[{}]异常！",  str ,ex);
		}
		return value;
	}


	/**
	 * SHA256加密
	 * @param orignal
	 * @return
     */
	public static String SHA256Encrypt(String orignal) {
		if (StringUtils.isEmpty(orignal)) {
			return null;
		}
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(ALGORITHM);
			byte[] origBytes = orignal.getBytes();
			md.update(origBytes);
			byte[] digestRes = md.digest();
			String digestStr = getDigestStr(digestRes);
			return digestStr;
		} catch (Exception e) {
			LOGGER.error("SHA256加密[{}]异常！",  orignal ,e);
		}

		return null;
	}

	private static String getDigestStr(byte[] origBytes) {
		if (origBytes == null || origBytes.length < 1){
			return null;
		}
		String tempStr = null;
		StringBuilder stb = new StringBuilder();
		for (int i = 0 , len = origBytes.length; i < len; i++) {

			tempStr = Integer.toHexString(origBytes[i] & 0xff);
			if (tempStr.length() == 1) {
				stb.append("0");
			}
			stb.append(tempStr);
		}
		return stb.toString();
	}

	/**
	 * 用SHA1算法生成安全签名
	 * @param token 票据
	 * @param timestamp 时间戳
	 * @param nonce 随机字符串
	 * @param encrypt 密文
	 * @return 安全签名
	 * @throws AesException
	 */
	public static String getSHA1(String token, String timestamp, String nonce, String encrypt) throws AesException{
		try {
			String[] array = new String[] { token, timestamp, nonce, encrypt };
			StringBuffer sb = new StringBuffer();
			// 字符串排序
			Arrays.sort(array);
			for (int i = 0; i < 4; i++) {
				sb.append(array[i]);
			}
			String str = sb.toString();
			// SHA1签名生成
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(str.getBytes());
			byte[] digest = md.digest();

			StringBuffer hexstr = new StringBuffer();
			String shaHex = "";
			for (int i = 0; i < digest.length; i++) {
				shaHex = Integer.toHexString(digest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexstr.append(0);
				}
				hexstr.append(shaHex);
			}
			return hexstr.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new AesException(AesException.ComputeSignatureError);
		}
	}

}
