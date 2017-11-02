package org.fs.sync.rest.res;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

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
			@QueryParam("token") String token){
		LOG.debug("userId: " + userId + ", configId" + configId + ", token: " + token);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", "success");
		resultMap.put("msg", "ok");
		return JSON.toJSONString(resultMap);
	}
}
