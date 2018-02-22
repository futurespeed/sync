package org.fs.sync.rest.res;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("main")
public class MainResource {
	private static final Logger LOG = LoggerFactory.getLogger(MainResource.class);
	
	@GET
	@Produces("text/html")
	@Path("/")
	public String main(){
		return "<html>Welcome to Sync!<br/><br/><form action='/agent/connect'>user id: <input name='userId' /><input type='submit' value='connect' /></form></html>";
	}
}
