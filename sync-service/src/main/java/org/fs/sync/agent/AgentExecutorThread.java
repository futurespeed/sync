package org.fs.sync.agent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.fs.sync.transfer.ChannelReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class AgentExecutorThread extends Thread{
	
	private static final Logger LOG = LoggerFactory.getLogger(AgentExecutorThread.class);
	
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
		LOG.trace("receive cmd: " + cmd);
		Map<?, ?> infoMap = JSON.parseObject(cmd, Map.class);
		String type = String.valueOf(infoMap.get("type"));
		if("open_read_channel".equals(type)){
			ChannelReader reader = new ChannelReader();
			reader.setUserId((String) infoMap.get("userId"));
			reader.setConfigId((String) infoMap.get("configId"));
//			reader.setWorkDir("D:/temp/sync/work/123");
			reader.init();
			reader.open();
		}
		//TODO
	}
}
