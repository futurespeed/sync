package org.fs.sync.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class CipherIOUtil {
	/**
	 * DES加密介绍 DES是一种对称加密算法，所谓对称加密算法即：加密和解密使用相同密钥的算法。DES加密算法出自IBM的研究，
	 * 后来被美国政府正式采用，之后开始广泛流传，但是近些年使用越来越少，因为DES使用56位密钥，以现代计算能力，
	 * 24小时内即可被破解。虽然如此，在某些简单应用中，我们还是可以使用DES加密算法，本文简单讲解DES的JAVA实现 。
	 * 注意：DES加密和解密过程中，密钥长度都必须是8的倍数
	 */
	protected static final String ALGORITHM = "DES"; // 定义加密算法,可用DES,DESede,Blowfish

	// static {
	// Security.addProvider(new com.sun.crypto.provider.SunJCE());
	// }

	/**
	 * 生成密钥, 注意此步骤时间比较长
	 * 
	 * @return 密钥
	 */
	public static byte[] genKey() {
		try {
			KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM);
			keygen.init(new SecureRandom());
			SecretKey deskey = keygen.generateKey();
			return deskey.getEncoded();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 加密
	 * 
	 * @param in
	 *            要加密输入流
	 * @param out
	 *            加密后的输出流
	 * @param key
	 *            密钥
	 * @throws Exception
	 */
	public static void encode(InputStream in, OutputStream out, byte[] key) throws Exception {
		// 秘密（对称）密钥(SecretKey继承(key))
		// 根据给定的字节数组构造一个密钥。
		SecretKey deskey = new SecretKeySpec(key, ALGORITHM);
		// 生成一个实现指定转换的 Cipher 对象。Cipher对象实际完成加解密操作
		Cipher c = Cipher.getInstance(ALGORITHM);
		// 用密钥初始化此 cipher
		c.init(Cipher.ENCRYPT_MODE, deskey);

		byte[] buffer = new byte[1024];

		CipherInputStream cin = new CipherInputStream(in, c);
		int i;
		while ((i = cin.read(buffer)) != -1) {
			out.write(buffer, 0, i);
		}
		out.close();
		cin.close();
	}

	/**
	 * 解密
	 * 
	 * @param in
	 *            要解密输入流
	 * @param out
	 *            解密后的输出流
	 * @param key
	 *            密钥
	 * @throws Exception
	 */
	public static void decode(InputStream in, OutputStream out, byte[] key) throws Exception {

		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		// 创建一个 DESKeySpec 对象,指定一个 DES 密钥
		DESKeySpec ks = new DESKeySpec(key);
		// 生成指定秘密密钥算法的 SecretKeyFactory 对象。
		SecretKeyFactory factroy = SecretKeyFactory.getInstance(ALGORITHM);
		// 根据提供的密钥规范（密钥材料）生成 SecretKey 对象,利用密钥工厂把DESKeySpec转换成一个SecretKey对象
		SecretKey sk = factroy.generateSecret(ks);
		// 生成一个实现指定转换的 Cipher 对象。Cipher对象实际完成加解密操作
		Cipher c = Cipher.getInstance(ALGORITHM);
		// 用密钥和随机源初始化此 cipher
		c.init(Cipher.DECRYPT_MODE, sk, sr);

		byte[] buffer = new byte[1024];
		CipherOutputStream cout = new CipherOutputStream(out, c);
		int i;
		while ((i = in.read(buffer)) != -1) {
			cout.write(buffer, 0, i);
		}
		cout.close();
		in.close();
	}
}
