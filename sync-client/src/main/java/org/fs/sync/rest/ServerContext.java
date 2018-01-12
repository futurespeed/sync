package org.fs.sync.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.fs.sync.agent.AgentConnector;
import org.fs.sync.config.UserSetting;

public class ServerContext {
	private static boolean isInit = false;
	
	private static Map<String, AgentConnector> agentMap = new HashMap<String, AgentConnector>();
	
	public static synchronized void init(){
		if(!isInit){
			isInit = true;
		}
	}
	
	public static synchronized void agentConnect(String userId){
		if(agentMap.containsKey(userId)){
			throw new RuntimeException("agent is connected!");
		}
		AgentConnector ac = new AgentConnector();
		ac.setIp(UserSetting.getConfig(UserSetting.AGENT_SERVER_DOMAIN));
		ac.setPort(Integer.parseInt(UserSetting.getConfig(UserSetting.AGENT_SERVER_PORT)));
		ac.setUserId(userId);
		ac.setClientId(UUID.randomUUID().toString().replaceAll("-", ""));
		ac.open();
		agentMap.put(userId, ac);
	}
}
