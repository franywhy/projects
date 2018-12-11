package com.hq.adaptive.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hq.adaptive.pojo.AdlKnowledgeFilePOJO;
import com.hq.adaptive.service.AdlKnowledgeFileService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hq.adaptive.pojo.AdlKnowledgePOJO;
import com.hq.adaptive.pojo.query.AdlKnowledgeQuery;
import com.hq.adaptive.service.AdlKnowledgeService;

import io.renren.common.doc.SysLog;
import io.renren.common.validator.Assert;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.RRException;

import java.io.IOException;
import java.util.List;

/**  
 * 类说明
 * 知识点
 * @author shihongjie
 * @email  shihongjie@hengqijy.com
 * @date 2017年11月29日
 */
@RestController
@RequestMapping("/adaptive/knowledge")
public class AdlKnowledgeController {

    @Autowired
    private AdlKnowledgeService adlKnowledgeService;

    @Autowired
    private AdlKnowledgeFileService adlKnowledgeFileService;

    /**
     * 知识点列表
     *
     * @return 知识点列表信息
     * @throws ServletRequestBindingException
     */
    @RequiresPermissions("adaptive:adlknowledge:list")
    @RequestMapping("/list")
    public R List(HttpServletRequest request) throws ServletRequestBindingException {
        AdlKnowledgeQuery adlKnowledgeQuery = new AdlKnowledgeQuery();
        adlKnowledgeQuery.initPage(request);
        //课程ID
        Long courseId = ServletRequestUtils.getLongParameter(request, "courseId", 0);
        //章ID
        Long chapterId = ServletRequestUtils.getLongParameter(request, "chapterId", 0);
        //节ID
        Long sectionId = ServletRequestUtils.getLongParameter(request, "sectionId", 0);

        if (courseId == 0 && chapterId == 0 && sectionId == 0) {
            throw new RRException("请选择课程或者章节！");
        }
        adlKnowledgeQuery.setCourseId(courseId);
        adlKnowledgeQuery.setChapterId(chapterId);
        adlKnowledgeQuery.setSectionId(sectionId);
        //知识点名称
        String knowledgeName = ServletRequestUtils.getStringParameter(request, "knowledgeName");
        if (StringUtils.isNotBlank(knowledgeName)) {
            adlKnowledgeQuery.setKnowledgeName(knowledgeName);
        }
        //知识点编号
        String knowledgeNo = ServletRequestUtils.getStringParameter(request, "knowledgeNo");
        if (StringUtils.isNotBlank(knowledgeNo)) {
            adlKnowledgeQuery.setKnowledgeNo(knowledgeNo);
        }
        //考点
        String keyPointString = ServletRequestUtils.getStringParameter(request, "keyPoint");
        if (StringUtils.isNotBlank(keyPointString)) {
            adlKnowledgeQuery.setKeyPoint(Integer.valueOf(keyPointString));
        }
        //查询结果
        java.util.List<AdlKnowledgePOJO> adlKnowledgePOJOs = this.adlKnowledgeService.queryList(adlKnowledgeQuery);
        int total = this.adlKnowledgeService.queryTotal(adlKnowledgeQuery);
        PageUtils pageUtil = new PageUtils(adlKnowledgePOJOs, total, request);
        return R.ok().put(pageUtil);
    }


    @SysLog("智慧闯关系统-修改知识点资料")
    @RequestMapping("/update")
    public R update(@RequestBody AdlKnowledgePOJO adlKnowledgePOJO, HttpServletRequest request) {
        this.verifyForm(adlKnowledgePOJO);
        this.adlKnowledgeService.updateZL(adlKnowledgePOJO);
        return R.ok();
    }

    @RequestMapping("/info/{knowledgeId}")
    public R info(@PathVariable("knowledgeId") Long knowledgeId, HttpServletRequest request) {
        if (null == knowledgeId) {
            throw new RRException("知识点ID不能为空！");
        }
        AdlKnowledgePOJO adlKnowledgePOJO = this.adlKnowledgeService.queryObject(knowledgeId);
        return R.ok().put(adlKnowledgePOJO);
    }

    /**
     * 验证参数是否正确
     */
    private void verifyForm(AdlKnowledgePOJO adlKnowledgePOJO) {
        Assert.isNull(adlKnowledgePOJO, "知识点对象不能为空！");
        Assert.isNull(adlKnowledgePOJO.getKnowledgeId(), "知识点对象不能为空！");
    }

    /**
     * 批量导入
     *
     * @return
     */
    @SysLog("批量导入知识点视频")
    @RequiresPermissions("adaptive:adlknowledge:importExcel")
    @RequestMapping("/importExcel")
    public R importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest mpReq = (MultipartHttpServletRequest) request;
        MultipartFile file = mpReq.getFile("file_data");
        Long courseId = ServletRequestUtils.getLongParameter(request, "courseId", 0);
        Long productId = ServletRequestUtils.getLongParameter(request, "productId", 0);
        String importExcel = this.adlKnowledgeService.importExcel(courseId, productId, file);
        return R.ok().put(importExcel);
    }

    @SysLog("智慧闯关系统-上传知识点资料")
    @RequestMapping("/uploadKnowledgeFile")
    public R uploadKnowledgeFile(@RequestBody String fileStr) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, AdlKnowledgeFilePOJO.class);
        List<AdlKnowledgeFilePOJO> list = objectMapper.readValue(fileStr, javaType);
        for (AdlKnowledgeFilePOJO file : list) {
            String fileName = file.getFileName();
            String knowledgeNo = fileName.substring(0, fileName.indexOf("."));
            AdlKnowledgePOJO adlKnowledgePOJO = new AdlKnowledgePOJO();
            adlKnowledgePOJO.setKnowledgeNo(knowledgeNo);
            adlKnowledgePOJO = adlKnowledgeService.queryObject(adlKnowledgePOJO);
            file.setKnowledgeId(adlKnowledgePOJO.getKnowledgeId());
            file.setFileName(adlKnowledgePOJO.getKnowledgeName()+"-资料");
            adlKnowledgeFileService.delete(file.getKnowledgeId());
            adlKnowledgeFileService.save(file);
        }
        return R.ok();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/checkFile")
    @ResponseBody
    public R multiUpload(
            @RequestBody String fileNameStr) {
        StringBuffer sb = new StringBuffer();
        if(fileNameStr != null){
            String[] fileNames = fileNameStr.split(",");
            if (fileNames != null && fileNames.length > 0) {
                for (String fileName : fileNames) {
                    fileName = fileName.trim();
                    String knowledgeNo = fileName.substring(0,fileName.indexOf("."));
                    if(adlKnowledgeService.queryKnowledgeIdByNo(null, knowledgeNo) == null){
                        sb.append(fileName+",");
                    }
                }
            }
        }

        return R.ok(sb.toString());
    }
}
