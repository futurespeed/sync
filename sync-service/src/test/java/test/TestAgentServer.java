package test;

import java.util.HashMap;
import java.util.Map;

import org.fs.sync.agent.AgentConnector;
import org.fs.sync.agent.AgentServer;

import com.alibaba.fastjson.JSON;

public class TestAgentServer {
	public static void main(String[] args) throws Exception {
		AgentServer server = new AgentServer();
		server.start();
		
		Thread.sleep(3000);
		
		AgentConnector ac = new AgentConnector();
		ac.setUserId("1234");
		ac.setClientId("abc");
		ac.open();
		
		Thread.sleep(3000);
		
		
		Map<String, Object> infoMap = new HashMap<String, Object>();
		infoMap.put("type", "open_read_channel");
		infoMap.put("userId", "1234");
		infoMap.put("configId", "567");
		server.sendCmdToChannel("1234_abc", JSON.toJSONString(infoMap));
		
	}
}
