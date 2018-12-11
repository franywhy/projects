package com.elise.datacenter.controller;


import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.bluewhale.common.enumeration.TransactionStatus;
import com.bluewhale.common.prototype.WrappedResponse;
import com.bluewhale.common.util.EncryptionUtils;
import com.elise.datacenter.config.ALiOSSConfig;
import com.elise.datacenter.config.LocalConfigEntity;
import com.elise.datacenter.dao.FileDao;
import com.elise.datacenter.entity.FileEntity;
import com.elise.datacenter.file.FileId;
import com.elise.datacenter.file.FileIdFactory;
import com.elise.datacenter.file.FileNameUtil;
import com.elise.datacenter.file.FileType;
import com.elise.datacenter.service.FileCacheService;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Glenn on 2017/4/15 0015.
 */

@Controller
@RequestMapping("/file")
@CrossOrigin
public class SingleUploadController {

    private static final Logger TRACER = LoggerFactory.getLogger(SingleUploadController.class);

    @Autowired
    private FileDao fileDao;

    @Autowired
    private LocalConfigEntity config;

    @Autowired
    private ALiOSSConfig ossConfig;

    @Autowired
    private TrackerClient client;

    @Autowired
    private FileCacheService fileCacheService;

    @RequestMapping(method = RequestMethod.POST, value = "/singleDirectUpload")
    @ResponseBody
    public ResponseEntity<Object> upload(
            @RequestParam(value = "file") MultipartFile file,
            @RequestParam(value = "sourceId") Long sourceId,
            @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
            @RequestParam(value = "md5", required = false) String md5,
            HttpServletResponse httpServletResponse) {
        try {
            byte[] fileContent = file.getBytes();
            Boolean isChecked;
            if (md5 != null) {
                isChecked = EncryptionUtils.check(md5, fileContent);
            } else {
                isChecked = true;
            }
            if (isChecked) {
                String fileSuffix = FileNameUtil.getPosixName(file.getOriginalFilename());
                FileType type = FileType.convertSuffix2Type(fileSuffix);
                /*
                 * 实现fastDFS文件上传
                 */
                StorageClient storageClient = new StorageClient(client.getConnection(), null);
                String[] ret = storageClient.upload_file(fileContent, type.getPureSuffix(), null);
                FileId fileId = FileIdFactory.Generate(sourceId, type.getValue());
                FileEntity entity = new FileEntity();
                entity.setFileId(fileId.getMySQLFileId());
                entity.setFileLength((int) file.getSize());
                entity.setFileUri(ret[1]);
                entity.setGroupName(ret[0]);
                entity.setIsPermanent(1);
                entity.setSource(sourceId);
                entity.setFileSuffix(type.getValue());
                Date date = new Date();
                date.setTime(System.currentTimeMillis());
                entity.setCreateTime(date);
                int result = fileDao.create(entity);
                fileCacheService.putFile(fileId.getMySQLFileId(), ByteBuffer.wrap(fileContent));
                TRACER.info(String.format("\r\nFile object :\r\n%s\r\nhas been upload successful\r\n", entity.toString()));
                // 判断结果
                if (result == 1) {
                    StringBuilder data = new StringBuilder();
                    data.append(config.getServiceUrl());
                    data.append(fileId.getFileId());
                    data.append(type.getFileSuffix());
                    TransactionStatus code = TransactionStatus.OK;
                    WrappedResponse response = WrappedResponse.generate(code.value(), code.getReasonPhrase(), data.toString());
                    if (redirectUrl == null || "".equals(redirectUrl.trim())) {
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.valueOf(FileType.JSON.getContentType()));
                        return new ResponseEntity<Object>(response, headers, HttpStatus.OK);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append(redirectUrl);
                        sb.append("&data=");
                        sb.append(data.toString());
                        sb.append("&code=");
                        sb.append(code.value());
                        httpServletResponse.sendRedirect(sb.toString());
                        return null;
                    }
                } else {
                    return TraceErrorAndGetJson(TransactionStatus.BAD_REQUEST, "Insert file entity failed", null);
                }
            } else {
                return TraceErrorAndGetJson(TransactionStatus.DAMAGE_FILE, "File has been damaged", null);
            }
            /* 从数据流中读取文件失败 */
        } catch (IOException e) {
            return TraceErrorAndGetJson(TransactionStatus.UPLOAD_FAILED, "Get file entity", e);
            /* MD5验签失败 */
        } catch (Throwable t) {
            return TraceErrorAndGetJson(TransactionStatus.BAD_REQUEST, "Unknown issue", t);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/alioss/singleDirectUpload")
    @ResponseBody
    public ResponseEntity<Object> aliOSSUpload(
            @RequestParam(value = "file") MultipartFile file,
            @RequestParam(value = "sourceId") Long sourceId,
            @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
            @RequestParam(value = "md5", required = false) String md5,
            HttpServletResponse httpServletResponse) {
        OSSClient ossClient = null;

        try {
            byte[] fileContent = file.getBytes();
            Boolean isChecked;
            if (md5 != null) {
                isChecked = EncryptionUtils.check(md5, fileContent);
            } else {
                isChecked = true;
            }
            if (isChecked) {
                // 获取 文件名 和 文件类型实体
                String fileSuffix = FileNameUtil.getPosixName(file.getOriginalFilename());
                FileType type = FileType.convertSuffix2Type(fileSuffix);
                /*
                 * 实现ali oss文件上传
                 */
                ossClient = new OSSClient(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());

                InputStream input = file.getInputStream();
                ObjectMetadata meta = new ObjectMetadata();                // 创建上传Object的Metadata
                meta.setContentType(type.getContentType());        // 设置上传内容类型
                meta.setCacheControl("no-cache");                    // 被下载时网页的缓存行为

                // 以yyyy-M-d的格式获得当前日期，用于上传文件夹区分日期
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");
                FileId fileId = FileIdFactory.Generate(sourceId, type.getValue());
                String filePath = ossConfig.getPicLocation() + simpleDateFormat.format(new Date()) + "/" + UUID.randomUUID() + fileId.getFileId() + type.getFileSuffix();
                PutObjectRequest putObjectRequest = new PutObjectRequest(ossConfig.getBucketName(), filePath, input, meta);            //创建上传请求
                ossClient.putObject(putObjectRequest);

                // 私有加密url
                //Date expiration = new Date(new Date().getTime() + 3600 * 1000);
                //String url = ossClient.putObject(file, fileType, config.getPicLocation() + fileName + "." + fileType);

                // 公有url
                String url = ossConfig.getSsoHost() + "/" + filePath;

                FileEntity entity = new FileEntity();
                entity.setFileId(fileId.getMySQLFileId());
                entity.setGroupName("group1");
                entity.setFileUri("");
                entity.setFileLength((int) file.getSize());
                entity.setFileUrl(url);
                entity.setIsPermanent(1);
                entity.setSource(sourceId);
                entity.setFileSuffix(type.getValue());
                Date date = new Date();
                date.setTime(System.currentTimeMillis());
                entity.setCreateTime(date);
                int result = fileDao.create(entity);
                fileCacheService.putFile(fileId.getMySQLFileId(), ByteBuffer.wrap(fileContent));

                TRACER.info(String.format("\r\nFile object :\r\n%s\r\nhas been upload successful\r\n", entity.toString()));
                if (result == 1) {
                    TransactionStatus code = TransactionStatus.OK;
                    WrappedResponse response = WrappedResponse.generate(code.value(), code.getReasonPhrase(), url);
                    if (redirectUrl == null || "".equals(redirectUrl.trim())) {
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.valueOf(FileType.JSON.getContentType()));
                        return new ResponseEntity<Object>(response, headers, HttpStatus.OK);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append(redirectUrl);
                        sb.append("&data=");
                        sb.append(url);
                        sb.append("&code=");
                        sb.append(code.value());
                        httpServletResponse.sendRedirect(sb.toString());
                        return null;
                    }
                } else {
                    return TraceErrorAndGetJson(TransactionStatus.BAD_REQUEST, "Insert file entity failed", null);
                }
            } else {
                return TraceErrorAndGetJson(TransactionStatus.DAMAGE_FILE, "File has been damaged", null);
            }
            /* 从数据流中读取文件失败 */
        } catch (IOException e) {
            return TraceErrorAndGetJson(TransactionStatus.UPLOAD_FAILED, "Get file entity", e);
            /* MD5验签失败 */
        } catch (Throwable t) {
            return TraceErrorAndGetJson(TransactionStatus.BAD_REQUEST, "Unknown issue", t);
        }
    }
    @RequestMapping(method = RequestMethod.POST, value = "/multiDirectUpload")
    @ResponseBody
    public ResponseEntity<Object> MultiUpload(
            @RequestParam(value = "file") MultipartFile[] file,
            @RequestParam(value = "sourceId") Long sourceId,
            @RequestParam(value = "errorFile", required = false) String errorFile,
            @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
            @RequestParam(value = "md5", required = false) String md5,
            HttpServletResponse httpServletResponse) {
        if (file != null && file.length > 0) {
            try {
                StringBuilder uploadResult = new StringBuilder();
                uploadResult.append(redirectUrl);
                uploadResult.append("&errorFile=" + errorFile);
                for (MultipartFile multipartFile : file) {
                    String fileName = URLEncoder.encode(multipartFile.getOriginalFilename().trim(), "utf-8");
                    if(errorFile == null || !errorFile.contains(fileName)){
                        byte[] fileContent = multipartFile.getBytes();
                        Boolean isChecked;
                        if (md5 != null) {
                            isChecked = EncryptionUtils.check(md5, fileContent);
                        } else {
                            isChecked = true;
                        }
                        if (isChecked) {
                            String fileSuffix = FileNameUtil.getPosixName(fileName);
                            FileType type = FileType.convertSuffix2Type(fileSuffix);
                            /*
                             * 实现fastDFS文件上传
                             */
                            StorageClient storageClient = new StorageClient(client.getConnection(), null);
                            String[] ret = storageClient.upload_file(fileContent, type.getPureSuffix(), null);
                            FileId fileId = FileIdFactory.Generate(sourceId, type.getValue());
                            FileEntity entity = new FileEntity();
                            entity.setFileId(fileId.getMySQLFileId());
                            entity.setFileLength((int) multipartFile.getSize());
                            entity.setFileUri(ret[1]);
                            entity.setGroupName(ret[0]);
                            entity.setIsPermanent(1);
                            entity.setSource(sourceId);
                            entity.setFileSuffix(type.getValue());
                            Date date = new Date();
                            date.setTime(System.currentTimeMillis());
                            entity.setCreateTime(date);
                            int result = fileDao.create(entity);
                            fileCacheService.putFile(fileId.getMySQLFileId(), ByteBuffer.wrap(fileContent));
                            TRACER.info(String.format("\r\nFile object :\r\n%s\r\nhas been upload successful\r\n", entity.toString()));
                            // 判断结果
                            if (result == 1) {
                                StringBuilder data = new StringBuilder();
                                data.append(config.getServiceUrl());
                                data.append(fileId.getFileId());
                                data.append(type.getFileSuffix());
                                TransactionStatus code = TransactionStatus.OK;
                                WrappedResponse response = WrappedResponse.generate(code.value(), code.getReasonPhrase(), data.toString());
                                StringBuilder sb = new StringBuilder();
                                sb.append("&url=");
                                sb.append(data.toString());
                                sb.append("&code=");
                                sb.append(code.value());
                                sb.append("&name=");
                                sb.append(fileName);
                                sb.append(";");
                                uploadResult.append(sb.toString());
                            } else {
                                StringBuilder sb = new StringBuilder();
                                sb.append("&code=");
                                sb.append(0);
                                sb.append("&name=");
                                sb.append(fileName);
                                sb.append(";");
                                uploadResult.append(sb.toString());
                            }
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append("&code=");
                            sb.append(0);
                            sb.append("&name=");
                            sb.append(fileName);
                            sb.append(";");
                            uploadResult.append(sb.toString());
                        }
                    }
                }
                String message = uploadResult.toString();
                httpServletResponse.sendRedirect(message);
                return null;
            } catch (IOException e) {
                return TraceErrorAndGetJson(TransactionStatus.UPLOAD_FAILED, "Get file entity", e);
                /* MD5验签失败 */
            } catch (Throwable t) {
                return TraceErrorAndGetJson(TransactionStatus.BAD_REQUEST, "Unknown issue", t);
            }
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/alioss/multiDirectUpload")
    @ResponseBody
    public ResponseEntity<Object> aliossMultiUpload(
            @RequestParam(value = "file") MultipartFile[] files,
            @RequestParam(value = "sourceId") Long sourceId,
            @RequestParam(value = "errorFile", required = false) String errorFile,
            @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
            @RequestParam(value = "md5", required = false) String md5,
            HttpServletResponse httpServletResponse) {
        if (files != null && files.length > 0) {
            OSSClient ossClient = null;
            try {
                StringBuilder uploadResult = new StringBuilder();
                uploadResult.append(redirectUrl);
                uploadResult.append("&errorFile=" + errorFile);
                for (MultipartFile file : files) {
                    String originalFileName = file.getOriginalFilename().trim();
                    if (errorFile == null || !errorFile.contains(originalFileName)) {
                        byte[] fileContent = file.getBytes();
                        Boolean isChecked;
                        if (md5 != null) {
                            isChecked = EncryptionUtils.check(md5, fileContent);
                        } else {
                            isChecked = true;
                        }
                        if (isChecked) {
                            String fileSuffix = FileNameUtil.getPosixName(file.getOriginalFilename());
                            FileType type = FileType.convertSuffix2Type(fileSuffix);

                            /*
                             * 实现ali oss文件上传
                             */
                            ossClient = new OSSClient(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());

                            InputStream input = file.getInputStream();
                            ObjectMetadata meta = new ObjectMetadata();                // 创建上传Object的Metadata
                            meta.setContentType(type.getContentType());        // 设置上传内容类型
                            meta.setCacheControl("no-cache");                    // 被下载时网页的缓存行为

                            // 以yyyy-M-d的格式获得当前日期，用于上传文件夹区分日期
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");
                            String fileName =  FileNameUtil.getPrefixName(file.getOriginalFilename()) + type.getFileSuffix();
                            FileId fileId = FileIdFactory.Generate(sourceId, type.getValue());
                            String filePath = ossConfig.getPicLocation() + simpleDateFormat.format(new Date()) + "/" + UUID.randomUUID() + fileId.getFileId() + type.getFileSuffix();
                            PutObjectRequest putObjectRequest = new PutObjectRequest(ossConfig.getBucketName(), filePath, input, meta);            //创建上传请求
                            ossClient.putObject(putObjectRequest);

                            // 公有url
                            //String url = ossConfig.getEndpoint().replaceFirst("http://", "http://" + ossConfig.getBucketName() + ".") + "/" + filePath;
                            String url = ossConfig.getSsoHost() + "/" + filePath;
                            /*
                             * 写入数据库
                             * */
                            FileEntity entity = new FileEntity();
                            entity.setFileId(fileId.getMySQLFileId());
                            entity.setGroupName("group1");
                            entity.setFileUri("");
                            entity.setFileLength((int) file.getSize());
                            entity.setFileUrl(url);
                            entity.setIsPermanent(1);
                            entity.setSource(sourceId);
                            entity.setFileSuffix(type.getValue());
                            Date date = new Date();
                            date.setTime(System.currentTimeMillis());
                            entity.setCreateTime(date);
                            int result = fileDao.create(entity);

                            fileCacheService.putFile(fileId.getMySQLFileId(), ByteBuffer.wrap(fileContent));
                            TRACER.info(String.format("\r\nFile object :\r\n%s\r\nhas been upload successful\r\n", entity.toString()));
                            // 判断结果
                            if (result == 1) {
                                TransactionStatus code = TransactionStatus.OK;
                                StringBuilder sb = new StringBuilder();
                                sb.append("&url=");
                                sb.append(url);
                                sb.append("&code=");
                                sb.append(code.value());
                                sb.append("&name=");
                                sb.append(URLEncoder.encode(fileName, "utf-8"));
                                sb.append(";");
                                uploadResult.append(sb.toString());
                            } else {
                                StringBuilder sb = new StringBuilder();
                                sb.append("&code=");
                                sb.append(0);
                                sb.append("&name=");
                                sb.append(fileName);
                                sb.append(";");
                                uploadResult.append(sb.toString());
                            }
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append("&code=");
                            sb.append(0);
                            sb.append("&name=");
                            sb.append(originalFileName);
                            sb.append(";");
                            uploadResult.append(sb.toString());
                        }
                    }
                }
                    String message = uploadResult.toString();
                httpServletResponse.sendRedirect(message);
                return null;
            } catch (IOException e) {
                return TraceErrorAndGetJson(TransactionStatus.UPLOAD_FAILED, "Get file entity", e);
                /* MD5验签失败 */
            } catch (Throwable t) {
                return TraceErrorAndGetJson(TransactionStatus.BAD_REQUEST, "Unknown issue", t);
            }
        }
        return null;
    }

    private ResponseEntity<Object> TraceErrorAndGetJson(TransactionStatus code, String errorMessage, Throwable t) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(FileType.JSON.getContentType()));
        WrappedResponse response = WrappedResponse.generate(code.value(), code.getReasonPhrase(), null);
        TRACER.error(errorMessage, t);
        return new ResponseEntity<Object>(response, headers, HttpStatus.OK);
    }
}
