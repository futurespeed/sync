package org.fs.sync.rest.res;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

@Path("/")
public class MainResource {
	private static final Logger LOG = LoggerFactory.getLogger(MainResource.class);
	
	@GET
	@Produces("text/html")
	@Path("/")
	public Response main() throws Exception{
		return Response.temporaryRedirect(new URI("/static/main.html")).build();
	}
}
