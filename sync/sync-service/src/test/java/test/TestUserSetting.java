package test;

import org.fs.sync.config.DataSourceContext;
import org.fs.sync.config.UserSetting;

public class TestUserSetting {
	public static void main(String[] args) {
		DataSourceContext.setConnectString("jdbc:sqlite:D:/temp/sync/config_local.db");
		DataSourceContext.init();
		
		UserSetting.setConfig(UserSetting.SERVICE_PATH, "http://120.79.86.63:20000");
		UserSetting.setConfig(UserSetting.WORK_DIR, "D:/temp/sync/work1");
		UserSetting.setConfig(UserSetting.TRANSPORT_SERVER_DOMAIN, "120.79.86.63");
		UserSetting.setConfig(UserSetting.TRANSPORT_SERVER_PORT, "20008");
		UserSetting.storageLocal("1234");
	}
}
