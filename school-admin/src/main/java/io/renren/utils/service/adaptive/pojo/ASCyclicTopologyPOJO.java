package io.renren.utils.service.adaptive.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * 检查知识空间是否为有向无环图
 */
public class ASCyclicTopologyPOJO {
    private Long courseId;
    private List<ASCyclicTopologyKnowledgeNode> knowledgeNodeSet;

    public ASCyclicTopologyPOJO() {
        this.knowledgeNodeSet = new ArrayList<>();
    }

    public ASCyclicTopologyPOJO(Long courseId) {
        this.courseId = courseId;
        this.knowledgeNodeSet = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "ASCyclicTopologyPOJO{" +
                "courseId=" + courseId +
                ", knowledgeNodeSet=" + knowledgeNodeSet +
                '}';
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public List<ASCyclicTopologyKnowledgeNode> getKnowledgeNodeSet() {
        return knowledgeNodeSet;
    }

    public void setKnowledgeNodeSet(List<ASCyclicTopologyKnowledgeNode> knowledgeNodeSet) {
        this.knowledgeNodeSet = knowledgeNodeSet;
    }

}
