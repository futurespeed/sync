package org.fs.sync.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
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
	private List<FileHandler> fileHandlers = new ArrayList<FileHandler>();

	public String getWorkDir() {
		return workDir;
	}

	public void setWorkDir(String workDir) {
		this.workDir = workDir;
	}
	
	public void init(){

	}

	public void addFileHandler(FileHandler fileHandler){
		fileHandlers.add(fileHandler);
	}

	@SuppressWarnings("unchecked")
	public void read(DataFrame frame) {
		try {
			if (DataFrame.TYPE_FI == frame.getType()) {
				if (fout != null) {
					IOUtils.closeQuietly(fout);
					infoMap.put("_temp_path", file.getAbsolutePath());
					fout = null;
					file = null;
					for(FileHandler handler: fileHandlers){
						handler.handle(infoMap);
					}
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
			} else if (DataFrame.TYPE_FC == frame.getType()) {
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
				for(FileHandler handler: fileHandlers){
					handler.handle(infoMap);
				}
				infoMap = null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
