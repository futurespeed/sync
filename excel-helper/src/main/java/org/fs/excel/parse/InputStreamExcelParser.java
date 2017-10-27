package org.fs.excel.parse;

import java.io.InputStream;

import org.fs.excel.common.Context;

public abstract class InputStreamExcelParser implements ExcelParser {
	
	public static void setInput(InputStream in, Context context){
		context.setExtendData(Constants.CONTEXT_ATTR_INPUT_STREAM, in);
	}
	
	public static void setPageSize(long pageSize, Context context){
		context.setExtendData(Constants.CONTEXT_ATTR_PAGE_SIZE, pageSize);
	}
	
	public void parse(Context context){
		InputStream in = (InputStream) context.getExtendData(Constants.CONTEXT_ATTR_INPUT_STREAM);
		read(context, in);
		onReady(context);
		long currPage = 1L;
		context.setExtendData(Constants.CONTEXT_ATTR_CURRENT_PAGE, currPage);
		Long pageSize = (Long) context.getExtendData(Constants.CONTEXT_ATTR_PAGE_SIZE);
		if(null == pageSize){
			pageSize = -1L;
		}
		long rowSize = getRowSize(context);
		long beginIdx = 2;//FIXME beginIdx
		for(long i = beginIdx; i < rowSize; i++){
			context.setExtendData(Constants.CONTEXT_ATTR_CURRENT_ROW_IDX, i);
			onRowRead(context);
			if(pageSize > 0 && 0 == (i - beginIdx + 1) % pageSize){
				onPageChange(context);
				currPage++;
				context.setExtendData(Constants.CONTEXT_ATTR_CURRENT_PAGE, currPage);
			}
		}
		onFinish(context);
	}
	
	protected abstract void read(Context context, InputStream in);
	
	protected abstract long getRowSize(Context context);
	
	protected abstract void onReady(Context context);
	
	protected abstract void onFinish(Context context);
	
	protected abstract void onPageChange(Context context);
	
	protected abstract void onRowRead(Context context);
	
	public static class Constants{
		public static final String CONTEXT_ATTR_INPUT_STREAM = "_input_stream";
		public static final String CONTEXT_ATTR_PAGE_SIZE = "_page_size";
		public static final String CONTEXT_ATTR_CURRENT_PAGE = "_current_page";
		public static final String CONTEXT_ATTR_SHEET = "_sheet";
		public static final String CONTEXT_ATTR_CURRENT_ROW_IDX = "_current_row_idx";
	}
}
