package test;

import java.util.UUID;

import org.fs.sync.agent.AgentConnector;
import org.fs.sync.config.DataSourceContext;
import org.fs.sync.config.UserSetting;

public class TestAgentConnector {
	public static void main(String[] args) {
		String userId = "1234";
		
		DataSourceContext.setConnectString("jdbc:sqlite:config_local.db");
		DataSourceContext.init();
		DataSourceContext.initUserTables();
		
		//default setting
		UserSetting.setConfig(UserSetting.SERVICE_PATH, "http://120.79.86.63:20000");
		UserSetting.setConfig(UserSetting.WORK_DIR, "work");
		UserSetting.setConfig(UserSetting.TRANSPORT_SERVER_DOMAIN, "120.79.86.63");
		UserSetting.setConfig(UserSetting.TRANSPORT_SERVER_PORT, "20008");
		UserSetting.loadLocal(userId);

		AgentConnector ac = new AgentConnector();
		ac.setIp("120.79.86.63");
		ac.setUserId(userId);
		ac.setClientId(UUID.randomUUID().toString().replaceAll("-", ""));
		ac.open();
	}
}
