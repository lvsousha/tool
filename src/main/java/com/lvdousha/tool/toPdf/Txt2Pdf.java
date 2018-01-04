package com.lvdousha.tool.toPdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

public class Txt2Pdf {

	
	private static String TXT2PDF = "E:/office2pdf/pdf/txt/";
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Txt2Pdf tp = new Txt2Pdf();
		File file = new File("E:/office2pdf/nanjing/衣服.txt");
		tp.txt2pdf(file, Txt2Pdf.TXT2PDF);
	}

	public String txt2pdf(File file, String target) throws Exception{
		String outfilePath = "";
		String recoding = "UTF-8";
		InputStream in= new java.io.FileInputStream(file);  
		byte[] b = new byte[3];  
		in.read(b);  
		in.close();  
		if (b[0] == -17 && b[1] == -69 && b[2] == -65){
//			System.out.println(file.getName() + "：编码为UTF-8");  
		}
		else{
			recoding = "GBK";
//			System.out.println(file.getName() + "：可能是GBK，也可能是其他编码");
		}
//		System.out.println(FileUtils.readFileToString(file,recoding));
		List<String> contents = FileUtils.readLines(file,recoding);
		if(contents != null && contents.size() > 0){
			outfilePath = target+"/"+file.getName()+".pdf";
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(outfilePath));
			document.open();
			BaseFont bfChinese = BaseFont.createFont( "STSongStd-Light" ,"UniGB-UCS2-H",false );
	        Font font = new Font(bfChinese,12,Font.NORMAL);
			for(String content : contents){
				document.add(new Paragraph(content,font));
			}
			document.close();
		}
		return outfilePath;
	}
}
