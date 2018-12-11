package com.elise.datacenter.controller;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.bluewhale.common.enumeration.TransactionStatus;
import com.bluewhale.common.prototype.WrappedResponse;
import com.bluewhale.common.util.EncryptionUtils;
import com.bluewhale.common.util.SchoolIdUtil;
import com.bluewhale.http.HttpConnManager;
import com.bluewhale.http.HttpPlainResult;
import com.bluewhale.http.HttpResultDetail;
import com.bluewhale.http.HttpResultHandler;
import com.elise.datacenter.config.ALiOSSConfig;
import com.elise.datacenter.config.LocalConfigEntity;
import com.elise.datacenter.dao.FileDao;
import com.elise.datacenter.entity.FileEntity;
import com.elise.datacenter.entity.UserTokenEntity;
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
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Glenn on 2017/5/15 0015.
 */
@Controller
@RequestMapping("/file")
@CrossOrigin
public class SingleFile4APIController {

    private static final Logger TRACER = LoggerFactory.getLogger(SingleFile4APIController.class);

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

    @Autowired
    private HttpConnManager httpConnManager;

    @RequestMapping(method = RequestMethod.POST, value = "/singleFile4API")
    public ResponseEntity<Object> upload(
            @RequestParam(value = "file") MultipartFile file,
            @RequestParam(value = "token") String token,
            @RequestParam(value = "md5", required = false) String md5,
            HttpServletRequest request) {
        try {
            byte[] fileContent = file.getBytes();
            Boolean isChecked;
            if (md5 != null) {
                isChecked = EncryptionUtils.check(md5, fileContent);
            } else {
                isChecked = true;
            }
            if (isChecked) {
                UserTokenEntity userToken = null;
                HashMap<String, Object> map = new HashMap<>();
                map.put("token", token);
                String schoolId = SchoolIdUtil.getSchoolId(request);
                HttpPlainResult httpResult = httpConnManager.invoke(HttpMethod.GET, config.getSsoHost() + "/inner/userTokenDetail", map, schoolId);
                TRACER.info("\r\n"+httpResult.getResult()+"\r\n");
                HttpResultDetail<UserTokenEntity> detail = HttpResultHandler.handle(httpResult, UserTokenEntity.class);
                TRACER.info("\r\n"+detail.toString()+"\r\n");
                if (detail.isOK()) {
                    userToken = detail.getResult();
                } else if (detail.isClientError()) {
                    TransactionStatus code = TransactionStatus.BAD_REQUEST;
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.valueOf(FileType.JSON.getContentType()));
                    WrappedResponse response = WrappedResponse.generate(detail.getResponseStatus().value(), detail.getResponseMessage(),null);
                    return new ResponseEntity<>(response, headers, HttpStatus.OK);
                } else {
                    TransactionStatus code = TransactionStatus.INTERNAL_SERVER_ERROR;
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.valueOf(FileType.JSON.getContentType()));
                    WrappedResponse response = WrappedResponse.generate(detail.getResponseStatus().value(), detail.getResponseMessage(),null);
                    return new ResponseEntity<>(response, headers, HttpStatus.OK);
                }

                String fileSuffix = FileNameUtil.getPosixName(file.getOriginalFilename());
                FileType type = FileType.convertSuffix2Type(fileSuffix);
                /*
                 * 实现fastDFS文件上传
                 */
                StorageClient storageClient = new StorageClient(client.getConnection(), null);
                String[] ret = storageClient.upload_file(fileContent,type.getPureSuffix(), null);
                FileId fileId = FileIdFactory.Generate((long)userToken.getUserId(), type.getValue());
                FileEntity entity = new FileEntity();
                entity.setFileId(fileId.getMySQLFileId());
                entity.setFileLength((int) file.getSize());
                entity.setFileUri(ret[1]);
                entity.setGroupName(ret[0]);
                entity.setIsPermanent(1);
                entity.setSource((long)userToken.getUserId());
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
                    WrappedResponse response = WrappedResponse.generate(code.value(),code.getReasonPhrase(), data.toString());
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.valueOf(FileType.JSON.getContentType()));
                    return new ResponseEntity<>(response, headers, HttpStatus.OK);
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




