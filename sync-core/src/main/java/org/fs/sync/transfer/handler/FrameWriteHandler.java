package org.fs.sync.transfer.handler;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.fs.sync.transfer.data.DataFrame;
import org.fs.sync.transfer.data.DataFrameSerializer;

public class FrameWriteHandler {

	private Socket socket;

	private BufferedWriter writer;

	private DataFrameSerializer serializer;

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		try {
			this.socket = socket;
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public DataFrameSerializer getSerializer() {
		return serializer;
	}

	public void setSerializer(DataFrameSerializer serializer) {
		this.serializer = serializer;
	}

	public void write(DataFrame dataFrame) {
		try {
			byte[] data = serializer.serialize(dataFrame);
			writer.write(Base64.encodeBase64String(data));
			writer.write("\n");
			writer.flush();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void release(){
		IOUtils.closeQuietly(writer);
	}
}
