package org.fs.sync.config;

import java.io.IOException;
import java.util.Properties;

public class SystemConfig {
	public static final String REST_PORT = "sys.rest.port";
	public static final String AGENT_SERVER_PORT = "sys.agent.server.port";
	public static final String TRANSPORT_SERVER_PORT = "sys.transport.server.port";
	public static final String DB_CONNECT_STRING = "sys.db.connect.string";
	
	private static Properties prop = new Properties();
	
	public static synchronized void loadConfig(){
		try {
			prop.load(SystemConfig.class.getResourceAsStream("/conf.properties"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String getConfig(String key){
		return prop.getProperty(key);
	}
}
