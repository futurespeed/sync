package org.fs.sync.rest;

import java.net.URI;

import org.fs.sync.rest.res.SyncResource;
import org.glassfish.jersey.netty.httpserver.NettyHttpContainerProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;

public class RestContainer {
	
	private static final Logger LOG = LoggerFactory.getLogger(RestContainer.class);
	
    private static final URI BASE_URI = URI.create("http://127.0.0.1:20000/");
	
	public static void main(String[] args) {
		try {
            System.out.println("\"Sync\" Jersey App on Netty container.");

            ResourceConfig resourceConfig = new ResourceConfig(SyncResource.class);
            final Channel server = NettyHttpContainerProvider.createHttp2Server(BASE_URI, resourceConfig, null);

            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    server.close();
                }
            }));

            System.out.println(String.format("Application started. (HTTP/2 enabled!)"));
            Thread.currentThread().join();
        } catch (InterruptedException e) {
        	LOG.error("Rest container startup fail", e);
        }

	}
}
