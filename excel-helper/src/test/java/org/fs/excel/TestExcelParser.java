package org.fs.excel;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.fs.excel.common.Context;
import org.fs.excel.parse.PoiExcelParser;
import org.junit.Test;

public class TestExcelParser {
	@Test
	public void testParse() throws Throwable{
		PoiExcelParser parser = new PoiExcelParser(){
			@Override
			protected void onPageChange(Context context) {
				System.out.print("page-" + context.getExtendData(PoiExcelParser.Constants.CONTEXT_ATTR_CURRENT_PAGE) + ": ");
				List list = (List) context.getExtendData(PoiExcelParser.Constants.CONTEXT_ATTR_DATA_LIST);
				System.out.println(list);
				list.clear();
			}
			
			@Override
			protected void onFinish(Context context) {
				List list = (List) context.getExtendData(PoiExcelParser.Constants.CONTEXT_ATTR_DATA_LIST);
				System.out.println("finish: " + list);
				list.clear();
			}
		};
		Context context = new Context();
		InputStream in = new FileInputStream("D:/data/require/营销平台二期优化功能清单-20170705.xlsx");
		PoiExcelParser.setInput(in, context);
		PoiExcelParser.setPageSize(10, context);
		parser.parse(context);
		in.close();
		
	}
}
