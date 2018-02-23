package org.fs.sync.agent;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.fs.sync.config.UserSetting;
import org.fs.sync.transfer.ChannelReader;
import org.fs.sync.handler.FileHandler;
import org.fs.sync.handler.FileReceiveHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;

public class AgentExecutorThread extends Thread{
	
	private static final Logger LOG = LoggerFactory.getLogger(AgentExecutorThread.class);
	
	private Socket socket;
	
	public AgentExecutorThread(Socket socket){
		this.socket = socket;
	}
	
	@Override
	public void run() {
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try{
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			String line = null;
			String result = null;
			while ((line = reader.readLine()) != null) {
				result = execute(line);
				writer.write(result);
				writer.newLine();
				writer.flush();
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(writer);
			IOUtils.closeQuietly(socket);
		}
	}
	
	public String execute(String cmd) {
		LOG.trace("receive cmd: " + cmd);
		Map<?, ?> infoMap = JSON.parseObject(cmd, Map.class);
		String type = String.valueOf(infoMap.get("type"));
		if("open_rad_channel".equals(type)){
			ChannelReader reader = new ChannelReader();
			reader.setUserId((String) infoMap.get("userId"));
			reader.setConfigId((String) infoMap.get("configId"));
			reader.setWorkDir(UserSetting.getConfig(UserSetting.WORK_DIR));
			reader.setIp(UserSetting.getConfig(UserSetting.TRANSPORT_SERVER_DOMAIN));
			reader.setPort(Integer.parseInt(UserSetting.getConfig(UserSetting.TRANSPORT_SERVER_PORT)));
			reader.setFileHandlers(Arrays.asList(new FileHandler[]{new FileReceiveHandler()}));
			reader.init();
			reader.open();
			return "ok";
		}
		//TODO
		return "error";
	}
}
