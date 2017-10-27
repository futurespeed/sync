package org.fs.sync.transfer.handler;

import java.io.File;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.fs.sync.util.FileHashUtil;

public class FileReceiveHandler {
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
			
			System.out.println("receive file:" + name + ",hash:" + hash + ",localFilehash:" + localFilehash);
			
			File dir = new File("D:/temp/sync/dest/1" + path);
			if(!dir.exists()){
				dir.mkdirs();
			}
			File destFile = new File(dir, name);
			FileUtils.copyFile(file, destFile);
			destFile.setLastModified(Long.parseLong(lastModified));
			file.delete();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}
