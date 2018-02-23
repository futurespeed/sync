package org.fs.sync.transfer;

import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.fs.sync.transfer.data.DataFrame;
import org.fs.sync.transfer.data.DataFrameSerializer;
import org.fs.sync.transfer.handler.FrameWriteHandler;
import org.fs.sync.util.FileHashUtil;

import com.alibaba.fastjson.JSON;

public class ChannelWriter {
	
	private String ip = "127.0.0.1";
	
	private int port = 20008;
	
	private String userId;
	
	private String token;
	
	private String configId;
	
	private Socket socket;
	
	private FrameWriteHandler frameWriteHandler;
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public void init(){
		frameWriteHandler = new FrameWriteHandler();
		frameWriteHandler.setSerializer(new DataFrameSerializer());
	}
	
	public void open(){
		try {
			Map<String, Object> infoMap = new HashMap<String, Object>();
			infoMap.put("type", "writer");
			infoMap.put("userId", userId);
			infoMap.put("configId", configId);
			infoMap.put("token", token);
			socket = new Socket(ip, port);
			socket.getOutputStream().write((JSON.toJSONString(infoMap) + "\n").getBytes("UTF-8"));
			frameWriteHandler.setSocket(socket);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void close(){
		try {
			frameWriteHandler.release();
			socket.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void writeFile(String dataId, String filePath, String path){
		FileInputStream in = null;
		try{
			File file = new File(filePath);
			String hash = FileHashUtil.getSha1(file);
			Map<String, Object> infoMap = new HashMap<String, Object>();
			infoMap.put("userId", userId);
			infoMap.put("configId", configId);
			infoMap.put("name", file.getName());
			infoMap.put("path", path);
			infoMap.put("size", String.valueOf(file.length()));
			infoMap.put("lastModified", String.valueOf(file.lastModified()));
			infoMap.put("hash", hash);
			byte[] headData = JSON.toJSONString(infoMap).getBytes("UTF-8");
			DataFrame headFrame = new DataFrame();
			headFrame.setType(DataFrame.TYPE_FI);
			headFrame.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			headFrame.setDataId(dataId);
			headFrame.setSeq(0);
			headFrame.setSize(headData.length);
			headFrame.setData(headData);
			frameWriteHandler.write(headFrame);
			
			in = new FileInputStream(filePath);
			byte[] buf = new byte[102400];
			int len = -1;
			int seq = 0;
			while((len = in.read(buf)) != -1){
				seq++;
				DataFrame frame = new DataFrame();
				frame.setType(DataFrame.TYPE_FC);
				frame.setId(UUID.randomUUID().toString().replaceAll("-", ""));
				frame.setDataId(dataId);
				frame.setSeq(seq);
				frame.setSize(len);
				frame.setData(buf);
				frameWriteHandler.write(frame);
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			IOUtils.closeQuietly(in);
		}
	}
}