    @RequestMapping(method = RequestMethod.POST, value = "/uploadFiles4API")
    public ResponseEntity<Object> uploadFiles_v410(
            @RequestParam(value = "file") MultipartFile[] file,
            @RequestParam(value = "token") String token,
            @RequestParam(value = "md5", required = false) String md5,
            HttpServletRequest request) {
        StringBuilder data = new StringBuilder();
        String[] md5Array = null;
        if (md5 != null){
            md5Array = md5.split(",");
        }
        if (file != null && file.length > 0){
            try {
                for (int i = 0; i < file.length; i++) {
                    MultipartFile multipartFile = file[i];
                    byte[] fileContent = multipartFile.getBytes();
                    Boolean isChecked = false;
                    if (md5Array != null) {
                        isChecked = EncryptionUtils.check(md5Array[i], fileContent);
                    } else {
                        isChecked = true;
                    }
                    if (isChecked) {
                        UserTokenEntity userToken = null;
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("token", token);
                        String schoolId = SchoolIdUtil.getSchoolId(request);
                        HttpPlainResult httpResult = httpConnManager.invoke(HttpMethod.GET, config.getSsoHost() + "/inner/userTokenDetail", map, schoolId);
                        TRACER.info("\r\n"+httpResult.getResult()+"\r\n");
                        HttpResultDetail<UserTokenEntity> detail = HttpResultHandler.handle(httpResult, UserTokenEntity.class);
                        TRACER.info("\r\n"+detail.toString()+"\r\n");
                        if (detail.isOK()) {
                            userToken = detail.getResult();
                        } else if (detail.isClientError()) {
                            TransactionStatus code = TransactionStatus.BAD_REQUEST;
                            HttpHeaders headers = new HttpHeaders();
                            headers.setContentType(MediaType.valueOf(FileType.JSON.getContentType()));
                            WrappedResponse response = WrappedResponse.generate(detail.getResponseStatus().value(), detail.getResponseMessage(),null);
                            return new ResponseEntity<>(response, headers, HttpStatus.OK);
                        } else {
                            TransactionStatus code = TransactionStatus.INTERNAL_SERVER_ERROR;
                            HttpHeaders headers = new HttpHeaders();
                            headers.setContentType(MediaType.valueOf(FileType.JSON.getContentType()));
                            WrappedResponse response = WrappedResponse.generate(detail.getResponseStatus().value(), detail.getResponseMessage(),null);
                            return new ResponseEntity<>(response, headers, HttpStatus.OK);
                        }

                        String fileSuffix = FileNameUtil.getPosixName(multipartFile.getOriginalFilename());
                        FileType type = FileType.convertSuffix2Type(fileSuffix);
                        /*
                         * 实现fastDFS文件上传
                         */
                        StorageClient storageClient = new StorageClient(client.getConnection(), null);
                        String[] ret = storageClient.upload_file(fileContent,type.getPureSuffix(), null);
                        FileId fileId = FileIdFactory.Generate((long)userToken.getUserId(), type.getValue());
                        FileEntity entity = new FileEntity();
                        entity.setFileId(fileId.getMySQLFileId());
                        entity.setFileLength((int) multipartFile.getSize());
                        entity.setFileUri(ret[1]);
                        entity.setGroupName(ret[0]);
                        entity.setIsPermanent(1);
                        entity.setSource((long)userToken.getUserId());
                        entity.setFileSuffix(type.getValue());
                        Date date = new Date();
                        date.setTime(System.currentTimeMillis());
                        entity.setCreateTime(date);
                        int result = fileDao.create(entity);
                        fileCacheService.putFile(fileId.getMySQLFileId(), ByteBuffer.wrap(fileContent));
                        TRACER.info(String.format("\r\nFile object :\r\n%s\r\nhas been upload successful\r\n", entity.toString()));
                        // 判断结果
                        if (result == 1) {
                            data.append(config.getServiceUrl());
                            data.append(fileId.getFileId());
                            data.append(type.getFileSuffix());
                            data.append(",");
                        } else {
                            return TraceErrorAndGetJson(TransactionStatus.BAD_REQUEST, "Insert file entity failed", null);
                        }
                    } else {
                        return TraceErrorAndGetJson(TransactionStatus.DAMAGE_FILE, "File has been damaged", null);
                    }
                }
                TransactionStatus code = TransactionStatus.OK;
                WrappedResponse response = WrappedResponse.generate(code.value(),code.getReasonPhrase(), data.toString());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.valueOf(FileType.JSON.getContentType()));
                return new ResponseEntity<>(response, headers, HttpStatus.OK);
                /* 从数据流中读取文件失败 */
            } catch (IOException e) {
                return TraceErrorAndGetJson(TransactionStatus.UPLOAD_FAILED, "Get file entity", e);
                /* MD5验签失败 */
            } catch (Throwable t) {
                return TraceErrorAndGetJson(TransactionStatus.BAD_REQUEST, "Unknown issue", t);
            }
        }
        return null;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/alioss/singleFile4API")
    public ResponseEntity<Object> aliOSSUpload(
            @RequestParam(value = "file") MultipartFile file,
            @RequestParam(value = "token") String token,
            @RequestParam(value = "md5", required = false) String md5,
            HttpServletRequest request) {

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
                UserTokenEntity userToken = null;
                HashMap<String, Object> map = new HashMap<>();
                map.put("token", token);
                String schoolId = SchoolIdUtil.getSchoolId(request);
                HttpPlainResult httpResult = httpConnManager.invoke(HttpMethod.GET, config.getSsoHost() + "/inner/userTokenDetail", map, schoolId);
                TRACER.info("\r\n" + httpResult.getResult() + "\r\n");
                HttpResultDetail<UserTokenEntity> detail = HttpResultHandler.handle(httpResult, UserTokenEntity.class);
                TRACER.info("\r\n" + detail.toString() + "\r\n");
                if (detail.isOK()) {
                    userToken = detail.getResult();
                } else if (detail.isClientError()) {
                    TransactionStatus code = TransactionStatus.BAD_REQUEST;
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.valueOf(FileType.JSON.getContentType()));
                    WrappedResponse response = WrappedResponse.generate(code.value(), code.getReasonPhrase(), null);
                    return new ResponseEntity<>(response, headers, HttpStatus.OK);
                } else {
                    TransactionStatus code = TransactionStatus.INTERNAL_SERVER_ERROR;
                    HttpHeaders headers = new HttpHeaders();

                    headers.setContentType(MediaType.valueOf(FileType.JSON.getContentType()));
                    WrappedResponse response = WrappedResponse.generate(code.value(), code.getReasonPhrase(), null);
                    return new ResponseEntity<>(response, headers, HttpStatus.OK);
                }

