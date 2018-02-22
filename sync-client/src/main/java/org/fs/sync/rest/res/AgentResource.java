package org.fs.sync.rest.res;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.fs.sync.agent.AgentConnector;
import org.fs.sync.config.UserSetting;
import org.fs.sync.rest.ServerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

@Path("agent")
public class AgentResource {
	
	private static final Logger LOG = LoggerFactory.getLogger(AgentResource.class);
	
	
	@GET
	@Produces("application/json")
	@Path("/connect")
	public String connect(@QueryParam("userId") String userId,
			@QueryParam("token") String token){
		LOG.trace("AgentResource.connect[userId: " + userId + ", token: " + token + "]");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		//TODO permission
		
		UserSetting.loadLocal(userId);
		if(null == UserSetting.getConfig(UserSetting.SERVICE_PATH)){
			//default setting
			UserSetting.setConfig(UserSetting.SERVICE_PATH, "http://120.79.86.63:20000");
			UserSetting.setConfig(UserSetting.WORK_DIR, "work");
			UserSetting.setConfig(UserSetting.AGENT_SERVER_DOMAIN, "120.79.86.63");
			UserSetting.setConfig(UserSetting.AGENT_SERVER_PORT, "20007");
			UserSetting.setConfig(UserSetting.TRANSPORT_SERVER_DOMAIN, "120.79.86.63");
			UserSetting.setConfig(UserSetting.TRANSPORT_SERVER_PORT, "20008");
			UserSetting.storageLocal(userId);
		}

		try{
			ServerContext.agentConnect(userId);
			resultMap.put("result", "success");
			resultMap.put("msg", "ok");
		}catch(Exception e){
			LOG.error("AgentResource.connect error!", e);
			resultMap.put("result", "fail");
			resultMap.put("msg", e.getMessage());
		}
		
		return JSON.toJSONString(resultMap);
	}
}
