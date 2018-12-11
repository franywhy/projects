package com.elise.datacenter.file;

import org.apache.commons.codec.binary.Base64;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Created by Glenn on 2017/4/26 0026.
 */

public class FileId {
	
	private Long sourceId;

	private Byte fileType;

	private String fileId;

	private String mySQLFileId;

	private Long fileStamp;

	private static final Byte maxByte = (byte)0xff;

	@SuppressWarnings("unused")
	private FileId() {
		
	}
	
	protected FileId(Long sourceId,Integer fileType) {
	    this.sourceId =sourceId;
	    this.fileType = (byte)(maxByte & fileType);
	}
	
	protected FileId(String fileId){
		this.fileId = fileId;
	}

	public String getMySQLFileId(){
		return mySQLFileId;
	}

	public Long getFileStamp(){
		return fileStamp;
	}
	
	public Integer getFileType(){
		return fileType & 0xff;
	}
	
	public Long getSourceId(){
		return sourceId;
	}
	
	public String getFileId(){
		return fileId;
	}
	
	protected void parse(){
		ByteBuffer buffer = ByteBuffer.wrap(Base64.decodeBase64(fileId));
		long leastUUID = buffer.getLong();
		this.fileType = buffer.get();
		long mostUUID = buffer.getLong();
		this.sourceId = buffer.getLong();
		StringBuilder  sb = new StringBuilder ();
		this.fileStamp = mostUUID;
		sb.append(Long.toHexString(mostUUID));
		sb.append(Long.toHexString(leastUUID));
		this.mySQLFileId = sb.toString();
	}
	
	protected void generate(){
		UUID uuid = UUID.randomUUID();
		ByteBuffer buffer = ByteBuffer.allocate(25);
		buffer.putLong(uuid.getLeastSignificantBits());
		buffer.put(fileType);
		buffer.putLong(uuid.getMostSignificantBits());
		buffer.putLong(sourceId);
		this.fileId = Base64.encodeBase64URLSafeString(buffer.array());
		StringBuilder sb = new StringBuilder ();
		this.fileStamp = uuid.getMostSignificantBits();
		sb.append(Long.toHexString(uuid.getMostSignificantBits()));
		sb.append(Long.toHexString(uuid.getLeastSignificantBits()));
		this.mySQLFileId = sb.toString();
	}
}
