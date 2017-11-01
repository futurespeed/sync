package org.fs.sync.agent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;

public class AgentExecutorThread extends Thread{
	
	private Socket socket;
	
	public AgentExecutorThread(Socket socket){
		this.socket = socket;
	}
	
	@Override
	public void run() {
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				execute(line);
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(socket);
		}
	}
	
	public void execute(String cmd) {
		Map<?, ?> infoMap = JSON.parseObject(cmd, Map.class);
		System.out.println(infoMap);
		//TODO
	}
}
