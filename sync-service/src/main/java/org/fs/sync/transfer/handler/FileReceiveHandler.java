package org.fs.sync.transfer.handler;

import java.io.File;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.fs.sync.util.FileHashUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileReceiveHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(FileReceiveHandler.class);
	
	public void handle(Map<String, Object> infoMap){
		try{
			String configId = (String) infoMap.get("configId");
			String name = (String) infoMap.get("name");
			String path = (String) infoMap.get("path");
			String size = (String) infoMap.get("size");
			String lastModified = (String) infoMap.get("lastModified");
			String hash = (String) infoMap.get("hash");
			String tempPath = (String) infoMap.get("_temp_path");
			File file = new File(tempPath);
			String localFilehash = FileHashUtil.getSha1(file);
			
			LOG.info("receive file:" + name + ",size:" + size + ",hash:" + hash + ",localFilehash:" + localFilehash);
			
			if(localFilehash.equals(hash)){
				File dir = new File(getPathByConfigId(configId) + path);
				if(!dir.exists()){
					dir.mkdirs();
				}
				File destFile = new File(dir, name);
				FileUtils.copyFile(file, destFile);
				destFile.setLastModified(Long.parseLong(lastModified));
			}else{
				LOG.warn("ignore file[" + name + "], because hash is not the same !");
			}
			file.delete();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	protected String getPathByConfigId(String configId){
		//TODO getPathByConfigId
		String path = "D:/temp/sync/dest/1";
		return path;
	}
}
