package com.hq.learningapi.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取文件服务上随机的5张照片用于课次展示
 * Created by DL on 2018/1/25.
 */
public class RandomUtils {
    private static Logger logger = LoggerFactory.getLogger(RandomUtils.class);
    //文件服务器前缀/后缀
    private static final String FILE_PREFIX = "http://file.hqjy.com/file/singleDirectDownload/";
    private static final String FILE_SUFFIX = ".jpg";


    public static String getFileUrl(){
        int number = (int) ((Math.random() * 5) + 1);
        String numberName=fileChoice(number);
        String fileUrl = FILE_PREFIX + numberName + FILE_SUFFIX;
        logger.info("图片地址:  "+ fileUrl);
        return  fileUrl;
    }

    public static String fileChoice(int number){
        String numberName;
        switch (number){
            case 1:
                numberName="sx2ZrxxeqXsBFqWArLTrQHkAAAAAAAAAAQ";
                break;
            case 2:
                numberName="uH_wIZcgi0IBcw_-1kqITLUAAAAAAAAAAQ";
                break;
            case 3:
                numberName="tazEJTLtMBcBHlZvyUCfRYoAAAAAAAAAAQ";
                break;
            case 4:
                numberName="gQJCGwTg964BDQrOY9XLR8QAAAAAAAAAAQ";
                break;
            case 5:
                numberName="hvUn_JwtRGoBUEe2SgJeQfgAAAAAAAAAAQ";
                break;
            default :
                numberName="sx2ZrxxeqXsBFqWArLTrQHkAAAAAAAAAAQ";
                break;
        }
        return numberName;
    }


    public static void main(String[] args) {
        System.out.println(RandomUtils.getFileUrl());
    }
}
