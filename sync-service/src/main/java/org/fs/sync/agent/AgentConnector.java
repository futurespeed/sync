package org.fs.sync.agent;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class AgentConnector {
	private String ip = "127.0.0.1";
	
	private int port = 20007;
	
	private String userId;
	
	private String token;
	
	private String clientId;
	
	private Socket socket;

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
	
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void open(){
		try {
			Map<String, Object> infoMap = new HashMap<String, Object>();
			infoMap.put("type", "agent");
			infoMap.put("userId", userId);
			infoMap.put("token", token);
			infoMap.put("clientId", clientId);
			socket = new Socket(ip, port);
			socket.getOutputStream().write((JSON.toJSONString(infoMap) + "\n").getBytes("UTF-8"));
			new AgentExecutorThread(socket).start();
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
