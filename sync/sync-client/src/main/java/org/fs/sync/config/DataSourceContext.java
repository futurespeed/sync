package org.fs.sync.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.DataSources;

public class DataSourceContext {
	
	private static final Logger LOG = LoggerFactory.getLogger(DataSourceContext.class);
	
	private static String connectString = "jdbc:sqlite:config.db";
	
	private static DataSource ds;
	
	public static void setConnectString(String connectString) {
		DataSourceContext.connectString = connectString;
	}

	public static synchronized void init(){
		if(ds != null){
			return;
		}
		try{
			DataSource ds_unpooled = DataSources.unpooledDataSource(connectString);  
			Map<String, Object> overrides = new HashMap<String, Object>();  
			overrides.put("maxStatements", "50");// Stringified property values work  
			overrides.put("maxPoolSize", new Integer(10));// "boxed primitives" also work  
			  
			// create the PooledDataSource using the default configuration and our overrides  
			ds = DataSources.pooledDataSource(ds_unpooled, overrides);  
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public static DataSource getDataSource(){
		return ds;
	}
	
	public static void closeResource(Connection conn, Statement stmt, ResultSet rs){
		if(rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				LOG.warn("[db] fail to close resultset", e);
			}
		}
		if(stmt != null){
			try {
				stmt.close();
			} catch (SQLException e) {
				LOG.warn("[db] fail to close statement", e);
			}
		}
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				LOG.warn("[db] fail to close connection", e);
			}
		}
	}
	
	protected static void checkUserTables(){
		String sql = "select count(1) from sqlite_master where type='table' and name in ('USER_DIR','USER_SETTING')";//must return 2
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try{
			conn = DataSourceContext.getDataSource().getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			int result = rs.next() ? rs.getInt(1) : 0;
			if(result != 2){
				throw new RuntimeException("user table not create!");
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			DataSourceContext.closeResource(conn, stmt, rs);
		}
	}
	
	public static void initUserTables(){
		try{
			checkUserTables();
			return;
		}catch(Exception e){
			LOG.warn("fail to find user tables", e);
		}
		LOG.info("create user tables...");
		String[] sqls = new String[]{
				"create table USER_DIR(ID varchar(32), USER_ID varchar(32),CONF_ID varchar(32), PATH varchar(300))",
				"create table USER_SETTING(ID varchar(32),USER_ID varchar(32),KEY varchar(100),VALUE varchar(300),UPDATE_TIME varchar(14))"
		};
		Connection conn = null;
		Statement stmt = null;
		
		try{
			conn = DataSourceContext.getDataSource().getConnection();
			stmt = conn.createStatement();
			for(String sql : sqls){
				stmt.execute(sql);
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			DataSourceContext.closeResource(conn, stmt, null);
		}
	}
}
