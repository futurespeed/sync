package org.fs.sync.transfer;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.fs.sync.transfer.handler.FrameReadHandler;

import com.alibaba.fastjson.JSON;

public class ChannelReader {
	
	private String ip = "127.0.0.1";
	
	private int port = 20008;
	
	private String userId;
	
	private String token;
	
	private String configId;
	
	private String workDir;
	
	private Socket socket;
	
	private FrameReadHandler frameReadHandler;
	
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

	public String getWorkDir() {
		return workDir;
	}

	public void setWorkDir(String workDir) {
		this.workDir = workDir;
	}

	public void init(){
		frameReadHandler = new FrameReadHandler();
		if(workDir != null){
			frameReadHandler.setWorkDir(workDir);
		}
	}
	
	public void open(){
		try {
			Map<String, Object> infoMap = new HashMap<String, Object>();
			infoMap.put("type", "reader");
			infoMap.put("userId", userId);
			infoMap.put("configId", configId);
			infoMap.put("token", token);
			socket = new Socket(ip, port);
			socket.getOutputStream().write((JSON.toJSONString(infoMap) + "\n").getBytes("UTF-8"));
			new ChannelReadThread(socket, frameReadHandler).start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void close(){
		try {
			socket.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
