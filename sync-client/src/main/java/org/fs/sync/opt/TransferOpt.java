package org.fs.sync.opt;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.IOUtils;
import org.fs.sync.config.UserSetting;

import com.alibaba.fastjson.JSON;
import org.fs.sync.transfer.ChannelWriter;

public class TransferOpt {
	public static void execute(Map<String, Object> params) {
		String userId = (String) params.get("userId");
		String configId = (String) params.get("configId");
		String clientId = (String) params.get("clientId");
		String token = (String) params.get("token");
		String url = UserSetting.getConfig(UserSetting.SERVICE_PATH) + "/sync/getAgentClient?userId=" + userId + "&token=" + token;
		String result = httpGet(url);
		Map<String, Object> resultMap = JSON.parseObject(result);
		
		List<String> clientIds = (List<String>) resultMap.get("clientIds");
		if(null == clientIds || clientIds.isEmpty()){
			throw new RuntimeException("no clients!");
		}
		for(String cid: clientIds){
			if(StringUtils.equals(cid, clientId)){
				continue;
			}
			
			url = UserSetting.getConfig(UserSetting.SERVICE_PATH) + "/sync/openReadChannel?userId=" + userId
					+ "&configId=" + configId + "&clientId=" + cid + "&token=" + token;
			result = httpGet(url);
			resultMap = JSON.parseObject(result);
			if("success".equals(resultMap.get("result"))){
				ChannelWriter writer = new ChannelWriter();
				writer.setIp(UserSetting.getConfig(UserSetting.TRANSPORT_SERVER_DOMAIN));
				writer.setPort(Integer.parseInt(UserSetting.getConfig(UserSetting.TRANSPORT_SERVER_PORT)));
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
		}
	}
	
	public static String httpGet(String url){
		BufferedReader in = null;
		try{
			URL urlObject = new URL(url);
			URLConnection connection = urlObject.openConnection();
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
			return result;
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally {
			IOUtils.closeQuietly(in);
		}
	}
}
