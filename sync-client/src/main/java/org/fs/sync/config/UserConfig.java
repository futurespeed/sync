package org.fs.sync.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class UserConfig {
	
	/*sqlite> create table USER_DIR(ID varchar(32), USER_ID varchar(32),CONF_ID varchar(32), PATH varchar(300));*/
	
	public static String getStorageDirPath(String userId, String configId){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			conn = DataSourceContext.getDataSource().getConnection();
			pstmt = conn.prepareStatement("select PATH from USER_DIR where USER_ID=? and CONF_ID=?");
			pstmt.setString(1, userId);
			pstmt.setString(2, configId);
			rs = pstmt.executeQuery();
			return rs.next() ? rs.getString(1) : null;
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			DataSourceContext.closeResource(conn, pstmt, rs);
		}
	}
	
	public static void storageUserDir(String userId, String configId, String path){
		Connection conn = null;
		PreparedStatement pstmt = null;
		try{
			conn = DataSourceContext.getDataSource().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement("delete from USER_DIR where USER_ID=? and CONF_ID=?");
			pstmt.setString(1, userId);
			pstmt.setString(2, configId);
			pstmt.executeUpdate();
			DataSourceContext.closeResource(null, pstmt, null);
		}catch(Exception e){
			DataSourceContext.closeResource(conn, pstmt, null);
			throw new RuntimeException(e);
		}
		
		try{
			pstmt = conn.prepareStatement("insert into USER_DIR(ID,USER_ID,CONF_ID,PATH) values (?,?,?,?)");
			pstmt.setString(1, UUID.randomUUID().toString().replaceAll("-", ""));
			pstmt.setString(2, userId);
			pstmt.setString(3, configId);
			pstmt.setString(4, path);
			pstmt.executeUpdate();
			conn.commit();
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			DataSourceContext.closeResource(conn, pstmt, null);
		}
	}

    public static void deleteUserDir(String userId, String configId){
        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = DataSourceContext.getDataSource().getConnection();
            pstmt = conn.prepareStatement("delete from USER_DIR where USER_ID=? and CONF_ID=?");
            pstmt.setString(1, userId);
            pstmt.setString(2, configId);
            pstmt.executeUpdate();
        }catch(Exception e){
            throw new RuntimeException(e);
        }finally{
            DataSourceContext.closeResource(conn, pstmt, null);
        }
    }

	public static List<Map<String, Object>> getUserDirs(String userId){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			conn = DataSourceContext.getDataSource().getConnection();
			pstmt = conn.prepareStatement("select CONF_ID,PATH from USER_DIR where USER_ID=?");
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			while(rs.next()){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("confId", rs.getString(1));
				map.put("path", rs.getString(2));
				list.add(map);
			}
			return list;
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			DataSourceContext.closeResource(conn, pstmt, rs);
		}
	}
}