                // 获取 文件名 和 文件类型实体
                String fileSuffix = FileNameUtil.getPosixName(file.getOriginalFilename());
                FileType type = FileType.convertSuffix2Type(fileSuffix);
                /*
                 * 实现ali oss文件上传
                 *
                 */
                ossClient = new OSSClient(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());

                InputStream input = file.getInputStream();
                ObjectMetadata meta = new ObjectMetadata();                // 创建上传Object的Metadata
                meta.setContentType(type.getContentType());        // 设置上传内容类型
                meta.setCacheControl("no-cache");                    // 被下载时网页的缓存行为

                // 以yyyy-M-d的格式获得当前日期，用于上传文件夹区分日期
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");
                FileId fileId = FileIdFactory.Generate((long) userToken.getUserId(), type.getValue());
                String filePath = ossConfig.getPicLocation() + simpleDateFormat.format(new Date()) + "/" + UUID.randomUUID() + fileId.getFileId() + type.getFileSuffix();
                PutObjectRequest putObjectRequest = new PutObjectRequest(ossConfig.getBucketName(), filePath, input, meta);            //创建上传请求
                ossClient.putObject(putObjectRequest);

                // 私有加密url
                //Date expiration = new Date(new Date().getTime() + 3600 * 1000);
                //String url = ossClient.putObject(file, fileType, config.getPicLocation() + fileName + "." + fileType);

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
                entity.setSource((long) userToken.getUserId());
                entity.setFileSuffix(type.getValue());
                Date date = new Date();
                date.setTime(System.currentTimeMillis());
                entity.setCreateTime(date);
                int result = fileDao.create(entity);

