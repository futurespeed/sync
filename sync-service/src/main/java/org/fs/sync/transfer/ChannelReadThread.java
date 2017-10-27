package org.fs.sync.transfer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.apache.commons.codec.binary.Base64;
import org.fs.sync.transfer.data.DataFrame;
import org.fs.sync.transfer.data.DataFrameSerializer;
import org.fs.sync.transfer.handler.FrameReadHandler;

public class ChannelReadThread extends Thread {
	
	private Socket socket;
	
	private FrameReadHandler frameReadHandler;
	
	public ChannelReadThread(Socket socket, FrameReadHandler frameReadHandler) {
		this.socket = socket;
		this.frameReadHandler = frameReadHandler;
	}
	
	@Override
	public void run() {
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			DataFrameSerializer dataFrameSerializer = new DataFrameSerializer();
			String line = null;
			frameReadHandler.init();
			try{
				while ((line = reader.readLine()) != null) {
					DataFrame frame = dataFrameSerializer.deserialize(Base64.decodeBase64(line.getBytes("UTF-8")));
					frameReadHandler.read(frame);
				}
			}finally{
				frameReadHandler.release();
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			if(reader != null){
				try {
					reader.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
