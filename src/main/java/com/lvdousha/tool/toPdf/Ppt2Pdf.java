package com.lvdousha.tool.toPdf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.RichTextRun;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Ppt2Pdf {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String source = "E:/office2pdf/nanjing/microppt.pptx";
		String target = "E:/office2pdf/pdf/ppt/";
		File file = new File(source);
		Ppt2Pdf pp = new Ppt2Pdf();
		pp.ppt2pdf(file, target, ".pptx");

	}

	public String ppt2pdf(File file, String target, String fileType) throws Exception {
		String outfilePath  = target+"/"+file.getName()+".pdf";
		FileInputStream inputStream = new FileInputStream(file);
		double zoom = 2;
		AffineTransform at = new AffineTransform();
		at.setToScale(zoom, zoom);
		Document pdfDocument = new Document();
		PdfWriter pdfWriter = PdfWriter.getInstance(pdfDocument, new FileOutputStream(outfilePath));
		PdfPTable table = new PdfPTable(1);
		pdfWriter.open();
		pdfDocument.open();
		Dimension pgsize = null;
		Image slideImage = null;
		BufferedImage img = null;
		if (fileType.equalsIgnoreCase(".ppt")) {
			SlideShow ppt = new SlideShow(inputStream);
			inputStream.close();
			pgsize = ppt.getPageSize();
			Slide[] slides = ppt.getSlides();
			pdfDocument.setPageSize(new Rectangle((float) pgsize.getWidth(), (float) pgsize.getHeight()));
			pdfWriter.open();
			pdfDocument.open();
			for (int i = 0; i < slides.length; i++) {
				TextRun[] truns = slides[i].getTextRuns();
				for (int k = 0; k < truns.length; k++) {
					RichTextRun[] rtruns = truns[k].getRichTextRuns();
					for (int l = 0; l < rtruns.length; l++) {
//				      int index = rtruns[l].getFontIndex();  
//				      String name = rtruns[l].getFontName();
//				      System.out.println(index+"==="+name);
						rtruns[l].setFontIndex(1);
						rtruns[l].setFontName("宋体");
					}
				}

				img = new BufferedImage((int) Math.ceil(pgsize.width * zoom), (int) Math.ceil(pgsize.height * zoom),
						BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics = img.createGraphics();
				graphics.setTransform(at);

				graphics.setPaint(Color.white);
				graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
				slides[i].draw(graphics);
				graphics.getPaint();
				slideImage = Image.getInstance(img, null);
				table.addCell(new PdfPCell(slideImage, true));
			}
		}
		if (fileType.equalsIgnoreCase(".pptx")) {
			XMLSlideShow ppt = new XMLSlideShow(inputStream);
			pgsize = ppt.getPageSize();
			XSLFSlide slide[] = ppt.getSlides();
			pdfDocument.setPageSize(new Rectangle((float) pgsize.getWidth(), (float) pgsize.getHeight()));
			pdfWriter.open();
			pdfDocument.open();

			for (int i = 0; i < slide.length; i++) {
				for (XSLFShape shape : slide[i].getShapes()) {
					if (shape instanceof XSLFTextShape) {
						XSLFTextShape txtshape = (XSLFTextShape) shape;
						for (XSLFTextParagraph textPara : txtshape.getTextParagraphs()) {
							List<XSLFTextRun> textRunList = textPara.getTextRuns();
							for (XSLFTextRun textRun : textRunList) {
								textRun.setFontFamily("宋体");
							}
						}
					}
				}
				img = new BufferedImage((int) Math.ceil(pgsize.width * zoom), (int) Math.ceil(pgsize.height * zoom),
						BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics = img.createGraphics();
				graphics.setTransform(at);
				graphics.setPaint(Color.white);
				graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
				slide[i].draw(graphics);
				graphics.getPaint();
				slideImage = Image.getInstance(img, null);
				table.addCell(new PdfPCell(slideImage, true));
			}
		}
		pdfDocument.add(table);
		pdfDocument.close();
		pdfWriter.close();
		return outfilePath;
	}

}
