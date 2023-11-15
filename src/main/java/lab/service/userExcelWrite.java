package lab.service;

import lab.vo.userVO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class userExcelWrite {
	private static final Logger log = LoggerFactory.getLogger(userExcelWrite.class);
	
	public InputStream userExcelWrite(List<userVO> userList) {
		int sheet_lenth = 1;  
		//int row_index = 1;   
		SXSSFWorkbook wb = new SXSSFWorkbook(1000);
		wb.setCompressTempFiles(false);  
		SXSSFSheet sheet = wb.createSheet("sheet" + sheet_lenth);
		
		//第一行内容输入标题
		SXSSFRow row = sheet.createRow(0);
		if(isRowEmpty(row)){
			CellStyle style1 = wb.createCellStyle();
			style1.setVerticalAlignment(VerticalAlignment.CENTER);
			style1.setAlignment(HorizontalAlignment.CENTER);
			row.setRowStyle(style1);

			row.createCell(0).setCellValue("姓名");
			row.createCell(1).setCellValue("用户名");
			row.createCell(2).setCellValue("密码");
			row.createCell(3).setCellValue("主管id");
			row.createCell(4).setCellValue("主管名称");
		}
		
		for(int j = 1;j < userList.size() + 1;j ++){
        	row = sheet.createRow(j);
        	row.createCell(0).setCellValue(userList.get(j - 1).getName());
        	row.createCell(1).setCellValue(userList.get(j - 1).getUsername());
        	row.createCell(2).setCellValue(userList.get(j - 1).getPassword());
		}

		InputStream Ins = null;
		try {
			//临时缓冲区
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			//创建临时文件
			wb.write(out);
			byte [] bookByteAry = out.toByteArray();
			Ins = new ByteArrayInputStream(bookByteAry);
		}catch (IOException e) {
			log.warn(e.toString());
		}finally {
		    try{
			    wb.close();
		    }catch (IOException e) {
				log.warn(e.toString());
		    }
		}
		
		return Ins;
	}
	
	@SuppressWarnings("deprecation")
	public static boolean isRowEmpty(Row row){
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK){
                return false;
            }
        }
        return true;
	}
}
