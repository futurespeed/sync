package test;

import org.fs.sync.config.SystemConfig;

public class TestSystemConfig {
	public static void main(String[] args) {
		SystemConfig.loadConfig();

		System.out.println(SystemConfig.getConfig(SystemConfig.DB_CONNECT_STRING));
	}
}
