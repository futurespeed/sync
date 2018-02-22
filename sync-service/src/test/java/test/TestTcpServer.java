package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.fs.sync.transfer.ChannelWriter;
import org.fs.sync.transfer.data.DataFrame;
import org.fs.sync.transfer.data.DataFrameSerializer;
import org.fs.sync.util.FileHashUtil;

import com.alibaba.fastjson.JSON;

public class TestTcpServer {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		
		final int port = 20018;
		
		new Thread() {
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ChannelWriter cw = new ChannelWriter();
				cw.init();
				cw.setIp("127.0.0.1");
				cw.setPort(port);
				cw.open();
				cw.writeFile(UUID.randomUUID().toString().replaceAll("-", ""), "D:/temp/test/test.html", "/temp/test");
				cw.writeFile(UUID.randomUUID().toString().replaceAll("-", ""), "D:/temp/test/test1.js", "/temp/test");
				cw.close();
			};
		}.start();

		ServerSocket ss = new ServerSocket(port);
		Socket s = ss.accept();
		BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF-8"));
		String line = null;
		File file = null;
		FileOutputStream fout = null;
		Map<String, Object> infoMap = null;
		DataFrameSerializer dataFrameSerializer = new DataFrameSerializer();
		while ((line = reader.readLine()) != null) {
//			System.out.println(line);
			DataFrame frame = dataFrameSerializer.deserialize(Base64.decodeBase64(line.getBytes("UTF-8")));
//			System.out.println(frame);
			if (3000 == frame.getType()) {
				if(fout != null){
					fout.close();
					System.out.println(infoMap.get("name") + "," + infoMap.get("hash") + "," + FileHashUtil.getSha1(file));
				}
				infoMap = JSON.parseObject(new String(frame.getData()), Map.class);
				file = new File("D:/" + infoMap.get("path") + "/" + infoMap.get("name") + ".test");
				file.createNewFile();
				fout = new FileOutputStream(file);
			}else if(3001 == frame.getType()){
				fout.write(frame.getData());
			}
		}
		
		if(fout != null){
			fout.close();
			System.out.println(infoMap.get("name") + "," + infoMap.get("hash") + "," + FileHashUtil.getSha1(file));
		}
		
		ss.close();
	}
}
