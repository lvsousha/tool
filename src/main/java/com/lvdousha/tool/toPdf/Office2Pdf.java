package com.lvdousha.tool.toPdf;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class Office2Pdf {
	
	private static String WORD2PDF = "E:/office2pdf/pdf/word/";
	private static String EXCEL2PDF = "E:/office2pdf/pdf/excel/";
	private static String TXT2PDF = "E:/office2pdf/pdf/txt/";
	private static String PPT2PDF = "E:/office2pdf/pdf/ppt/";
	private static String PDF2PDF = "E:/office2pdf/pdf/pdf/";
	private List<Map<String,Object>> list = new ArrayList<>();
	private Logger log = Logger.getLogger(this.getClass());
	static{
		File file1 = new File(WORD2PDF);
		File file2 = new File(EXCEL2PDF);
		File file3 = new File(TXT2PDF);
		File file4 = new File(PPT2PDF);
		File file5 = new File(PDF2PDF);
		if(!file1.exists()){
			file1.mkdirs();
		}
		if(!file2.exists()){
			file2.mkdirs();
		}
		if(!file3.exists()){
			file3.mkdirs();
		}
		if(!file4.exists()){
			file4.mkdirs();
		}
		if(!file5.exists()){
			file5.mkdirs();
		}
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Office2Pdf jb = new Office2Pdf();
		jb.transformFolder("E:/office2pdf/nanjing");
	}
	
	public void transformFolder(String path) throws Exception {
		File fold = new File(path);
		String filePath = "";
		if (fold.exists()) {
			File[] files = fold.listFiles();
			if (files.length == 0) {
				log.info("文件夹是空的!");
				return;
			} else {
				for (File file : files) {
					filePath = file.getAbsolutePath();
					if (file.isDirectory()) {
						log.info("文件夹:" + filePath);
						transformFolder(filePath);
					} else {
						log.info("文件:" + filePath);
						transformSingle(filePath);
					}
				}
			}
		} else {
			log.info("文件不存在!");
		}
	}
	
	public Map<String,Object> transformSingle(String source) throws Exception{
		Map<String,Object> map = new HashMap<>();
		File file = new File(source);
		String outfilePath = "";
		if(source.lastIndexOf(".doc") != -1 || source.lastIndexOf(".docx") != -1){
			Word2Pdf wp = new Word2Pdf();
			outfilePath = wp.word2pdf(file, WORD2PDF);
		}else if(source.lastIndexOf(".xls") != -1 || source.lastIndexOf(".xlsx") != -1 ){
			Excel2Pdf ep = new Excel2Pdf();
			outfilePath = ep.excel2pdf(file, EXCEL2PDF);
		}else if(source.lastIndexOf(".ppt") != -1 || source.lastIndexOf(".pptx") != -1 ){
			Ppt2Pdf pp = new Ppt2Pdf();
			outfilePath = pp.ppt2pdf(file, PPT2PDF, source.substring(source.indexOf("."),source.length()));
		}else if(source.lastIndexOf(".pdf") != -1){
			outfilePath = PDF2PDF+file.getName();
			FileUtils.copyFile(file, new File(outfilePath));
		}else if(source.lastIndexOf(".txt") != -1){
			Txt2Pdf tp = new Txt2Pdf();
			outfilePath = tp.txt2pdf(file, TXT2PDF);
		}
		if(outfilePath != null && !outfilePath.equals("")){
			PdfReader reader = new PdfReader(outfilePath);
			PdfReaderContentParser parser = new PdfReaderContentParser(reader);  
			StringBuffer buff = new StringBuffer(); 
			TextExtractionStrategy strategy;  
			for (int i = 1; i <= reader.getNumberOfPages(); i++) {  
				strategy = parser.processContent(i,new SimpleTextExtractionStrategy());buff.append(strategy.getResultantText());  
			}
			map.put("pages", reader.getNumberOfPages());
			if(buff.length() > 200){
				map.put("content", buff.substring(0,200));
			}else{
				map.put("content", buff.toString());
			}
			if(outfilePath.lastIndexOf(".ppt") != -1 || outfilePath.lastIndexOf(".pptx") != -1){
				System.out.println(outfilePath);
				map.put("content", readPpt(source));
			}
		}
		list.add(map);
		log.info(map);
		return map;
	}
	
	public String readPpt(String source){
		String content = "";
		try {
			content = new PowerPointExtractor(new FileInputStream(new File(source))).getText();
		} catch (Exception e) {
			try {
				content = new XSLFPowerPointExtractor(POIXMLDocument.openPackage(source)).getText();
			} catch (Exception e1) {
				log.error(source,e);
				log.error(source,e1);
			}
		} 
		return content;
	}

}
