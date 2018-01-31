package com.shop.util;

public class ByteUtil {

	public static byte[] unitByteArray(byte[] b1,byte[] b2){
		byte[] b = new byte[b1.length+b2.length];
		System.arraycopy(b1, 0, b, 0, b1.length);
		System.arraycopy(b2, 0, b, b1.length, b2.length);
		return b;
	}
	
}
