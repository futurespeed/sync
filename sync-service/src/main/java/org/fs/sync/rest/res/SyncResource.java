package org.fs.sync.rest.res;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.fs.sync.rest.ServerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

@Path("sync")
public class SyncResource {
	
	private static final Logger LOG = LoggerFactory.getLogger(SyncResource.class);
	
	@GET
	@Produces("application/json")
	@Path("/openReadChannel")
	public String openReadChannel(@QueryParam("userId") String userId,
			@QueryParam("configId") String configId,
			@QueryParam("clientId") String clientId,
			@QueryParam("token") String token){
		LOG.debug("SyncResource.openReadChannel[userId: " + userId + ", configId: " + configId + ", clientId: " + clientId + ", token: " + token + "]");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String channelId = userId + "_" + clientId;
		
		Map<String, Object> infoMap = new HashMap<String, Object>();
		infoMap.put("type", "open_read_channel");
		infoMap.put("userId", userId);
		infoMap.put("configId", configId);
		ServerContext.getAgentServer().sendCmdToChannel(channelId, JSON.toJSONString(infoMap));
		
		resultMap.put("result", "success");
		resultMap.put("msg", "ok");
		return JSON.toJSONString(resultMap);
	}
}
