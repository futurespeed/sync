package test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

import org.fs.sync.util.CipherIOUtil;

public class TestCipherIO {
	public static void main(String[] args) throws Exception {
		byte[] key = CipherIOUtil.genKey();
		System.out.println(Arrays.toString(key));
		// byte[] key = new byte[] { 49, 38, -88, -75, 103, -50, 94, -92 }; //
		// 字节数必须是8的整数倍
		// 文件加密
		CipherIOUtil.encode(new FileInputStream("D:/temp/test/test.html"), new FileOutputStream("D:/temp/test/test_enc.html"), key);

		// 文件解密
		CipherIOUtil.decode(new FileInputStream("D:/temp/test/test_enc.html"), new FileOutputStream("D:/temp/test/test_dec.html"),
				key);

	}
}
