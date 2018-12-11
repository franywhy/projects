package com.elise.datacenter.file;

import java.util.HashMap;

/**
 * Created by Glenn on 2017/4/26 0026.
 */

public enum FileType {
    JPG(1, ".jpg", "jpg", "image/jpeg", false),
    JPEG(2, ".jpeg", "jpeg", "image/jpeg", false),
    PNG(3, ".png", "png", "image/png", false),
    GIF(4, ".gif", "gif", "image/gif", false),
    ICON(5, ".ico", "ico", "image/x-icon", false),
    JSON(6, ".json", "json", "application/json;charset=UTF-8", false),
    TEXT(7, ".txt", "txt", "text/plain; charset=utf-8", true),
    DOC(8, ".doc", "doc", "application/msword", true),
    PDF(9, ".pdf", "pdf", "application/pdf", false),
    XML(10, ".xml", "xml", "text/xml", false),
    XLS(11, ".xls", "xls", "application/x-xls", true),
    DOC_XML(12, ".docx", "docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.template", true),
    PPT_XML(13, ".pptx", "pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation", true),
    XLS_XML(14, ".xlsx", "xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", true),
    PPT(15, ".ppt", "ppt", "application/vnd.ms-powerpoint", true),
    TAR_GZ(16, ".tar.gz", "tar.gz", "application/octet-stream", true),
    ZIP(17, ".zip", "zip", "application/octet-stream", true),
    RAR(18, ".rar", "rar", "application/octet-stream", true),
	MP3(19,".mp3","mp3","audio/mpeg",false),
    WAVE(20,".wave","wave","audio/mpeg",false),
    WMA(21,".wma","wma","audio/mpeg",false),
    WAV(22,".wav","wav","audio/x-wav",false),
    VQF(22,".vqf","vqf","audio/mpeg",false),
    MP4(23,".mp4","mp4","video/mp4",false),
    FLV(24,".flv","flv","application/octet-stream",true),
    RM(25,".rm","rm","application/octet-stream",true),
    RMVB(26,".rmvb","rmvb","application/octet-stream",true),
    WMV(27,".wmv","wmv","application/octet-stream",true),
    AVI(28,".avi","avi","application/octet-stream",true),
    MPG(29,".mpg","mpg","application/octet-stream",true),
    FILE(128, "", "", "application/octet-stream", true),
	AMR(129, ".amr", "amr", "multipart/form-data", true);

    private final static HashMap<String, FileType> MAP_4_FILE_SUFFIX = new HashMap<String, FileType>();
    private final static HashMap<Integer, FileType> MAP_4_VALUE = new HashMap<Integer, FileType>();

    static {
        for (FileType type : FileType.values()) {
            MAP_4_FILE_SUFFIX.put(type.getFileSuffix(), type);
            MAP_4_VALUE.put(type.getValue(), type);
        }
    }

    private Integer value;
    private String fileSuffix;
    private String pureSuffix;
    private String type;
    private Boolean isAttachment;

    FileType(Integer value, String fileSuffix, String pureSuffix, String type, Boolean isAttachment) {
        this.value = value;
        this.fileSuffix = fileSuffix;
        this.pureSuffix = pureSuffix;
        this.type = type;
        this.isAttachment = isAttachment;

    }

    public static FileType convertSuffix2Type(String fileSuffix) {
        String[] nameAndSuffix = null;
        FileType type = MAP_4_FILE_SUFFIX.get(fileSuffix);
        int cursor = 1;
        StringBuilder strSuffix = new StringBuilder();
        while (type == null) {
            nameAndSuffix = nameAndSuffix == null ? fileSuffix.split("\\.") : nameAndSuffix;
            strSuffix = new StringBuilder()
                    .append(".")
                    .append(nameAndSuffix[nameAndSuffix.length-cursor++])
                    .append(strSuffix);
            if (null != (type = MAP_4_FILE_SUFFIX.get(strSuffix.toString()))
                    || (cursor >= nameAndSuffix.length && null != (type = FileType.FILE)))
                break;
        }
        return type;
    }

    public Boolean isBinaryFile() {
        return this.getValue() >= FileType.FILE.getValue();
    }

    public static FileType convertValue2Type(Integer value) {
        return MAP_4_VALUE.get(value);
    }

    public String getPureSuffix() {
        return pureSuffix;
    }

    public String getContentType() {
        return type;
    }

    public Integer getValue() {
        return value == null ? FileType.FILE.getValue() : value;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public Boolean isAttachment() {
        return isAttachment;
    }
}
