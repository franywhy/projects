package com.elise.datacenter.file;

/**
 * Created by Glenn on 2017/4/26 0026.
 */

public class FileIdFactory {
    
	 /**
     */
	public static FileId Generate(Long sourceId, Integer fileType) {
		FileId fileId = new FileId(sourceId, fileType);
		fileId.generate();
		return fileId;
	}
    /**
     */
	public static FileId Generate(String fileId) {
		FileId fileIdObject = new FileId(fileId);
		fileIdObject.parse();
		return fileIdObject;
	}
}
