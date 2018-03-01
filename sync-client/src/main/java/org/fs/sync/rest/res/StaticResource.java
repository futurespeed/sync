package org.fs.sync.rest.res;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Path("static")
public class StaticResource {
	private static final Logger LOG = LoggerFactory.getLogger(StaticResource.class);
	
	@GET
	@Produces("text/html")
	@Path("/{path:.*}")
	public Response main(@PathParam("path") String path) throws Exception{
		InputStream in = null;
		in = getClass().getResourceAsStream("/www/" + path);
		if(null == in){
		    return Response.status(404).build();
        }
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		IOUtils.copy(in, out);
		//return new String(out.toByteArray(), "UTF-8");
        Response.ResponseBuilder response = Response.ok(out.toByteArray());
        return response.build();
    }
}
