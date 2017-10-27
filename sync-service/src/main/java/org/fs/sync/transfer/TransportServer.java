package org.fs.sync.transfer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class TransportServer {
	
	private int port = 20008;
	
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
	
	static class ConnectThread extends Thread {
		private TransportServer server;
		
		public ConnectThread(TransportServer server) {
			this.server = server;
		}
		
		@Override
		public void run() {
			try{
				while(server.isRunning){
					Socket socket = server.serverSocket.accept();
					(new ProcessThread(server, socket)).start();
				}
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}
	}
	
	static class ProcessThread extends Thread {
		
		private TransportServer server;
		
		private Socket socket;
		
		public ProcessThread(TransportServer server, Socket socket) {
			this.server = server;
			this.socket = socket;
		}
		
		@Override
		public void run() {
			try{
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				String line = reader.readLine();
				Map infoMap = JSON.parseObject(line, Map.class);
				String channelId = infoMap.get("userId") + "_" + infoMap.get("configId");
				if("writer".equals(infoMap.get("type"))){
					Socket destSocket = server.socketChannelMap.get(channelId);
					if(null == destSocket){
						throw new RuntimeException("cannot find reader channel !");
					}
					BufferedWriter writer = null;
					try{
						writer = new BufferedWriter(new OutputStreamWriter(destSocket.getOutputStream()));
						while(server.isRunning && (line = reader.readLine()) != null){
							writer.write(line);
							writer.write("\n");
							writer.flush();
						}
					}finally{
						if(writer != null){
							try{
								writer.close();
							}catch(Exception e){
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if(reader != null){
							try{
								reader.close();
							}catch(Exception e){
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if(socket != null){
							try{
								socket.close();
							}catch(Exception e){
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if(destSocket != null){
							try{
								destSocket.close();
							}catch(Exception e){
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}else if("reader".equals(infoMap.get("type"))){
					server.socketChannelMap.put(channelId, socket);
				}
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}
	}
}