                /*
                 * 写入Cache
                 * */
                fileCacheService.putFile(fileId.getMySQLFileId(), ByteBuffer.wrap(fileContent));
                TRACER.info(String.format("\r\nFile object :\r\n%s\r\nhas been upload successful\r\n", entity.toString()));
                // 判断结果
                if (result == 1) {
                    TransactionStatus code = TransactionStatus.OK;
                    WrappedResponse response = WrappedResponse.generate(code.value(), code.getReasonPhrase(), url);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.valueOf(FileType.JSON.getContentType()));
                    return new ResponseEntity<>(response, headers, HttpStatus.OK);
                } else {
                    return TraceErrorAndGetJson(TransactionStatus.BAD_REQUEST, "Insert file entity failed", null);
                }
            } else {
                return TraceErrorAndGetJson(TransactionStatus.DAMAGE_FILE, "File has been damaged", null);
            }
            /* 从数据流中读取文件失败 */
        } catch (OSSException oe) {
            oe.printStackTrace();
            return null;
        } catch (ClientException ce) {
            ce.printStackTrace();
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            return TraceErrorAndGetJson(TransactionStatus.UPLOAD_FAILED, "Get file entity", e);
            /* MD5验签失败 */
        } catch (Throwable t) {
            return TraceErrorAndGetJson(TransactionStatus.BAD_REQUEST, "Unknown issue", t);
        } finally {
            if (ossClient != null)
                ossClient.shutdown();
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/alioss/uploadFiles4API")
    public ResponseEntity<Object> aliossUploadFiles_v410(
            @RequestParam(value = "file") MultipartFile[] files,
            @RequestParam(value = "token") String token,
            @RequestParam(value = "md5", required = false) String md5,
            HttpServletRequest request) {
        StringBuilder data = new StringBuilder();
        String[] md5Array = null;
        if (md5 != null){
            md5Array = md5.split(",");
        }
        if (files != null && files.length > 0){
            OSSClient ossClient = null;
            try {
                for (int i = 0; i < files.length; i++) {
                    MultipartFile file = files[i];
                    byte[] fileContent = file.getBytes();
                    Boolean isChecked = false;
                    if (md5Array != null) {
                        isChecked = EncryptionUtils.check(md5Array[i], fileContent);
                    } else {
                        isChecked = true;
                    }
                    if (isChecked) {
                        UserTokenEntity userToken = null;
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("token", token);
                        String schoolId = SchoolIdUtil.getSchoolId(request);
                        HttpPlainResult httpResult = httpConnManager.invoke(HttpMethod.GET, config.getSsoHost() + "/inner/userTokenDetail", map, schoolId);
                        TRACER.info("\r\n"+httpResult.getResult()+"\r\n");
                        HttpResultDetail<UserTokenEntity> detail = HttpResultHandler.handle(httpResult, UserTokenEntity.class);
                        TRACER.info("\r\n"+detail.toString()+"\r\n");
                        if (detail.isOK()) {
                            userToken = detail.getResult();
                        } else if (detail.isClientError()) {
                            TransactionStatus code = TransactionStatus.BAD_REQUEST;
                            HttpHeaders headers = new HttpHeaders();
                            headers.setContentType(MediaType.valueOf(FileType.JSON.getContentType()));
                            WrappedResponse response = WrappedResponse.generate(detail.getResponseStatus().value(), detail.getResponseMessage(),null);
                            return new ResponseEntity<>(response, headers, HttpStatus.OK);
                        } else {
                            TransactionStatus code = TransactionStatus.INTERNAL_SERVER_ERROR;
                            HttpHeaders headers = new HttpHeaders();
                            headers.setContentType(MediaType.valueOf(FileType.JSON.getContentType()));
                            WrappedResponse response = WrappedResponse.generate(detail.getResponseStatus().value(), detail.getResponseMessage(),null);
                            return new ResponseEntity<>(response, headers, HttpStatus.OK);
                        }

                        // 获取 文件名 和 文件类型实体
                        String fileSuffix = FileNameUtil.getPosixName(file.getOriginalFilename());
                        FileType type = FileType.convertSuffix2Type(fileSuffix);
                        /*
                         * 实现ali oss文件上传
                         *
                         */
                        ossClient = new OSSClient(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());

                        InputStream input = file.getInputStream();
                        ObjectMetadata meta = new ObjectMetadata();                // 创建上传Object的Metadata
                        meta.setContentType(type.getContentType());        // 设置上传内容类型
                        meta.setCacheControl("no-cache");                    // 被下载时网页的缓存行为

                        // 以yyyy-M-d的格式获得当前日期，用于上传文件夹区分日期
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");
                        FileId fileId = FileIdFactory.Generate((long) userToken.getUserId(), type.getValue());
                        String filePath = ossConfig.getPicLocation() + simpleDateFormat.format(new Date()) + "/" + UUID.randomUUID() + fileId.getFileId() + type.getFileSuffix();;
                        PutObjectRequest putObjectRequest = new PutObjectRequest(ossConfig.getBucketName(), filePath, input, meta);            //创建上传请求
                        ossClient.putObject(putObjectRequest);

                        // 私有加密url
                        //Date expiration = new Date(new Date().getTime() + 3600 * 1000);
                        //String url = ossClient.putObject(file, fileType, config.getPicLocation() + fileName + "." + fileType);

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
                        entity.setSource((long) userToken.getUserId());
                        entity.setFileSuffix(type.getValue());
                        Date date = new Date();
                        date.setTime(System.currentTimeMillis());
                        entity.setCreateTime(date);
                        int result = fileDao.create(entity);

                        fileCacheService.putFile(fileId.getMySQLFileId(), ByteBuffer.wrap(fileContent));
                        TRACER.info(String.format("\r\nFile object :\r\n%s\r\nhas been upload successful\r\n", entity.toString()));
                        // 判断结果
                        if (result == 1) {
                            data.append(url);
                            data.append(",");
                        } else {
                            return TraceErrorAndGetJson(TransactionStatus.BAD_REQUEST, "Insert file entity failed", null);
                        }
                    } else {
                        return TraceErrorAndGetJson(TransactionStatus.DAMAGE_FILE, "File has been damaged", null);
                    }
                }
                TransactionStatus code = TransactionStatus.OK;
                WrappedResponse response = WrappedResponse.generate(code.value(),code.getReasonPhrase(), data.toString());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.valueOf(FileType.JSON.getContentType()));
                return new ResponseEntity<>(response, headers, HttpStatus.OK);
                /* 从数据流中读取文件失败 */
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
        TRACER.error(errorMessage, t);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(FileType.JSON.getContentType()));
        WrappedResponse response = WrappedResponse.generate(code.value(), code.getReasonPhrase(), null);
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
}