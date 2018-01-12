package org.fs.sync.transfer.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.fs.sync.transfer.data.DataFrame;

import com.alibaba.fastjson.JSON;

public class FrameReadHandler {

	private String workDir = "work";
	private File file;
	private FileOutputStream fout;
	private Map<String, Object> infoMap;
	private FileReceiveHandler fileReceiveHandler;

	public String getWorkDir() {
		return workDir;
	}

	public void setWorkDir(String workDir) {
		this.workDir = workDir;
	}
	
	public void init(){
		fileReceiveHandler = new FileReceiveHandler();
	}

	@SuppressWarnings("unchecked")
	public void read(DataFrame frame) {
		try {
			if (3000 == frame.getType()) {
				if (fout != null) {
					IOUtils.closeQuietly(fout);
					infoMap.put("_temp_path", file.getAbsolutePath());
					fout = null;
					file = null;
					fileReceiveHandler.handle(infoMap);
					infoMap = null;
				}
				infoMap = JSON.parseObject(new String(frame.getData()), Map.class);
				file = new File(workDir + File.separator + (UUID.randomUUID().toString()) + "._");
				File dir = file.getParentFile();
				if(!dir.exists()){
					dir.mkdirs();
				}
				file.createNewFile();
				fout = new FileOutputStream(file);
			} else if (3001 == frame.getType()) {
				fout.write(frame.getData());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void release() {
		try {
			if (fout != null) {
				IOUtils.closeQuietly(fout);
				infoMap.put("_temp_path", file.getAbsolutePath());
				fout = null;
				file = null;
				fileReceiveHandler.handle(infoMap);
				infoMap = null;
			}
//			(new File(workDir)).delete();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}