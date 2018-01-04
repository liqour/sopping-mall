package com.shop.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shop.exception.BusinessRuntimeException;

/**
 * 
 * @Description: 加密工具类 
 * @ClassName: EncryptionUtils 
 * @author liquor
 * @date 2017年9月2日 上午9:54:19 
 *
 */
public class EncryptionUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(EncryptionUtils.class);

	/** 
	 * @Description: md5加密
	 * @Title: encryption 
	 * @param @param str
	 * @param @return    设定文件 
	 * @return String    返回类型 
	 * @throws UnsupportedEncodingException 
	 * @throws 
	 * @author liquor
	 */
	public static String EncoderByMd5(String str){
		try {
			//确定计算方法
	        MessageDigest md5=MessageDigest.getInstance("MD5");
	        md5.update(str.getBytes());
	        //加密后的字符串
	        return new BigInteger(1, md5.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			logger.error("md5转化错误");
			e.printStackTrace();
			throw new BusinessRuntimeException("服务器错误");
		}
	}
	
	public static void main(String[] args) {
		System.out.println(EncoderByMd5("123456"));
	}
	
}
