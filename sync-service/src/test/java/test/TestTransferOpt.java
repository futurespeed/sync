package test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.fs.sync.config.DataSourceContext;
import org.fs.sync.config.UserSetting;
import org.fs.sync.opt.TransferOpt;

public class TestTransferOpt {
	public static void main(String[] args) {
		String userId = "1234";
		String configId = "567";
		String clientId = UUID.randomUUID().toString().replaceAll("-", "");
		
		DataSourceContext.setConnectString("jdbc:sqlite:D:/temp/sync/config_local.db");
		DataSourceContext.init();
		UserSetting.loadLocal(userId);
		
		List<Map<String, String>> fileList = new ArrayList<Map<String, String>>();
		File dir = new File("D:/temp/test");
		addToList(dir.getAbsolutePath(), dir, fileList);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("configId", configId);
		params.put("clientId", clientId);
		params.put("fileList", fileList);
		
		TransferOpt.execute(params);
	}
	
	public static void addToList(String parentPath, File file, List<Map<String, String>> fileList){
		if(file.isDirectory()){
			for(File subFile : file.listFiles()){
				if(subFile.isDirectory()){
					addToList(parentPath, subFile, fileList);
				}else{
					addFileToList(parentPath, subFile, fileList);
				}
			}
		}else{
			addFileToList(parentPath, file, fileList);
		}
	}
	
	public static void addFileToList(String parentPath, File file, List<Map<String, String>> fileList){
		Map<String, String> fileInfo = new HashMap<String, String>();
		fileInfo.put("fileId", UUID.randomUUID().toString().replaceAll("-", ""));
		fileInfo.put("localPath", file.getAbsolutePath());
		fileInfo.put("path", file.getParent().substring(parentPath.length()));
		fileList.add(fileInfo);
	}
}
