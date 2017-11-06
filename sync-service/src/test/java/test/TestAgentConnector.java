package test;

import java.util.UUID;

import org.fs.sync.agent.AgentConnector;
import org.fs.sync.config.DataSourceContext;
import org.fs.sync.config.UserSetting;

public class TestAgentConnector {
	public static void main(String[] args) {
		String userId = "1234";
		
		DataSourceContext.setConnectString("jdbc:sqlite:D:/temp/sync/config_local.db");
		DataSourceContext.init();
		UserSetting.loadLocal(userId);

		AgentConnector ac = new AgentConnector();
		ac.setUserId(userId);
		ac.setClientId(UUID.randomUUID().toString().replaceAll("-", ""));
		ac.open();
	}
}
