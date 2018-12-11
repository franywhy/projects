package com.hq.learningapi.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2018/1/18 0018.
 */
public class KnowledgeFilePOJO implements Serializable {

    private Long userId; //用户ID
    private Long phaseId; //阶段ID

    private Long knowledgeId; //当前知识点ID,如果第一次获取null
    private String knowledgeName; //知识点名称
    private String fileUrl; //        资料地址
    private String fileName;//文件名称
    private String polyvVid; //保利威视视频VID，待定，有可能是视频地址
    private String pplyvName; //        视频名称
    private String employvPicail; //视频首截图
    private String ployvPic; //视频首截图
    private Long polyvDuration;//播放时长:毫秒
    private Long vtype;//观看记录 0.未观看 1.已观看

    private String multiClassesId;
    private String titleName;
    private String filePic;
    private String examUUID;

    public String getMultiClassesId() {
        return multiClassesId;
    }

    public void setMultiClassesId(String multiClassesId) {
        this.multiClassesId = multiClassesId;
    }

    private Date modifiedTime;
    private String remake;

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    private Date creationTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(Long phaseId) {
        this.phaseId = phaseId;
    }

    public Long getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(Long knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public String getKnowledgeName() {
        return knowledgeName;
    }

    public void setKnowledgeName(String knowledgeName) {
        this.knowledgeName = knowledgeName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPolyvVid() {
        return polyvVid;
    }

    public void setPolyvVid(String polyvVid) {
        this.polyvVid = polyvVid;
    }

    public String getPplyvName() {
        return pplyvName;
    }

    public void setPplyvName(String pplyvName) {
        this.pplyvName = pplyvName;
    }

    public String getPloyvPic() {
        return ployvPic;
    }

    public void setPloyvPic(String ployvPic) {
        this.ployvPic = ployvPic;
    }

    public Long getPolyvDuration() {
        return polyvDuration;
    }

    public void setPolyvDuration(Long polyvDuration) {
        this.polyvDuration = polyvDuration;
    }

    public Long getVtype() {
        return vtype;
    }

    public void setVtype(Long vtype) {
        this.vtype = vtype;
    }


    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }


    public String getFilePic() {
        return filePic;
    }

    public void setFilePic(String filePic) {
        this.filePic = filePic;
    }

    public String getEmployvPicail() {
        return employvPicail;
    }

    public void setEmployvPicail(String employvPicail) {
        this.employvPicail = employvPicail;
    }

    public String getExamUUID() {
        return examUUID;
    }

    public void setExamUUID(String examUUID) {
        this.examUUID = examUUID;
    }
}
