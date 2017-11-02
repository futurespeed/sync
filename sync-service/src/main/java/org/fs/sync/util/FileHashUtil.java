package org.fs.sync.util;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivilegedAction;

public class FileHashUtil {

	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	/***
	 * 计算SHA1码
	 * 
	 * @return String 适用于上G大的文件
	 * @throws NoSuchAlgorithmException
	 */
	public static String getSha1(File file) {
		try {
			MessageDigest messagedigest = MessageDigest.getInstance("SHA-1");
			FileInputStream in = null;
			FileChannel ch = null;
			MappedByteBuffer byteBuffer = null;
			try {
				in = new FileInputStream(file);
				ch = in.getChannel();
				byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
				messagedigest.update(byteBuffer);
				return bufferToHex(messagedigest.digest());
			} finally {
				if(byteBuffer != null){
					clean(byteBuffer);
				}
				if (ch != null) {
					try {
						ch.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (in != null) {
					try {
						in.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}
	
	private static void clean(final Object buffer) throws Exception {
		AccessController.doPrivileged(new PrivilegedAction() {
			public Object run() {
				try {
					Method getCleanerMethod = buffer.getClass().getMethod("cleaner", new Class[0]);
					getCleanerMethod.setAccessible(true);
					sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(buffer, new Object[0]);
					cleaner.clean();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
	}
}
