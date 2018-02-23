package org.fs.sync.transfer.data;

import java.io.Serializable;

public class DataFrame implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	public static final int TYPE_FI = 3000;
	public static final int TYPE_FC = 3001;
	private String id;//32
	private int type;//4
	private int size;//10
	private String dataId;//32
	private int seq;//10
	private byte[] data;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "{id:" + id + ",type:" + type + ",size:" + size + ",dataId:" + dataId + ",seq:" + seq + "}" ;
	}
}
