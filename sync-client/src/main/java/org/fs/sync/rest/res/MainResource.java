package org.fs.sync.rest.res;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Path("/")
public class MainResource {
	private static final Logger LOG = LoggerFactory.getLogger(MainResource.class);
	
	@GET
	@Produces("text/html")
	@Path("/")
	public Response main() throws Exception{
		return Response.temporaryRedirect(new URI("/static/main.html")).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/test")
	public Map test(Map params){
		params.put("result", "success");
		return params;
	}
}
