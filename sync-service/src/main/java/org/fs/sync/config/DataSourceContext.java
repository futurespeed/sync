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
			overrides.put("maxStatements", "200");// Stringified property values work  
			overrides.put("maxPoolSize", new Integer(50));// "boxed primitives" also work  
			  
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
}
