package org.fs.sync.rest.res;

import java.util.HashMap;
import java.util.List;
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
		LOG.trace("SyncResource.openReadChannel[userId: " + userId + ", configId: " + configId + ", clientId: " + clientId + ", token: " + token + "]");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String channelId = userId + "_" + clientId;
		
		//TODO permission
		
		Map<String, Object> infoMap = new HashMap<String, Object>();
		infoMap.put("type", "open_read_channel");
		infoMap.put("userId", userId);
		infoMap.put("configId", configId);
		
		try{
			String result = ServerContext.getAgentServer().sendCmdToChannel(channelId, JSON.toJSONString(infoMap));
			if("ok".equals(result)){
				resultMap.put("result", "success");
				resultMap.put("msg", "ok");
			}else{
				resultMap.put("result", "fail");
				resultMap.put("msg", "fail to send cmd to channel");
			}
		}catch(Exception e){
			LOG.error("fail to open read channel", e);
			resultMap.put("result", "fail");
			resultMap.put("msg", e.getMessage());
		}
		return JSON.toJSONString(resultMap);
	}
	
	@GET
	@Produces("application/json")
	@Path("/getAgentClient")
	public String getAgentClient(@QueryParam("userId") String userId,
			@QueryParam("token") String token){
		LOG.trace("SyncResource.getAgentClient[userId: " + userId + ", token: " + token + "]");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		//TODO permission
		
		Map<String, Object> clientInfo = ServerContext.getAgentServer().getUserClient(userId);
		List<String> clientIds = clientInfo != null ? (List<String>) clientInfo.get("clientIds") : null;
		resultMap.put("clientIds", clientIds);
		resultMap.put("result", "success");
		resultMap.put("msg", "ok");
		return JSON.toJSONString(resultMap);
	}
}
