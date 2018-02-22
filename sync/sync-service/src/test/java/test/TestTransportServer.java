package test;

import org.fs.sync.transfer.TransportServer;

public class TestTransportServer {
	public static void main(String[] args) {
		TransportServer server = new TransportServer();
		server.start();
	}
}
