package io.renren.controller;

/**
 * 使用jdk自带的HttpURLConnection向URL发送POST请求并输出响应结果
 * 参数使用流传递，并且硬编码为字符串"name=XXX"的格式
 */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import io.renren.utils.R;



@Controller
@RequestMapping("upload")
public class UploadController {


	private static Logger logger = LoggerFactory.getLogger(UploadController.class);
	/*
//	@ResponseBody
	@RequestMapping("/upload")
	public R upload(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		request.setCharacterEncoding("utf-8");  
		response.setCharacterEncoding("utf-8");  
		//1、创建一个DiskFileItemFactory工厂  
		DiskFileItemFactory factory = new DiskFileItemFactory(); 
		//2、创建一个文件上传解析器  
		ServletFileUpload upload = new ServletFileUpload(factory);
		//解决上传文件名的中文乱码  
        upload.setHeaderEncoding("UTF-8");   
//        factory.setSizeThreshold(1024 * 500);//设置内存的临界值为500K  
//        File linshi = new File("E:\\linshi");//当超过500K的时候，存到一个临时文件夹中  
//        factory.setRepository(linshi);  
        upload.setSizeMax(1024 * 1024 * 5);//设置上传的文件总的大小不能超过5M  
        // 1. 得到 FileItem 的集合 items  
				
//		CommonsMultipartResolver parse = new CommonsMultipartResolver();
////		parse.setMaxUploadSize(10 * 1024 * 1024);  // 10MB
//        MultipartHttpServletRequest req = null;
        try{
        	List<FileItem>  FileItem items = upload.parseRequest(request);  
        	 String boundary = "admin-html-pic"; //Could be any string  
             String Enter = "\r\n";  
             
        	// 2. 遍历 items:  
            for (FileItem item : items) {  
                // 若是一个一般的表单域, 打印信息  
                if (item.isFormField()) {  
                    String name = item.getFieldName();  
                    String value = item.getString("utf-8");  
                    System.out.println(name + ": " + value);  
                }  
                // 若是文件域则把文件保存到 e:\\files 目录下.  
                else {  
                    String fileName = item.getName();  
                    long sizeInBytes = item.getSize();  
                    System.out.println(fileName);  
                    System.out.println(sizeInBytes);  
  
                    InputStream fis = item.getInputStream();  
//                    byte[] buffer = new byte[1024];  
//                    int len = 0;  
//  
//                    fileName = "e:\\files\\" + fileName;//文件最终上传的位置  
//                    System.out.println(fileName);  
//                    OutputStream out = new FileOutputStream(fileName);  
//  
//                    while ((len = in.read(buffer)) != -1) {  
//                        out.write(buffer, 0, len);  
//                    }  
  
//                    out.close();  
//                    in.close();  
                    
                    
                    
                    URL url = new URL("http://177.77.83.132:8081POST/file/singleDirectUpload");  
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
                    conn.setDoOutput(true);  
                    conn.setDoInput(true);  
                    conn.setRequestMethod("POST");  
                    conn.setUseCaches(false);  
                    conn.setInstanceFollowRedirects(true);  
                    conn.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);   
                      
                    conn.connect();  
                      
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());  
                    
                  //part 1  
                    String part1 =  "--" + boundary + Enter  
                            + "Content-Type: application/octet-stream" + Enter  
                            + "Content-Disposition: form-data; filename=\""+fileName+"\"; name=\"file\"" + Enter + Enter;  
                  //part 2  
                    String part2 = Enter  
                            + "--" + boundary + Enter  
                            + "Content-Type: text/plain" + Enter  
                            + "Content-Disposition: form-data; name=\"dataFormat\"" + Enter + Enter  
                            + "hk" + Enter  
                            + "--" + boundary + "--";  
                      
                    byte[] xmlBytes = new byte[fis.available()];  
                    fis.read(xmlBytes);  
                      
                    dos.writeBytes(part1);  
                    dos.write(xmlBytes);  
                    dos.writeBytes(part2);  
                      
                    dos.flush();  
                    dos.close();  
                    fis.close();  
                      
//                    System.out.println("status code: "+conn.getResponseCode());  
                      
                  //获得响应状态
        		    int resultCode=conn.getResponseCode();
        		    if(HttpURLConnection.HTTP_OK==resultCode){
        		      StringBuffer sb=new StringBuffer();
        		      String readLine=new String();
        		      BufferedReader responseReader=new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
        		      while((readLine=responseReader.readLine())!=null){
        		        sb.append(readLine).append("\n");
        		      }
        		      responseReader.close();
        		      System.out.println(sb.toString());
        		    } 
                    
                    conn.disconnect();  
                }  
            }  

            return R.error("2222");
        } catch (Exception e) {
			e.printStackTrace();
		}finally {
//			if(req != null){
//				parse.cleanupMultipart(req);
//			}
        }
		return R.error("333");
    }*/
	@ResponseBody
	@RequestMapping("/upload2")
	public R upload2(HttpServletRequest request,HttpServletResponse response){
		 // 创建一个通用的多部分解析器
	    CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
	    // 设置编码
	    commonsMultipartResolver.setDefaultEncoding("utf-8");
	    // 判断 request 是否有文件上传,即多部分请求...
	    if (commonsMultipartResolver.isMultipart(request))
	    {
	        // 转换成多部分request
	        MultipartHttpServletRequest multipartRequest = commonsMultipartResolver.resolveMultipart(request);
	        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
	        logger.info("fileMap is ",fileMap);
	        // file 是指 文件上传标签的 name=值
	        // 根据 name 获取上传的文件...
	        MultipartFile file = multipartRequest.getFile("imgFile");
	 
	        // 上传后记录的文件...  
	        File imageFile = new File("fileName");
	        // 上传...
//	        file.transferTo(imageFile);
	 
	        // to do
	    }
		return R.ok();
	}
	@RequestMapping("/upload")
	public R upload(HttpServletRequest request,HttpServletResponse response){
		CommonsMultipartResolver parse = new CommonsMultipartResolver();
//		parse.setMaxUploadSize(10 * 1024 * 1024);  // 10MB
		MultipartHttpServletRequest req = null;
		
		try{
			req = parse.resolveMultipart(request);
			MultipartFile file = null;
			for(Map.Entry<String, MultipartFile> entry : req.getFileMap().entrySet()){
				file = entry.getValue();
				String file_name = file.getOriginalFilename();
				System.out.println("uploadFile:" + file_name);
				break;
			}
			
			
			String boundary = "admin-html-pic"; //Could be any string  
			String Enter = "\r\n";  
			
			InputStream fis = file.getInputStream();  
//            FileInputStream fis = file.getInputStream();  
			
			URL url = new URL("http://177.77.83.132:8081POST/file/singleDirectUpload");  
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
			conn.setDoOutput(true);  
			conn.setDoInput(true);  
			conn.setRequestMethod("POST");  
			conn.setUseCaches(false);  
			conn.setInstanceFollowRedirects(true);  
			conn.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);   
			
			conn.connect();  
			
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());  
			
