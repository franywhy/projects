package com.hq.learningcenter.school.freemarker.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.stereotype.Component;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
@Component("EncodeURLModel")
public class EncodeURL implements TemplateMethodModelEx {
  
    /** 
     * 执行方法 
     * @param argList 方法参数列表 
     * @return Object 方法返回值 
     * @throws TemplateModelException 
     */  
    public Object exec(List argList) throws TemplateModelException {  
        if(argList.size()!=1)   //限定方法中必须且只能传递一个参数  
        {  
            throw new TemplateModelException("Wrong arguments!");  
        }  
        //返回response.encodeURL执行结果  
        String result = argList.get(0).toString();
		try {
			result = URLEncoder.encode(result, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}
        return result;  
    } 
}
