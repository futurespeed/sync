package test;

import org.fs.sync.agent.AgentConnector;

public class TestAgentConnector {
	public static void main(String[] args) {
		AgentConnector ac = new AgentConnector();
		ac.setUserId("1234");
		ac.setClientId("abc");
		ac.open();
	}
}
