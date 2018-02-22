package test;

import org.fs.sync.config.DataSourceContext;
import org.fs.sync.config.UserConfig;

public class TestUserConfig {
	public static void main(String[] args) {
		DataSourceContext.setConnectString("jdbc:sqlite:D:/temp/sync/config.db");
		DataSourceContext.init();
		
		UserConfig.storageUserDir("1234", "567", "D:\\temp\\sync\\1");
	}
}
