package io.renren.utils.service.adaptive.pojo;

import java.util.List;

/**
 * 根据知识空间生成决策树
 */
public class AdlDecisionForestPOJO {
    //课程PK
    private Long courseId;
    //阶段PK
    private Long phaseId;
    //注释
    private int maxTreeNode;
    //版本
    private int versionNo;
    //树形知识点对象集合
    private List<AdlDecisionForestNode> graphNodeSet;
    //随机知识点对象集合
    private List<AdlDecisionForestNode> randomNodeSet;




    @Override
    public String toString() {
        return "AdlDecisionForestPOJO{" +
                "courseId=" + courseId +
                ", phaseId=" + phaseId +
                ", maxTreeNode=" + maxTreeNode +
                ", versionNo=" + versionNo +
                ", graphNodeSet=" + graphNodeSet +
                ", randomNodeSet=" + randomNodeSet +
                '}';
    }

    public AdlDecisionForestPOJO() {
        this.maxTreeNode = AsConfig.MAXTREENODE_DEFAULT;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(Long phaseId) {
        this.phaseId = phaseId;
    }

    public int getMaxTreeNode() {
        return maxTreeNode;
    }

    public void setMaxTreeNode(int maxTreeNode) {
        this.maxTreeNode = maxTreeNode;
    }

    public int getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(int versionNo) {
        this.versionNo = versionNo;
    }

    public List<AdlDecisionForestNode> getGraphNodeSet() {
        return graphNodeSet;
    }

    public void setGraphNodeSet(List<AdlDecisionForestNode> graphNodeSet) {
        this.graphNodeSet = graphNodeSet;
    }

    public List<AdlDecisionForestNode> getRandomNodeSet() {
        return randomNodeSet;
    }

    public void setRandomNodeSet(List<AdlDecisionForestNode> randomNodeSet) {
        this.randomNodeSet = randomNodeSet;
    }
}
