package com.hq.learningcenter.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取文件服务上随机的5张照片用于课次展示
 * Created by DL on 2018/1/25.
 */
public class RandomUtils {
    private static Logger logger = LoggerFactory.getLogger(RandomUtils.class);
    //文件服务器前缀/后缀
    private static final String FILE_PREFIX = "http://download.hqjy.com/staic/lessontimes/";
    private static final String FILE_SUFFIX = "-min.jpg";


    public static String getFileUrl(){
        int number = (int) ((Math.random() * 5) + 1);
        String fileUrl = FILE_PREFIX + number + FILE_SUFFIX;
        logger.info("图片地址:  "+ fileUrl);
        return  fileUrl;
    }

    public static void main(String[] args) {
        System.out.println(RandomUtils.getFileUrl());
    }
}
