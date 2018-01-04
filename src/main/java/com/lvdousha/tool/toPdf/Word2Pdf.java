package com.lvdousha.tool.toPdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.w3c.dom.Document;

public class Word2Pdf {
	public static void main(String[] args) throws Throwable {
		Word2Pdf wp = new Word2Pdf();
		String path = "E:/office2pdf/nanjing/java笔试重要知识点深度解析.doc";
		File file = new File(path);
		wp.word2pdf(file, "e:/office2pdf/pdf/word/");
	}

	public String word2pdf(File file, String target) throws Exception{
		long startTime = System.currentTimeMillis();
		String fileName = file.getName();
		File targetFold = new File(target+"/"+fileName.substring(0,fileName.indexOf(".")));
		File imagesFold = new File(target+"/"+fileName.substring(0,fileName.indexOf("."))+"/images");
		if(!targetFold.exists()){
			targetFold.mkdirs();
		}
		if(!imagesFold.exists()){
			imagesFold.mkdirs();
		}
		try{
			doc2html(file, targetFold.getPath(),imagesFold.getPath(), fileName.substring(0,fileName.indexOf("."))+".html");
		}catch(Exception e){
			docx2html(file, targetFold.getPath(),imagesFold.getPath(), fileName.substring(0,fileName.indexOf("."))+".html");
		}
		Html2Pdf.html2Pdf(
				new File(targetFold.getPath()+"/"+fileName.substring(0,fileName.indexOf("."))+".html"), 
				target+"/"+fileName+".pdf",imagesFold.getPath());
        System.out.println( "Generate " + fileName + " with " + ( System.currentTimeMillis() - startTime ) + " ms." ); 
        return target+"/"+fileName+".pdf";
	}
	
	public void doc2html(File file, String targetPath, String imagesPath, String fileName) throws Exception{
		InputStream input = new FileInputStream(file);
		HWPFDocument wordDocument = new HWPFDocument(input);
		WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
				DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
		wordToHtmlConverter.setPicturesManager(new PicturesManager() {
			public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches,float heightInches) {
				return suggestedName;
			}
		});
		wordToHtmlConverter.processDocument(wordDocument);
		List<Picture> pics = wordDocument.getPicturesTable().getAllPictures();
		System.out.println(pics.size());
		if (pics != null) {
			for (int i = 0; i < pics.size(); i++) {
				Picture pic = (Picture) pics.get(i);
				try {
					pic.writeImageContent(new FileOutputStream(imagesPath+"/"+pic.suggestFullFileName()));
					System.out.println(imagesPath+"/"+pic.suggestFullFileName());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		Document htmlDocument = wordToHtmlConverter.getDocument();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		DOMSource domSource = new DOMSource(htmlDocument);
		StreamResult streamResult = new StreamResult(outStream);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer serializer = tf.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.METHOD, "html");
		serializer.transform(domSource, streamResult);
		outStream.close();
		String content = new String(outStream.toByteArray());
		FileUtils.write(new File(targetPath, fileName), content, "utf-8");
	}
	
	
	public void docx2html(File file, String targetPath, String imagesPath, String fileName) throws Exception{
        String fileOutName = targetPath +"/"+fileName;  
        XWPFDocument document = new XWPFDocument(new FileInputStream(file));  
        XHTMLOptions options = XHTMLOptions.create();// .indent( 4 );  
        // Extract image  
        File imageFolder = new File(imagesPath);  
        options.setExtractor(new FileImageExtractor(imageFolder));  
        // URI resolver  
        options.URIResolver(new FileURIResolver(imageFolder));  
        OutputStream out = new FileOutputStream(new File(fileOutName));  
        XHTMLConverter.getInstance().convert(document, out, options);   
	}
	
}
