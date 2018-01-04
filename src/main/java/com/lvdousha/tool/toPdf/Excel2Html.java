package com.lvdousha.tool.toPdf;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Excel2Html {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String pdfFilePath = "/2016.1-11执行案件数据统计.xls";
		String outputFile = "/test.html";
		InputStream is = new FileInputStream(pdfFilePath);

        HSSFWorkbook excelBook = new HSSFWorkbook(is);

        ExcelToHtmlConverter ethc = new ExcelToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        ethc.setOutputColumnHeaders(false);
        ethc.setOutputRowNumbers(false);

        ethc.processWorkbook(excelBook);

        org.w3c.dom.Document htmlDocument = ethc.getDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(out);
        
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        out.close();
        
        String htmlStr = new String(out.toByteArray());
        
        htmlStr = htmlStr.replace("<h2>Sheet1</h2>", "")
                         .replace("<h2>Sheet2</h2>", "")
                         .replace("<h2>Sheet3</h2>", "")
                         .replace("<h2>Sheet4</h2>", "")
                         .replace("<h2>Sheet5</h2>", "");
        
        writeFile(htmlStr, outputFile);
	}
	
	public static void writeFile(String content, String path) {
        FileOutputStream fos = null;
        BufferedWriter bw = null;

        File file = new File(path);

        try {
            fos = new FileOutputStream(file);

            bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
            bw.write(content);
        } catch (FileNotFoundException ex) {
        } catch (UnsupportedEncodingException ex) {
        } catch (IOException ex) {
        } finally {
            try {
                if (null != bw) {
                    bw.close();
                }
                if (null != fos) {
                    fos.close();
                }
            } catch (IOException ex) {
            }

        }
    }

}
