package org.fs.sync.rest.res;

import com.alibaba.fastjson.JSON;
import org.fs.sync.config.UserConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.HashMap;
import java.util.Map;

@Path("config")
public class ConfigResource {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigResource.class);

    @GET
    @Produces("application/json")
    @Path("/userDirs")
    public String userDirs(@QueryParam("userId") String userId,
                          @QueryParam("token") String token) {
        LOG.trace("ConfigResource.userDirs[userId: " + userId + ", token: " + token + "]");
        return JSON.toJSONString(UserConfig.getUserDirs(userId));
    }

    @GET
    @Produces("application/json")
    @Path("/storageUserDir")
    public String storageUserDir(@QueryParam("userId") String userId,
                                 @QueryParam("configId") String configId,
                                 @QueryParam("path") String path,
                           @QueryParam("token") String token) {
        LOG.trace("ConfigResource.userDirs[userId: " + userId + ", token: " + token + "]");
        Map<String, Object> result = new HashMap<String, Object>();
        try{
            UserConfig.storageUserDir(userId, configId, path);
            result.put("result", "success");
        }catch(Exception e){
            LOG.error("fail to storage user dir", e);
            result.put("result", "fail");
        }
        return JSON.toJSONString(result);
    }
}
