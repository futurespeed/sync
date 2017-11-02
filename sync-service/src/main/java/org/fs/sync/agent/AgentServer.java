package org.fs.sync.agent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;

public class AgentServer {
	private int port = 20007;
	
	private ServerSocket serverSocket;
	
	private boolean isRunning = false;
	
	private Map<String, Socket> socketChannelMap = new HashMap<String, Socket>();
	
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
		for(Socket s: socketChannelMap.values()){
			IOUtils.closeQuietly(s);
		}
		socketChannelMap.clear();
	}
	
	public void sendCmdToChannel(String channelId, String cmd){
		try{
			Socket socket = socketChannelMap.get(channelId);
			if(null == socket){
				throw new RuntimeException("channel not exists !");
			}
			socket.getOutputStream().write(cmd.getBytes("UTF-8"));
			socket.getOutputStream().write("\n".getBytes("UTF-8"));
			socket.getOutputStream().flush();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
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
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				String line = reader.readLine();
				Map<?, ?> infoMap = JSON.parseObject(line, Map.class);
				String channelId = infoMap.get("userId") + "_" + infoMap.get("clientId");
				Socket recentSocket = server.socketChannelMap.get(channelId);
				//TODO permission
				if(recentSocket != null){
					socket.close();//FIXME
					throw new RuntimeException("agent channel [" + channelId + "] already exists !");
				}
				server.socketChannelMap.put(channelId, socket);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}
	}
}
