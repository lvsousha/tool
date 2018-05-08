package com.lvdousha.tool;

import java.io.File;
import java.net.ConnectException;
import java.util.Calendar;

import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.DocumentFormatRegistry;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

public class OpenOfficeUtil {

	private OfficeManager officeManager;
	public static String OFFICE_HOME = "C:\\Program Files (x86)\\OpenOffice 4";// 本机OpenOffice安装目录
	private int port = 8100;

	public static void main(String[] args) {
		File sourceFile = new File("/office2pdf/待处理文件/java基础深度解析(全).DOC");
        File targetFile = new File("/target.pdf");
		OpenOfficeUtil oou = new OpenOfficeUtil();
//		oou.startService(0);
		oou.convert(sourceFile, targetFile);
		oou.stopService();
		// TODO Auto-generated method stub

	}

	public void startService(int i) {
		DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
		try {
			// 先关闭再启动
			// System.out.println("准备启动服务....");
			// 设置OpenOffice.org安装目录
			configuration.setOfficeHome(OFFICE_HOME);
			// 设置转换端口
			configuration.setPortNumbers(port);
			// 设置任务执行超时30分钟
			configuration.setTaskExecutionTimeout(1000 * 60 * 5L);
			// 设置任务队列超时24小时
			configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);
			officeManager = configuration.buildOfficeManager();
			officeManager.start(); // 启动服务
			System.out.println("office转换服务启动成功!");
		} catch (Exception ce) {
			System.out.println("office转换服务启动失败!详细信息:" + ce);
		}
	}

	public void stopService() {
		// System.out.println("关闭office转换服务....");
		if (officeManager != null) {
			officeManager.stop();
		}
		System.out.println("关闭office转换成功!");
	}
	
	public void convert(File sourceFile, File targetFile) {  
  	  
        // 1: 打开连接  
        OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);  
        try {
			connection.connect();
			DocumentConverter converter = new OpenOfficeDocumentConverter(connection);  
			// 2:获取Format  
			DocumentFormatRegistry factory = new CustomerDocumentFormatRegistry();  
			
			DocumentFormat inputDocumentFormat = factory  
					.getFormatByFileExtension("docx");  
			DocumentFormat outputDocumentFormat = factory  
					.getFormatByFileExtension("pdf");  
			// 3:执行转换  
			System.out.println(Calendar.getInstance().getTimeInMillis()/1000);
			converter.convert(sourceFile, inputDocumentFormat, targetFile, outputDocumentFormat);  
			System.out.println(Calendar.getInstance().getTimeInMillis()/1000);
		} catch (ConnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(connection.isConnected()){
				connection.disconnect();
			}
		}
  
}

}
