package org.fs.sync.agent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class AgentServer {
	
	private static final Logger LOG = LoggerFactory.getLogger(AgentServer.class);
	
	private int port = 20007;
	
	private ServerSocket serverSocket;
	
	private boolean isRunning = false;
	
	private Map<String, Object> channelMap = new HashMap<String, Object>();
	
	private Map<String, Object> userInfoMap = new HashMap<String, Object>();
	
	
	public void setPort(int port) {
		this.port = port;
	}

	public void start(){
		try{
			serverSocket = new ServerSocket(port);
			isRunning = true;
			(new ConnectThread(this)).start();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public void stop(){
		isRunning = false;
	}
	
	protected void closeAllChannel(){
		for(Object info: channelMap.values()){
			Map<String, Object> infoMap = (Map<String, Object>) info;
			IOUtils.closeQuietly((Socket) infoMap.get("socket"));
		}
		channelMap.clear();
	}
	
	public String sendCmdToChannel(String channelId, String cmd){
		try{
			Map<String, Object> info = (Map<String, Object>) channelMap.get(channelId);
			if(null == info){
				throw new RuntimeException("channel not exists !");
			}
			BufferedWriter writer = (BufferedWriter) info.get("writer");
			writer.write(cmd);
			writer.write("\n");
			writer.flush();
			
			BufferedReader reader = (BufferedReader) info.get("reader");
			return reader.readLine();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public Map<String, Object> getUserClient(String userId) {
		return (Map<String, Object>) userInfoMap.get(userId);
	}
	
	static class ConnectThread extends Thread {
		private AgentServer server;
		
		public ConnectThread(AgentServer server) {
			this.server = server;
			setDaemon(true);
		}
		
		@Override
		public void run() {
			try{
				LOG.info("[AgentServer] begin to accept clients...");
				while(server.isRunning){
					Socket socket = server.serverSocket.accept();
					(new ProcessThread(server, socket)).start();
				}
				server.closeAllChannel();
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}
	}
	
	static class ProcessThread extends Thread {
		
		private AgentServer server;
		
		private Socket socket;
		
		public ProcessThread(AgentServer server, Socket socket) {
			setDaemon(true);
			this.server = server;
			this.socket = socket;
		}
		
		@Override
		public void run() {
			try{
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
				writer.write("ok\n");
				writer.flush();
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				String line = reader.readLine();
				Map<?, ?> infoMap = JSON.parseObject(line, Map.class);
				String userId = (String) infoMap.get("userId");
				String clientId = (String) infoMap.get("clientId");
				String channelId = userId + "_" + clientId;
				Map<String, Object> channelInfo = (Map<String, Object>) server.channelMap.get(channelId);
				//TODO permission
				if(channelInfo != null){
					LOG.warn("agent channel [" + channelId + "] already exists, close recent agent socket !");
					IOUtils.closeQuietly((Socket) channelInfo.get("socket"));
				}
				channelInfo = new HashMap<String, Object>();
				channelInfo.put("socket", socket);
				channelInfo.put("writer", new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8")));
				channelInfo.put("reader", new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8")));
				server.channelMap.put(channelId, channelInfo);
				Map<String, Object> clientInfoMap = (Map<String, Object>) server.userInfoMap.get(userId);
				List<String> clientIds = null;
				if(null == clientInfoMap){
					clientInfoMap = new HashMap<String, Object>();
					clientInfoMap.put("userId", userId);
					clientIds = new ArrayList<String>();
					clientInfoMap.put("clientIds", clientIds);
					server.userInfoMap.put(userId, clientInfoMap);
				}else{
					clientIds = (List<String>) clientInfoMap.get("clientIds");
					clientIds.clear();
				}
				clientIds.add(clientId);
				LOG.trace("agent channel [" + channelId + "] opened !");
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}
	}

}