			//part 1  
			String part1 =  "--" + boundary + Enter  
					+ "Content-Type: application/octet-stream" + Enter  
					+ "Content-Disposition: form-data; filename=\""+file.getName()+"\"; name=\"file\"" + Enter + Enter;  
			//part 2  
			String part2 = Enter  
					+ "--" + boundary + Enter  
					+ "Content-Type: text/plain" + Enter  
					+ "Content-Disposition: form-data; name=\"dataFormat\"" + Enter + Enter  
					+ "hk" + Enter  
					+ "--" + boundary + "--";  
			
			byte[] xmlBytes = new byte[fis.available()];  
			fis.read(xmlBytes);  
			
			dos.writeBytes(part1);  
			dos.write(xmlBytes);  
			dos.writeBytes(part2);  
			
			dos.flush();  
			dos.close();  
			fis.close();  
			
//            System.out.println("status code: "+conn.getResponseCode());  
			
			//获得响应状态
			int resultCode=conn.getResponseCode();
			if(HttpURLConnection.HTTP_OK==resultCode){
				StringBuffer sb=new StringBuffer();
				String readLine=new String();
				BufferedReader responseReader=new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
				while((readLine=responseReader.readLine())!=null){
					sb.append(readLine).append("\n");
				}
				responseReader.close();
				System.out.println(sb.toString());
			} 
			
			conn.disconnect();  
			
			
			return R.error("2222");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(req != null){
				parse.cleanupMultipart(req);
			}
		}
		return R.error("333");
	}
	
}
