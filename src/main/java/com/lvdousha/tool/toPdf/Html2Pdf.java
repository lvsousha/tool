package com.lvdousha.tool.toPdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.pdf.BaseFont;

public class Html2Pdf {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		File file = new File("e:/test.html");
		Html2Pdf.html2Pdf(file, "e:/test.pdf","e:/office2pdf/pdf/word/java笔试重要知识点深度解析/images");
	}
	
	public static void html2Pdf(File inputFile, String outputFile, String imagespath) throws Exception{  
		String content = FileUtils.readFileToString(inputFile, "UTF-8");
		content = content.replaceAll("font-family:[^;]*", "font-family:SimSun");
		content = content.replaceAll("text-align:end", "text-align:right");
		Document doc = Jsoup.parse(content);
		Element style = doc.getElementsByTag("style").get(0);
		style.prepend("body{font-family:SimSun;} ");
		style.prepend("@page{size:700mm 297mm;}");
		OutputStream os = new FileOutputStream(outputFile);       
        ITextRenderer renderer = new ITextRenderer();       
//		FileUtils.write(inputFile, doc.html().replaceAll("&nbsp;", "&#160;"));
        renderer.setDocumentFromString(doc.html().replaceAll("&nbsp;", "&#160;"));
        // 解决中文支持问题       
        ITextFontResolver fontResolver = renderer.getFontResolver();      
        fontResolver.addFont("E:/office2pdf/font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);     
        fontResolver.addFont("E:/office2pdf/font/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);    
        //解决图片的相对路径问题    windows 路径
        imagespath = "file:/"+imagespath.replaceAll("\\\\", "/")+"/";
        //解决图片的相对路径问题   相对路径
//        imagespath = imagespath.replaceAll("\\\\", "/")+"/";
        renderer.getSharedContext().setBaseURL(imagespath);  
        renderer.layout();
        renderer.createPDF(os);
          
        os.flush();  
        os.close();  
	}
	public void html2Pdf2(String inputFile, String outputFile) throws Exception{
		
	}
	

}
