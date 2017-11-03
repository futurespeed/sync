package test;

import org.fs.sync.config.DataSourceContext;
import org.fs.sync.config.UserSetting;

public class TestUserSetting {
	public static void main(String[] args) {
		DataSourceContext.setConnectString("jdbc:sqlite:D:/temp/sync/config_local.db");
		DataSourceContext.init();
		
		UserSetting.setConfig(UserSetting.CONFIG_DB_PATH, "D:/temp/sync/config_local.db");
		UserSetting.storageLocal("1234");
	}
}