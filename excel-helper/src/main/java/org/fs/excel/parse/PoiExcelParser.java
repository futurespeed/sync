package org.fs.excel.parse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.fs.excel.common.Context;

public class PoiExcelParser extends InputStreamExcelParser {
	
	protected void read(Context context, InputStream in) {
		try{
			Workbook wb = WorkbookFactory.create(in);
			Sheet sheet = wb.getSheetAt(0);
			context.setExtendData(Constants.CONTEXT_ATTR_SHEET, sheet);
		}catch(Throwable e){
			throw new RuntimeException(e);
		}
	}
	
	protected long getRowSize(Context context) {
		Long rowSize = (Long) context.getExtendData(Constants.CONTEXT_ATTR_ROW_SIZE);
		if(null == rowSize){
			Sheet sheet = (Sheet) context.getExtendData(Constants.CONTEXT_ATTR_SHEET);
			rowSize = (long) sheet.getLastRowNum();
			if(rowSize >= 0){
				rowSize++;
			}
			context.setExtendData(Constants.CONTEXT_ATTR_ROW_SIZE, rowSize);
		}
		return rowSize;
	}
	
	protected long getColumnSize(Context context){
		//FIXME getColumnSize from configuration
		Long columnSize = (Long) context.getExtendData(Constants.CONTEXT_ATTR_COLUMN_SIZE);
		if(null == columnSize){
			Sheet sheet = (Sheet) context.getExtendData(Constants.CONTEXT_ATTR_SHEET);
			Row nameRow = sheet.getRow(1);
			columnSize = (long) nameRow.getLastCellNum();
			context.setExtendData(Constants.CONTEXT_ATTR_COLUMN_SIZE, columnSize);
		}
		return columnSize;
	}

	
	protected void onReady(Context context) {
		List<Object> list = new ArrayList<>();
		context.setExtendData(Constants.CONTEXT_ATTR_DATA_LIST, list);
		List<Object> errList = new ArrayList<>();
		context.setExtendData(Constants.CONTEXT_ATTR_ERROR_LIST, errList);
	}

	protected void onFinish(Context context) {
	}
	
	protected void onPageChange(Context context) {
	}

	protected void onRowRead(Context context) {
		long rowIdx = (long) context.getExtendData(Constants.CONTEXT_ATTR_CURRENT_ROW_IDX);
		Sheet sheet = (Sheet) context.getExtendData(Constants.CONTEXT_ATTR_SHEET);
		List<Object> list = (List<Object>) context.getExtendData(Constants.CONTEXT_ATTR_DATA_LIST);
		List<Object> errorList = (List<Object>) context.getExtendData(Constants.CONTEXT_ATTR_ERROR_LIST);
		Row row = sheet.getRow((int) rowIdx);
		Map<String, Object> rowData = new HashMap<String, Object>();
		for(long i = 0, len = getColumnSize(context); i < len; i++){
			Cell cell = row.getCell((int) i);
			Object value = columnRead(cell, i);
			boolean valid = validate(value, i, rowData, errorList);
//			if(!continueOnError && !valid){
//				throw new RuntimeException("validate fail");
//			}
			rowData.put("COL_" + i, value);
		}
		list.add(rowData);
	}
	
	protected Object columnRead(Cell cell, long colIdx) {
		if(null == cell){
			return "";
		}
		CellType cellType = cell.getCellTypeEnum();
		if(CellType.STRING.equals(cellType)){
			return cell.getStringCellValue();
		}
		if(CellType.NUMERIC.equals(cellType)){
			//FIXME NUMERIC
			return String.valueOf(cell.getNumericCellValue());
		}
		if(CellType.BOOLEAN.equals(cellType)){
			return cell.getBooleanCellValue() ? "1" : "0";
		}
		return "";
	}
	
	protected boolean validate(Object value, long colIdx, Object rowData, List<Object> errorList){
		//TODO
		return true;
	}
	
	public static class Constants extends InputStreamExcelParser.Constants{
		public static final String CONTEXT_ATTR_ERROR_LIST = "_error_list";
		public static final String CONTEXT_ATTR_DATA_LIST = "_data_list";
		public static final String CONTEXT_ATTR_ROW_SIZE = "_row_size";
		public static final String CONTEXT_ATTR_COLUMN_SIZE = "_column_size";
	}
	

}
