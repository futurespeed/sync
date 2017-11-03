package test;

import org.fs.sync.agent.AgentConnector;
import org.fs.sync.config.DataSourceContext;

public class TestAgentConnector {
	public static void main(String[] args) {
		DataSourceContext.setConnectString("jdbc:sqlite:D:/temp/sync/config_local.db");
		DataSourceContext.init();

		AgentConnector ac = new AgentConnector();
		ac.setUserId("1234");
		ac.setClientId("abc");
		ac.open();
	}
}
