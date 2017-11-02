package org.fs.sync.rest;

import org.fs.sync.agent.AgentServer;
import org.fs.sync.transfer.TransportServer;

public class ServerContext {
	private static AgentServer agentServer;
	private static TransportServer transportServer;
	private static boolean isInit = false;
	
	public static synchronized void init(){
		if(!isInit){
			agentServer = new AgentServer();
			agentServer.start();
			transportServer = new TransportServer();
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
