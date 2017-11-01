package test;

import org.fs.sync.agent.AgentConnector;
import org.fs.sync.agent.AgentServer;

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
		
		server.sendCmdToChannel("1234_abc", "{\"type\":\"test_cmd\"}");
		
	}
}
