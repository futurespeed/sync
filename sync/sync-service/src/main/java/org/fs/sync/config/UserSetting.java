package org.fs.sync.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserSetting {
	public static final String AGENT_DOMAIN = "user.agent.domain";
	public static final String AGENT_PORT = "user.agent.port";
	public static final String SERVICE_PATH = "user.config.service.path";
	public static final String WORK_DIR = "user.config.workdir";
	public static final String TRANSPORT_SERVER_DOMAIN = "user.transport.server.domain";
	public static final String TRANSPORT_SERVER_PORT = "user.transport.server.port";
	
	private static Map<String, String> settingMap = new HashMap<String, String>();
	
	public static String getConfig(String key){
		return settingMap.get(key);
	}
	
	public static String setConfig(String key, String value){
		return settingMap.put(key, value);
	}
	
	public static synchronized void loadLocal(String userId){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			//create table USER_SETTING(ID varchar(32),USER_ID varchar(32),KEY varchar(100),VALUE varchar(300),UPDATE_TIME varchar(14));
			conn = DataSourceContext.getDataSource().getConnection();
			pstmt = conn.prepareStatement("select KEY,VALUE from USER_SETTING where USER_ID=?");
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				settingMap.put(rs.getString(1), rs.getString(2));
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			DataSourceContext.closeResource(conn, pstmt, rs);
		}
	}
	
	public static synchronized void storageLocal(String userId){
		String currTime = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try{
			conn = DataSourceContext.getDataSource().getConnection();
			pstmt = conn.prepareStatement("delete from USER_SETTING where USER_ID=?");
			pstmt.setString(1, userId);
			pstmt.executeUpdate();
			DataSourceContext.closeResource(null, pstmt, null);
		}catch(Exception e){
			DataSourceContext.closeResource(conn, pstmt, null);
			throw new RuntimeException(e);
		}
		
		try{
			pstmt = conn.prepareStatement("insert into USER_SETTING(ID,USER_ID,KEY,VALUE,UPDATE_TIME) values (?,?,?,?,?)");
			for(Map.Entry<String, String> entry : settingMap.entrySet()){
				pstmt.setString(1, UUID.randomUUID().toString().replaceAll("-", ""));
				pstmt.setString(2, userId);
				pstmt.setString(3, entry.getKey());
				pstmt.setString(4, entry.getValue());
				pstmt.setString(5, currTime);
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			pstmt.clearBatch();
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			DataSourceContext.closeResource(conn, pstmt, null);
		}
	}
	
	public static synchronized void server2local(String userId){
		//TODO server2local
	}
	
	public static synchronized void local2Server(String userId){
		//TODO server2local
	}
}
