package org.fs.excel.common;

import java.util.HashMap;
import java.util.Map;

public class Context {
	private String flowNo;
	private String createTime;
	private String code;
	private String result;
	private Map<String, Object> extendDataMap = new HashMap<String, Object>();
	public String getFlowNo() {
		return flowNo;
	}
	public void setFlowNo(String flowNo) {
		this.flowNo = flowNo;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Object getExtendData(String key){
		return this.extendDataMap.get(key);
	}
	public void setExtendData(String key, Object value){
		this.extendDataMap.put(key, value);
	}
}
