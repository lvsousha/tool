package com.lvdousha.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class XmlToWord {

	private static Configuration cfg = null;
	
	public static void main(String[] args) {
		try {  
            // xml的文件名  
            String xmlTemplate = "/office2pdf/Xml2Word/tpl_promise.xml";  
            // docx的路径和文件名  
            String docxTemplate = "/office2pdf/Xml2Word/tpl_promise.docx";  
            // 填充完数据的临时xml  
            String xmlTemp = "/office2pdf/Xml2Word/temp.xml";  
            // 目标文件名  
            String toFilePath = "/office2pdf/Xml2Word/final.docx";  
  
            Writer w = new FileWriter(new File(xmlTemp));  
            // 1.需要动态传入的数据  
            Map<String, Object> p = new HashMap<String, Object>();  
            List<String> students = new ArrayList<String>();  
            students.add("张三");  
            students.add("李四");  
            students.add("王二");  
            p.put("ddeptdept", "研发部门呵呵哒");  
            p.put("ddatedate", "2016-12-15");  
//            p.put("dnamename", students);  
  
            // 2.把map中的数据动态由freemarker传给xml  
            XmlToWord.process(xmlTemplate, p, w);  
  
            // 3.把填充完成的xml写入到docx中  
            XmlToWord xtd = new XmlToWord();  
            xtd.outDocx(new File(xmlTemp), docxTemplate, toFilePath);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
	}
	
	public static void process(String templatefile, Map<String,Object> param, Writer out) throws IOException, TemplateException {  
        // 获取模板  
        Template template = getTemplate(templatefile);  
        template.setOutputEncoding("UTF-8");  
        // 合并数据  
        template.process(param, out);  
        if (out != null) {  
            out.close();  
        }  
    } 
	
	private static Template getTemplate(String name) throws IOException {  
        if (cfg == null) {  
        	cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);  
            try {  
                // 注册tmlplate的load路径  
                // cfg.setClassForTemplateLoading(this.getClass(), "/template/");  
                cfg.setDirectoryForTemplateLoading(new File("/"));  
            } catch (Exception e) {  
      
            } 
        }  
        return cfg.getTemplate(name);  
    }
	
	public void outDocx(File documentFile, String docxTemplate, String toFilePath) throws ZipException, IOException {  
		  
        try {  
            File docxFile = new File(docxTemplate);  
            ZipFile zipFile = new ZipFile(docxFile);  
            Enumeration<? extends ZipEntry> zipEntrys = zipFile.entries();  
            ZipOutputStream zipout = new ZipOutputStream(new FileOutputStream(toFilePath));  
            int len = -1;  
            byte[] buffer = new byte[1024];  
            while (zipEntrys.hasMoreElements()) {  
                ZipEntry next = zipEntrys.nextElement();  
                InputStream is = zipFile.getInputStream(next);  
                // 把输入流的文件传到输出流中 如果是word/document.xml由我们输入  
                zipout.putNextEntry(new ZipEntry(next.toString()));  
                if ("word/document.xml".equals(next.toString())) {  
                    InputStream in = new FileInputStream(documentFile);  
                    while ((len = in.read(buffer)) != -1) {  
                        zipout.write(buffer, 0, len);  
                    }  
                    in.close();  
                } else {  
                    while ((len = is.read(buffer)) != -1) {  
                        zipout.write(buffer, 0, len);  
                    }  
                    is.close();  
                }  
            }  
            zipout.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
	
	
}
