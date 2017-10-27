package org.fs.sync.transfer.data;

import java.util.Arrays;

public class DataFrameSerializer {
	
	private static final String ENCODING = "UTF-8";
	
	public byte[] serialize(DataFrame frame){
		try{
			byte[] data = new byte[frame.getSize() + 88];
			byte[] id = frame.getId().getBytes(ENCODING);
			byte[] type = Integer.toHexString(frame.getType()).getBytes(ENCODING);
			byte[] size = Integer.toHexString(frame.getSize()).getBytes(ENCODING);
			byte[] dataId = frame.getDataId().getBytes(ENCODING);
			byte[] seq = Integer.toHexString(frame.getSeq()).getBytes(ENCODING);
			System.arraycopy(id, 0, data, 0, id.length);
			System.arraycopy(type, 0, data, 32, type.length);
			System.arraycopy(size, 0, data, 32 + 4, size.length);
			System.arraycopy(dataId, 0, data, 32 + 4 + 10, dataId.length);
			System.arraycopy(seq, 0, data, 32 + 4 + 10 + 32, seq.length);
			System.arraycopy(frame.getData(), 0, data, 32 + 4 + 10 + 32 + 10, frame.getSize());
			return data;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public DataFrame deserialize(byte[] data){
		try{
			DataFrame frame = new DataFrame();
			byte[] id = arrayTrimEmpty(Arrays.copyOfRange(data, 0, 32));
			byte[] type = arrayTrimEmpty(Arrays.copyOfRange(data, 32, 36));
			byte[] size = arrayTrimEmpty(Arrays.copyOfRange(data, 36, 46));
			byte[] dataId = arrayTrimEmpty(Arrays.copyOfRange(data, 46, 78));
			byte[] seq = arrayTrimEmpty(Arrays.copyOfRange(data, 78, 88));
			
			frame.setId(new String(id, ENCODING));
			frame.setType(Integer.valueOf(new String(type, ENCODING), 16));
			frame.setSize(Integer.valueOf(new String(size, ENCODING), 16));
			frame.setDataId(new String(dataId, ENCODING));
			frame.setSeq(Integer.valueOf(new String(seq, ENCODING), 16));
			frame.setData(Arrays.copyOfRange(data, 88, data.length));
			return frame;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	protected byte[] arrayTrimEmpty(byte[] arr){
		for(int i = 0, len = arr.length; i < len; i++){
			if(0 == arr[i]){
				return Arrays.copyOfRange(arr, 0, i);
			}
		}
		return arr;
	}
}
