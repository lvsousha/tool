package com.lvdousha.tool;

import java.util.Iterator;

import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentFormat;

public class CustomerDocumentFormatRegistry extends DefaultDocumentFormatRegistry {

	public CustomerDocumentFormatRegistry(){
		super();
	}
	
	/**
	 * @param extension the file extension
	 * @return the DocumentFormat for this extension, or null if the extension is not mapped
	 */
	public DocumentFormat getFormatByFileExtension(String extension) {
        if (extension == null) {
            return null;
        }
        
      //将文件名后缀统一转化  
        if (extension.indexOf("doc") >= 0) {  
            extension = "doc";  
        }  
        if (extension.indexOf("ppt") >= 0) {  
            extension = "ppt";  
        }  
        if (extension.indexOf("xls") >= 0) {  
            extension = "xls";  
        } 
        
        String lowerExtension = extension.toLowerCase();
		for (Iterator<?> it = getDocumentFormats().iterator(); it.hasNext();) {
			DocumentFormat format = (DocumentFormat) it.next();		
			if (format.getFileExtension().equals(lowerExtension)) {
				return format;
			}
		}
		return null;
	}

	public DocumentFormat getFormatByMimeType(String mimeType) {
		for (Iterator<?> it = getDocumentFormats().iterator(); it.hasNext();) {
			DocumentFormat format = (DocumentFormat) it.next();		
			if (format.getMimeType().equals(mimeType)) {
				return format;
			}
		}
		return null;
	}

}
