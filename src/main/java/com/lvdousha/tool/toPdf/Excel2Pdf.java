package com.lvdousha.tool.toPdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Excel2Pdf {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String source = "E:/office2pdf/execl/excel.xlsx";
		String target = "E:/office2pdf/pdf/excel.pdf";
		File file = new File(source);
		Excel2Pdf ep = new Excel2Pdf();
		ep.excel2pdf(file,target);
	}
	
	public String excel2pdf(File file, String target) throws Exception{
		String outfilePath = target+"/"+file.getName()+".pdf";
		Map<Integer, List<String>> contents = readExcelContent(file.getPath());
		int columnNum = contents.get(contents.keySet().iterator().next()).size();
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(outfilePath));
		document.open();

		PdfPTable table = new PdfPTable(columnNum);// 建立一个pdf表格  
        table.setSpacingBefore(20f);  
        table.setWidthPercentage(100);// 设置表格宽度为100%  
        
        BaseFont bfChinese = BaseFont.createFont( "STSongStd-Light" ,"UniGB-UCS2-H",false );
        Font font = new Font(bfChinese,12,Font.NORMAL);
        for(Integer i : contents.keySet()){
        	for(String content : contents.get(i)){
        		PdfPCell cell = new PdfPCell(new Phrase(content,font));  
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
        	}
        }
        document.add(table);
		document.close();
		return outfilePath;
	}
	
	/**
     * 读取Excel数据内容
     * @param sourceFileName
     * @return Map 包含单元格数据内容的Map对象
     */
    public Map<Integer, List<String>> readExcelContent(String source) {
        Workbook wb;
        Sheet sheet;
        Row row;
        Map<Integer, List<String>> contents = new HashMap<>();
        try {
        	InputStream is = new FileInputStream(source);
            try {
            	wb = new XSSFWorkbook(new FileInputStream(source));
            } catch (Exception ex) {
//            	ex.printStackTrace();
            	wb = new HSSFWorkbook(new FileInputStream(source));
            }
            sheet = wb.getSheetAt(0);
            int rowNum = sheet.getLastRowNum();// 得到总行数
            int colNum = 0;
            for(int i = 0; i <= rowNum; i++){
            	row = sheet.getRow(i);
            	if(row != null){
            		if(colNum < row.getLastCellNum()){
            			colNum = row.getLastCellNum();
            		}
            	}
            }
            for (int i = 0; i <= rowNum; i++) {// 正文内容应该从第二行开始,第一行为表头的标题
                List<String> content = new ArrayList<>();
            	row = sheet.getRow(i);
            	if(row != null){
            		for(int j=0;j<colNum;j++){
            			content.add(getCellFormatValue(row.getCell(j)));
            		}
            	}else{
            		for(int j=0;j<colNum;j++){
            			content.add("");
            		}
            	}
                contents.put(i, content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contents;
    }

    /**
     * 根据HSSFCell类型设置数据
     * @param cell
     * @return
     */
    private String getCellFormatValue(Cell cell) {
        String cellvalue = "";
        if (cell != null) {          
            switch (cell.getCellType()) {// 判断当前Cell的Type            
            case HSSFCell.CELL_TYPE_NUMERIC:// 如果当前Cell的Type为NUMERIC
            case HSSFCell.CELL_TYPE_FORMULA: {               
                if (HSSFDateUtil.isCellDateFormatted(cell)) {// 判断当前的cell是否为Date
                    // 如果是Date类型则，转化为Data格式                   
                    //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                    //cellvalue = cell.getDateCellValue().toLocaleString();                    
                    //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cellvalue = sdf.format(date);                    
                }else{ // 如果是纯数字                  
                    cellvalue = String.valueOf(cell.getNumericCellValue());// 取得当前Cell的数值
                }
                break;
            }           
            case HSSFCell.CELL_TYPE_STRING:// 如果当前Cell的Type为STRING                
                cellvalue = cell.getRichStringCellValue().getString();// 取得当前的Cell字符串
                break;           
            default:// 默认的Cell值
                cellvalue = "";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }

}
