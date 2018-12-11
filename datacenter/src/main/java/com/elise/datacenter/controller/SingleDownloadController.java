package com.elise.datacenter.controller;

import com.bluewhale.common.enumeration.TransactionStatus;
import com.bluewhale.common.prototype.WrappedResponse;
import com.elise.datacenter.dao.FileDao;
import com.elise.datacenter.entity.FileEntity;
import com.elise.datacenter.file.FileId;
import com.elise.datacenter.file.FileIdFactory;
import com.elise.datacenter.file.FileNameUtil;
import com.elise.datacenter.file.FileType;
import com.elise.datacenter.service.FileCacheService;
import org.csource.fastdfs.FileInfo;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.ByteBuffer;

/**
 * Created by Glenn on 2017/4/15 0015.
 */

@Controller
@RequestMapping("/file")
@CrossOrigin
public class SingleDownloadController {

    private static final Logger TRACER = LoggerFactory.getLogger(SingleDownloadController.class);

    @Autowired
    private FileDao fileDao;

    @Autowired
    private TrackerClient client;

    @Autowired
    private FileCacheService fileCacheService;

    @RequestMapping(method = RequestMethod.GET, value = "/singleDirectDownload/{fileId}")
    public ResponseEntity<Object> download(@PathVariable String fileId,
                                           HttpServletRequest request,
                                           HttpServletResponse httpServletResponse) {
        try {
            Long time = request.getDateHeader("If-Modified-Since");
            //If time is larger than zero,give 304 response to client;
            if(time > 0){
                httpServletResponse.setCharacterEncoding("UTF-8");
                httpServletResponse.sendError(HttpStatus.NOT_MODIFIED.value());
                return null;
            }
            FileId id = FileIdFactory.Generate(FileNameUtil.getPrefixName(fileId));
            FileType type = FileType.convertValue2Type(id.getFileType());
            if (type != null) {
                ByteBuffer buffer = fileCacheService.getFile(id.getMySQLFileId());
                byte[] fileContent;
                FileEntity entity;
                if (buffer.capacity() == 0) {
                    entity = fileDao.queryObject(id.getMySQLFileId());
                    /*Can't get file instance from database*/
                    if (entity == null) {
                        TRACER.error("\r\nFile " + fileId + " not found\r\n");
                        return errorResponse(TransactionStatus.NOT_FOUND);
                    }
                    /*
                     * Get file bytes from FastDFS
                     */
                    StorageClient storageClient = new StorageClient(client.getConnection(), null);
                    FileInfo fi = storageClient.get_file_info(entity.getGroupName(), entity.getFileUri());
                    if (fi == null) {
                        throw new Exception("File information from FastDFS is null");
                    }
                    fileContent = storageClient.download_file(entity.getGroupName(), entity.getFileUri());
                    if (fileContent == null) {
                        throw new Exception("File entity from FastDFS is null");
                    }
                    fileCacheService.putFile(id.getMySQLFileId(),ByteBuffer.wrap(fileContent));
                } else {
                    fileContent = buffer.array();
                    entity = new FileEntity();
                    entity.setFileId(id.getMySQLFileId());
                    entity.setFileLength(fileContent.length);
                }
                HttpHeaders headers = new HttpHeaders();
                StringBuilder sb = new StringBuilder();
                sb.append(fileId);
                sb.append(".");
                sb.append(type.getPureSuffix());
                if (type.isAttachment()) {
                    headers.setContentDispositionFormData("attachment", sb.toString());
                }else{
                    // Set "Content-Disposition Header" into "inline" value
                    headers.set("Content-Disposition","inline;filename="+sb.toString());
                }
                //For http format, add current time as header;
                headers.setLastModified(System.currentTimeMillis());
                headers.setContentType(MediaType.valueOf(type.getContentType()));
                headers.setCacheControl("no-cache");
                /*文件下载成功,并返回*/
                TRACER.info(String.format("\r\nnFile entity\r\n%s\r\nhas been download\r\n", entity.toString()));
                return new ResponseEntity<Object>(fileContent, headers, HttpStatus.OK);
            } else {
                /*文件类型不支持,判定为非法请求*/
                TRACER.error("\r\nInvalid fileId\r\n");
                return errorResponse(TransactionStatus.REQUEST_REJECT);
            }
        } catch (Exception e) {
            TRACER.error("\r\nGet file from FastDFS failed\r\n", e);
            return errorResponse(TransactionStatus.NOT_FOUND);
        } catch (Throwable t) {
            TRACER.error("\r\nUnknown issue\r\n", t);
            return errorResponse(TransactionStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<Object> errorResponse(TransactionStatus code) {
        WrappedResponse response = WrappedResponse.generate(code.value(), code.getReasonPhrase(),null);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(FileType.JSON.getContentType()));
        return new ResponseEntity<Object>(response, headers, HttpStatus.OK);
    }
}