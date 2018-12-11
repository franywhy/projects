package com.hqjy.msg.utils;

import javax.tools.SimpleJavaFileObject;
import java.io.IOException;
import java.net.URI;

/**
 * Created by Administrator on 2018/3/23 0023.
 */
public class StringJavaObject extends SimpleJavaFileObject {
    // 源代码
    private String content = "";
    // 遵循Java 规范的类名及文件
    public StringJavaObject(String _javaFileName,String _content){
        super(_createStringJavaObjectUri(_javaFileName),Kind.SOURCE);
        content = _content;
    }
    // 产生一个URL 资源路径
    private static URI _createStringJavaObjectUri(String name){
        // 注意此处没有设置包名
        return URI.create("String:///" + name + Kind.SOURCE.extension);
    }
    // 文本文件代码
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors)
            throws IOException {
        return content;
    }
}
