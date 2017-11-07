package org.fs.sync.rest;

import org.fs.sync.agent.AgentServer;
import org.fs.sync.config.SystemConfig;
import org.fs.sync.transfer.TransportServer;

public class ServerContext {
	private static AgentServer agentServer;
	private static TransportServer transportServer;
	private static boolean isInit = false;
	
	public static synchronized void init(){
		if(!isInit){
			agentServer = new AgentServer();
			agentServer.setPort(Integer.parseInt(SystemConfig.getConfig(SystemConfig.AGENT_SERVER_PORT)));
			agentServer.start();
			transportServer = new TransportServer();
			transportServer.setPort(Integer.parseInt(SystemConfig.getConfig(SystemConfig.TRANSPORT_SERVER_PORT)));
			transportServer.start();
			isInit = true;
		}
	}

	public static AgentServer getAgentServer() {
		return agentServer;
	}

	public static TransportServer getTransportServer() {
		return transportServer;
	}
	
	
}
