package test;

import java.util.UUID;

import org.fs.sync.transfer.ChannelWriter;

public class TestChannelWriter {
	public static void main(String[] args) {
		ChannelWriter writer = new ChannelWriter();
		writer.setUserId("gjx");
		writer.setConfigId("123");
		writer.init();
		try{
			writer.open();
			
			writer.writeFile(UUID.randomUUID().toString().replaceAll("-", ""), "D:/temp/test/test.html", "/html");
			writer.writeFile(UUID.randomUUID().toString().replaceAll("-", ""), "D:/temp/VirtualBox-5.1.22-115126-Win.exe", "/other");
		}finally{
			writer.close();
		}
	}
}
