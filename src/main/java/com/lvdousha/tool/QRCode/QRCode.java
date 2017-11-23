package com.lvdousha.tool.QRCode;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCode {

	private static Logger log = Logger.getRootLogger();
	
	public static void main(String[] args) throws Exception {
		String text = "zhenchanin+3310211993022kkxxxx+男+18823423789"; // 二维码内容
		String src = System.getProperty("user.dir")+"/src/main/resources/logo.jpg";
		String des = "d:" + File.separator + "new.jpg";
		QRCode.createQRCode(text, des, src);
	}

	private static final int SCALING_RATE = 6;
	private static final int FRAME_WIDTH = 2;
	private static final int WIDTH = 300;
	private static final int HEIGHT = 300;

	/**
	 * 生成二维码图片
	 * 
	 * @param content
	 *            内容
	 * @param destImagePath
	 *            目标文件夹
	 * @param srcImagePath
	 *            logo图片，用户头像
	 * @throws WriterException 
	 * @throws IOException 
	 */
	public static void createQRCode(String content, String destPath, String srcImagePath) throws Exception{
		File file2 = new File(destPath);
		ImageIO.write(genBarcode(content, srcImagePath), "jpg", file2);
	}

	public static BufferedImage genBarcode(String content, String srcImagePath) throws WriterException, IOException {
		int IMAGE_WIDTH = WIDTH / SCALING_RATE;
		int IMAGE_HEIGHT = HEIGHT / SCALING_RATE;
		int IMAGE_HALF_WIDTH = IMAGE_WIDTH / 2;
		BufferedImage scaleImage = scale(srcImagePath, IMAGE_WIDTH, IMAGE_HEIGHT, true);
		int[][] srcPixels = new int[IMAGE_WIDTH][IMAGE_HEIGHT];
		for (int i = 0; i < scaleImage.getWidth(); i++) {
			for (int j = 0; j < scaleImage.getHeight(); j++) {
				srcPixels[i][j] = scaleImage.getRGB(i, j);
			}
		}
		Map<EncodeHintType, Object> hint = new HashMap<EncodeHintType, Object>();
		hint.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		// 生成二维码
		MultiFormatWriter mutiWriter = new MultiFormatWriter();
		BitMatrix matrix = mutiWriter.encode(content, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hint);
		// 二维矩阵转为一维像素数组
		int halfW = matrix.getWidth() / 2;
		int halfH = matrix.getHeight() / 2;
		int[] pixels = new int[WIDTH * HEIGHT];
		for (int y = 0; y < matrix.getHeight(); y++) {
			for (int x = 0; x < matrix.getWidth(); x++) {
				// 左上角颜色,根据自己需要调整颜色范围和颜色
				if (x > 0 && x < 100 && y > 0 && y < 100) {
					Color color = new Color(231, 144, 56);
					int colorInt = color.getRGB();
					pixels[y * WIDTH + x] = matrix.get(x, y) ? colorInt : 16777215;
				}
				// 读取图片
				else if (x > halfW - IMAGE_HALF_WIDTH && x < halfW + IMAGE_HALF_WIDTH && y > halfH - IMAGE_HALF_WIDTH
						&& y < halfH + IMAGE_HALF_WIDTH) {
					pixels[y * WIDTH + x] = srcPixels[x - halfW + IMAGE_HALF_WIDTH][y - halfH + IMAGE_HALF_WIDTH];
				} else if ((x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH && x < halfW - IMAGE_HALF_WIDTH + FRAME_WIDTH
						&& y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH + IMAGE_HALF_WIDTH + FRAME_WIDTH)
						|| (x > halfW + IMAGE_HALF_WIDTH - FRAME_WIDTH && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
								&& y > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH
								&& y < halfH + IMAGE_HALF_WIDTH + FRAME_WIDTH)
						|| (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
								&& y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH
								&& y < halfH - IMAGE_HALF_WIDTH + FRAME_WIDTH)
						|| (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
								&& y > halfH + IMAGE_HALF_WIDTH - FRAME_WIDTH
								&& y < halfH + IMAGE_HALF_WIDTH + FRAME_WIDTH)) {
					pixels[y * WIDTH + x] = 0xfffffff;
					// 在图片四周形成边框
				} else {
					// 二维码颜色
					int num1 = (int) (50 - (50.0 - 13.0) / matrix.getHeight() * (y + 1));
					int num2 = (int) (165 - (165.0 - 72.0) / matrix.getHeight() * (y + 1));
					int num3 = (int) (162 - (162.0 - 107.0) / matrix.getHeight() * (y + 1));
					Color color = new Color(num1, num2, num3);
					int colorInt = color.getRGB();
					// 此处可以修改二维码的颜色，可以分别制定二维码和背景的颜色；
					pixels[y * WIDTH + x] = matrix.get(x, y) ? colorInt : 16777215;
					// 0x000000:0xffffff
				}
				if(x<26 || x>274 || y<26 || y>274){
					if(x%4 == 0){
						pixels[y * WIDTH + x] = 0xffff0000;
					}else{
						pixels[y * WIDTH + x] = 0xffffffff;
					}
				}
				if((x-150)*(x-150)+(y-150)*(y-150)>125*125*2){
						pixels[y * WIDTH + x] = 0xff00ff00;
				}
			}
		}
//		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
//		image.getRaster().setDataElements(0, 0, WIDTH, HEIGHT, pixels);
		
		int realWidth = matrix.getEnclosingRectangle()[2];
		int realHeight = matrix.getEnclosingRectangle()[3];
		BufferedImage bi1 = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		bi1.getRaster().setDataElements(0, 0, WIDTH, HEIGHT, pixels);
		//这种是黑色底的  
		Double w = new Double(realWidth*Math.sqrt(2));
		Double h = new Double(realHeight*Math.sqrt(2));
//	    BufferedImage bi2 = new BufferedImage(w.intValue(),h.intValue(),BufferedImage.TYPE_INT_RGB);   
	      
	    //透明底的图片  
	    BufferedImage bi2 = new BufferedImage(bi1.getWidth(),bi1.getHeight(),BufferedImage.TYPE_INT_ARGB);   
	    Ellipse2D.Double shape = new Ellipse2D.Double(0,0,w.intValue(),h.intValue());    
//	    Ellipse2D.Double shape = new Ellipse2D.Double((int)(w-300)/2,(int)(h-300)/2,400,400);  
	    Graphics2D g2 = bi2.createGraphics();  
	    bi2 = g2.getDeviceConfiguration().createCompatibleImage(w.intValue(),h.intValue(), Transparency.TRANSLUCENT);
	    g2.dispose();
        g2 = bi2.createGraphics();
//	    g2 = bi2.createGraphics();
	    g2.fill(new Rectangle(w.intValue(), h.intValue())); 
//	    g2.setClip(shape);   
//	    g2.setBackground(Color.GREEN);  
//	    g2.setComposite(AlphaComposite.Clear);
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.fillRoundRect(0, 0,w.intValue(), h.intValue(), 360, 360);
	    g2.setComposite(AlphaComposite.SrcIn);
	    // 使用 setRenderingHint 设置抗锯齿  
	    g2.drawImage(bi1,(int)(w-bi1.getWidth())/2,(int)(h-bi1.getHeight())/2,null);   
	    //设置颜色  
	    g2.dispose();  
	    
//	    int[] c = new int[w.intValue()*h.intValue()];
//	    bi2.getRaster().getPixels(0, 0, w.intValue(), h.intValue(), c);
//	    for (int y = 0; y < h.intValue(); y++) {
//			for (int x = 0; x < w.intValue(); x++) {
//				
//			}
//	    }
	    return bi2;
		
//		return image;
	}

	public static BufferedImage genBarcode(String content) throws WriterException, IOException {
		Map<EncodeHintType, Object> hint = new HashMap<EncodeHintType, Object>();
		hint.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		// 生成二维码
		MultiFormatWriter mutiWriter = new MultiFormatWriter();
		BitMatrix matrix = mutiWriter.encode(content, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hint);
		// 二维矩阵转为一维像素数组
		int[] pixels = new int[WIDTH * HEIGHT];
		for (int y = 0; y < matrix.getHeight(); y++) {
			for (int x = 0; x < matrix.getWidth(); x++) {
				// 左上角颜色,根据自己需要调整颜色范围和颜色
				if (x > 0 && x < 100 && y > 0 && y < 100) {
					Color color = new Color(231, 144, 56);
					int colorInt = color.getRGB();
					pixels[y * WIDTH + x] = matrix.get(x, y) ? colorInt : 16777215;
				} else {
					// 二维码颜色
					int num1 = (int) (50 - (50.0 - 13.0) / matrix.getHeight() * (y + 1));
					int num2 = (int) (165 - (165.0 - 72.0) / matrix.getHeight() * (y + 1));
					int num3 = (int) (162 - (162.0 - 107.0) / matrix.getHeight() * (y + 1));
					Color color = new Color(num1, num2, num3);
					int colorInt = color.getRGB();
					// 此处可以修改二维码的颜色，可以分别制定二维码和背景的颜色；
					pixels[y * WIDTH + x] = matrix.get(x, y) ? colorInt : 16777215;
					// 0x000000:0xffffff
				}
			}
		}
		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		image.getRaster().setDataElements(0, 0, WIDTH, HEIGHT, pixels);
		return image;
	}

	/**
	 * 缩放logo图
	 * 
	 * @param srcImageFile
	 *            logo图片地址
	 * @param height
	 *            缩放后的高度
	 * @param width
	 *            缩放后的宽度
	 * @param hasFiller
	 *            是否有白边
	 * @return
	 * @throws IOException
	 */
	private static BufferedImage scale(String srcImageFile, int height, int width, boolean hasFiller)
			throws IOException {
		double ratio = 0.0; // 缩放比例
		File file = new File(srcImageFile);
		BufferedImage srcImage = ImageIO.read(file);
		Image destImage = srcImage.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
		// 计算比例
		if ((srcImage.getHeight() > height) || (srcImage.getWidth() > width)) {
			if (srcImage.getHeight() > srcImage.getWidth()) {
				ratio = (new Integer(height)).doubleValue() / srcImage.getHeight();
			} else {
				ratio = (new Integer(width)).doubleValue() / srcImage.getWidth();
			}
			AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
			destImage = op.filter(srcImage, null);
		}
		if (hasFiller) {
			// 补白
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphic = image.createGraphics();
			graphic.setColor(Color.white);
			graphic.fillRect(0, 0, width, height);
			if (width == destImage.getWidth(null))
				graphic.drawImage(destImage, 0, (height - destImage.getHeight(null)) / 2, destImage.getWidth(null),
						destImage.getHeight(null), Color.white, null);
			else
				graphic.drawImage(destImage, (width - destImage.getWidth(null)) / 2, 0, destImage.getWidth(null),
						destImage.getHeight(null), Color.white, null);
			graphic.dispose();
			destImage = image;
		}
		return (BufferedImage) destImage;
	}

}
