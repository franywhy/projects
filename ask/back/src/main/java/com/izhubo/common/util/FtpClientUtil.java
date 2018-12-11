package com.izhubo.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * shiHongjie
 * 2015-01-11
 * FTP上传 下载 删除
 */
public class FtpClientUtil {
	private FTPClient ftpClient = null;
	private static final String _SERVER = "rsync.file.cachecn.net";
	private static final int _PORT = 21;
	private static final String _USER_NAME = "xuelxuew.com";
	private static final String _USER_PASSWORD = "mfu3*kTB3";
	private static final String _FILE_HEAD = "dtvpull.xuelxuew.com/";

	public static void main(String[] args) {
		// 调用FTP传输g
		FtpClientUtil f = new FtpClientUtil();
		try {
			if (f.open()) {

				// f.getFtpClient().sendServer("quote PASV");

				// f.getFtpClient().sendServer("quote PORT");

				// f.mkDir("put2/5/6\\8\\9");
				// f.mkDir("test6\\7/8/9");
				// f.mkDir("test6/7/8/9");
				// f.mkDir("test7");
				f.put_demo("C:/Users/Administrator/Desktop/222/user_star_song_list.java", "cdn.java", "dtvpull.xuelxuew.com/file1/1");// 远程路径为相对路径
//				f.put("d:/1.txt", "中国.txt", "put3/测试/6\\8\\9");// 远程路径为相对路径
				//f.get("/opt/IBM/WebSphere/AppServer/profiles/AppSrv01/qunarlog.txt","E:/111111111.txt");// 远程路径为相对路径
				f.close();
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public boolean put_demo(String localDirectoryAndFileName, String ftpFileName,String ftpDirectory){
		File srcFile = new File(localDirectoryAndFileName);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(srcFile);
			return put(fis, ftpFileName, ftpDirectory);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public FtpClientUtil() {
		
	}

	/**
	 * 链接到服务器
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean open() {
		if (ftpClient != null && ftpClient.isConnected()) {
			return true;
		}
		try {
			ftpClient = new FTPClient();
			// 连接
			ftpClient.connect(this._SERVER, this._PORT);
			ftpClient.login(this._USER_NAME, this._USER_PASSWORD);
			// 检测连接是否成功
			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				this.close();
				System.err.println("FTP server refused connection.");
				System.exit(1);
			}
			System.out.println("open FTP success:" + this._SERVER + ";port:"
					+ this._PORT + ";name:" + this._USER_NAME + ";pwd:"
					+ this._USER_PASSWORD);
			ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE); // 设置上传模式.binally
			// or ascii
			return true;
		} catch (Exception ex) {
			// 关闭
			this.close();
			ex.printStackTrace();
			return false;
		}

	}

	private boolean cd(String dir) throws IOException {
		if (ftpClient.changeWorkingDirectory(dir)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取目录下所有的文件名称
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */

	private FTPFile[] getFileList(String filePath) throws IOException {
		FTPFile[] list = ftpClient.listFiles();
		return list;

	}

	/**
	 * 循环将设置工作目录
	 */
	public boolean changeDir(String ftpPath) {
		if (!ftpClient.isConnected()) {
			return false;
		}
		try {

			// 将路径中的斜杠统一
			char[] chars = ftpPath.toCharArray();
			StringBuffer sbStr = new StringBuffer(256);
			for (int i = 0; i < chars.length; i++) {

				if ('\\' == chars[i]) {
					sbStr.append('/');
				} else {
					sbStr.append(chars[i]);
				}
			}
			ftpPath = sbStr.toString();
			// System.out.println("ftpPath"+ftpPath);

			if (ftpPath.indexOf('/') == -1) {
				// 只有一层目录
				// System.out.println("change"+ftpPath);
				ftpClient.changeWorkingDirectory(new String(ftpPath.getBytes(),"iso-8859-1"));
			} else {
				// 多层目录循环创建
				String[] paths = ftpPath.split("/");
				// String pathTemp = "";
				for (int i = 0; i < paths.length; i++) {
					// System.out.println("change "+paths[i]);
					ftpClient.changeWorkingDirectory(new String(paths[i].getBytes(), "iso-8859-1"));
				}
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 循环创建目录，并且创建完目录后，设置工作目录为当前创建的目录下
	 */
	public boolean mkDir(String ftpPath) {
		if (!ftpClient.isConnected()) {
			return false;
		}
		try {

			// 将路径中的斜杠统一
			char[] chars = ftpPath.toCharArray();
			StringBuffer sbStr = new StringBuffer(256);
			for (int i = 0; i < chars.length; i++) {

				if ('\\' == chars[i]) {
					sbStr.append('/');
				} else {
					sbStr.append(chars[i]);
				}
			}
			ftpPath = sbStr.toString();
			System.out.println("ftpPath" + ftpPath);

			if (ftpPath.indexOf('/') == -1) {
				// 只有一层目录

				ftpClient.makeDirectory(new String(ftpPath.getBytes(),"iso-8859-1"));
				ftpClient.changeWorkingDirectory(new String(ftpPath.getBytes(),"iso-8859-1"));
			} else {
				// 多层目录循环创建
				String[] paths = ftpPath.split("/");
				// String pathTemp = "";
				for (int i = 0; i < paths.length; i++) {

					ftpClient.makeDirectory(new String(paths[i].getBytes(),"iso-8859-1"));
					ftpClient.changeWorkingDirectory(new String(paths[i].getBytes(), "iso-8859-1"));
				}
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 上传文件到FTP服务器
	 * 
	 * @param localPathAndFileName
	 *            本地文件目录和文件名
	 * @param ftpFileName
	 *            上传后的文件名
	 * @param ftpDirectory
	 *            FTP目录如:/path1/pathb2/,如果目录不存在回自动创建目录
	 * @throws Exception
	 */
	public boolean put(String localDirectoryAndFileName, String ftpFileName,
			String ftpDirectory) {
		ftpDirectory = _FILE_HEAD + ftpDirectory;
		if (!ftpClient.isConnected()) {
			return false;
		}
		boolean flag = false;
		if (ftpClient != null) {
			File srcFile = new File(localDirectoryAndFileName);
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(srcFile);

				// 创建目录

				this.mkDir(ftpDirectory);

				ftpClient.setBufferSize(1024);
				ftpClient.setControlEncoding("UTF-8");

				// 设置文件类型（二进制）
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				// 上传
				flag = ftpClient.storeFile(new String(ftpFileName.getBytes(),"iso-8859-1"), fis);
			} catch (Exception e) {
				this.close();
				e.printStackTrace();
				return false;
			} finally {
				IOUtils.closeQuietly(fis);
			}
		}

		System.out.println("success put file " + localDirectoryAndFileName
				+ " to " + ftpDirectory + ftpFileName);
		return flag;
	}
	
	
	/**
	 * 上传文件到FTP服务器
	 * 
	 * @param fis
	 * 			  文件流
	 * @param ftpFileName
	 *            上传后的文件名
	 * @param ftpDirectory
	 *            FTP目录如:path1/pathb2/,如果目录不存在回自动创建目录
	 * @throws Exception
	 */
	public boolean put(InputStream fis, String ftpFileName,String ftpDirectory) {
		if (!ftpClient.isConnected()) {
			return false;
		}
		boolean flag = false;
		if (ftpClient != null) {
			try {
				// 创建目录
				this.mkDir(_FILE_HEAD + ftpDirectory);
				
				ftpClient.setBufferSize(1024);
				ftpClient.setControlEncoding("UTF-8");
				
				// 设置文件类型（二进制）
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				// 上传
				flag = ftpClient.storeFile(new String(ftpFileName.getBytes(),"iso-8859-1"), fis);
			} catch (Exception e) {
				this.close();
				e.printStackTrace();
				return false;
			} finally {
				IOUtils.closeQuietly(fis);
			}
		}
		
		return flag;
	}

	/**
	 * 从FTP服务器上下载文件并返回下载文件长度
	 * 
	 * @param ftpDirectoryAndFileName
	 * @param localDirectoryAndFileName
	 * @return
	 * @throws Exception
	 */
	public long get(String ftpDirectoryAndFileName,
			String localDirectoryAndFileName) {

		long result = 0;
		if (!ftpClient.isConnected()) {
			return 0;
		}
		ftpClient.enterLocalPassiveMode(); // Use passive mode as default
		// because most of us are behind
		// firewalls these days.

		try {
			// 将路径中的斜杠统一
			char[] chars = ftpDirectoryAndFileName.toCharArray();
			StringBuffer sbStr = new StringBuffer(256);
			for (int i = 0; i < chars.length; i++) {

				if ('\\' == chars[i]) {
					sbStr.append('/');
				} else {
					sbStr.append(chars[i]);
				}
			}
			ftpDirectoryAndFileName = sbStr.toString();
			String filePath = ftpDirectoryAndFileName.substring(0,
					ftpDirectoryAndFileName.lastIndexOf("/"));
			String fileName = ftpDirectoryAndFileName
					.substring(ftpDirectoryAndFileName.lastIndexOf("/") + 1);
			// System.out.println("filePath | "+filePath);
			// System.out.println("fileName | "+fileName);
			this.changeDir(filePath);
			ftpClient.retrieveFile(
					new String(fileName.getBytes(), "iso-8859-1"),
					new FileOutputStream(localDirectoryAndFileName)); // download
			// file
			System.out.print(ftpClient.getReplyString()); // check result

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Success get file" + ftpDirectoryAndFileName
				+ " from " + localDirectoryAndFileName);
		return result;
	}

//	/**
//	 * 返回FTP目录下的文件列表
//	 * 
//	 * @param ftpDirectory
//	 * @return
//	 */
//	public List getFileNameList(String ftpDirectory) {
//		List list = new ArrayList();
//		 if (!open())
//		 return list;
//		 try {
//		 DataInputStream dis = new DataInputStream(ftpClient.nameList(ftpDirectory));
//		 String filename = "";
//		 while ((filename = dis.readLine()) != null) {
//		 list.add(filename);
//		 }
//		 } catch (Exception e) {
//		 e.printStackTrace();
//		 }
//		return list;
//	}

	/**
	 * 删除FTP上的文件
	 * 
	 * @param ftpDirAndFileName
	 */
	public boolean deleteFile(String ftpDirAndFileName) {
		if (!ftpClient.isConnected()) {
			return false;
		}
		// Todo
		return true;
	}

	/**
	 * 删除FTP目录
	 * 
	 * @param ftpDirectory
	 */
	public boolean deleteDirectory(String ftpDirectory) {
		if (!ftpClient.isConnected()) {
			return false;
		}
		// ToDo
		return true;
	}

	/**
	 * 关闭链接
	 */
	public void close() {
		try {
			if (ftpClient != null && ftpClient.isConnected())
				ftpClient.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Close Server Success :" + this._SERVER + ";port:"+ this._PORT);
	}

	public FTPClient getFtpClient() {
		return ftpClient;
	}

	public void setFtpClient(FTPClient ftpClient) {

		this.ftpClient = ftpClient;
	}
	
	
	// 对扩展名进行小写转换
	public static String getEXT(String _file_name) {
		if(StringUtils.isNotBlank(_file_name)){
			return _file_name.substring(_file_name.lastIndexOf(".") + 1, _file_name.length()).toLowerCase();
		}
		return null;
	}
}

// //设置以什么文件的格式输出
//
// try
// {
// response.setContentType("application/octet-stream;charset=GB2312");
// String downloadname=downName;
// response.setHeader("Content-Disposition","attachment; filename="+downloadname);
//
// if (filename!=null)
// {
// File file1=new File(folerDown);
// FileInputStream is=new FileInputStream(file1);
// byte []bbb=new byte[(int)file1.length()];
//
// System.out.println("------bbb.length:"+bbb.length);
// is.read(bbb);
// is.close();
// OutputStream out = response.getOutputStream();
// out.write(bbb);
// out.close();
// response.setStatus(response.SC_OK);
// file1.delete();
// }
// else
// {
// System.out.println("----createExcelFile error!");
// }
// }
// catch (Exception e)
// {
// System.out.println("----------ex in servlet:"+e);
// e.printStackTrace();
// }