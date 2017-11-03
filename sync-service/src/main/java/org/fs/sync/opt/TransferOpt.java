package org.fs.sync.opt;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.fs.sync.config.UserSetting;
import org.fs.sync.transfer.ChannelWriter;

import com.alibaba.fastjson.JSON;

public class TransferOpt {
	public static void execute(Map<String, Object> params) {
		BufferedReader in = null;
		try {
			String userId = (String) params.get("userId");
			String configId = (String) params.get("configId");
			String clientId = (String) params.get("clientId");
			URL url = new URL(UserSetting.getConfig(UserSetting.SERVICE_PATH) + "/sync/openReadChannel?userId=" + userId
					+ "&configId=" + configId + "&clientId=" + clientId);
			URLConnection connection = url.openConnection();
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.connect();
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String result = "";
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			
			Map<String, Object> resultMap = JSON.parseObject(result);
			if("success".equals(resultMap.get("result"))){
				ChannelWriter writer = new ChannelWriter();
				writer.setUserId(userId);
				writer.setConfigId(configId);
				writer.init();
				try{
					writer.open();
					for(Map<String, String> fileInfo: (List<Map<String, String>>) params.get("fileList")){
						writer.writeFile(fileInfo.get("fileId"), fileInfo.get("localPath"), fileInfo.get("path"));
					}
				}finally{
					writer.close();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
}
