package test;

import org.fs.sync.transfer.ChannelReader;

public class TestChannelReader {
	public static void main(String[] args) throws Exception {
		ChannelReader reader = new ChannelReader();
		reader.setUserId("1234");
		reader.setConfigId("567");
		reader.setWorkDir("D:/temp/sync/work/123");
		reader.init();
		reader.open();
		
//		System.in.read();
	}
}
