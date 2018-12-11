package com.elise.datacenter.file;

/**
 * Created by Glenn on 2017/4/24 0024.
 */
public class FileNameUtil {

    public static String getPosixName(String fileName) {
        String[] nameAndSuffix = fileName.split("\\.");
        if (nameAndSuffix.length == 1) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(".");
            for (int i = 1; i < nameAndSuffix.length; i++) {
                if (i == nameAndSuffix.length-1) {
                    sb.append(nameAndSuffix[i]);
                } else {
                    sb.append(nameAndSuffix[i]);
                    sb.append(".");
                }
            }
            return sb.toString();
        }
    }

    public static String getPrefixName(String fileName) {
        String[] nameAndSuffix = fileName.split("\\.");
        if (nameAndSuffix.length == 1) {
            return fileName;
        } else {
            return nameAndSuffix[0];
        }
    }
}
