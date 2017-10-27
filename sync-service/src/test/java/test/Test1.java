package test;

import org.apache.commons.codec.binary.Base64;

public class Test1 {
	public static void main(String[] args) throws Exception {
		String str = "2343xvxcv";
		System.out.println(Base64.encodeBase64String(str.getBytes("UTF-8")));
		System.out.println(new String(Base64.decodeBase64(Base64.encodeBase64String(str.getBytes("UTF-8"))), "UTF-8"));
	}
}
