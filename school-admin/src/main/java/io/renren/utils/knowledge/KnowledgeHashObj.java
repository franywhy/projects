package io.renren.utils.knowledge;

import java.util.List;

public class KnowledgeHashObj {

    private Long knowledegId;
    private List<Long> childList;

    public KnowledgeHashObj(Long knowledegId, List<Long> childList) {
        this.knowledegId = knowledegId;
        this.childList = childList;
    }

    @Override
    public String toString() {
        return "KnowledgeHashObj{" +
                "knowledegId=" + knowledegId +
                ", childList=" + childList +
                '}';
    }

    public Long getKnowledegId() {
        return knowledegId;
    }

    public void setKnowledegId(Long knowledegId) {
        this.knowledegId = knowledegId;
    }

    public List<Long> getChildList() {
        return childList;
    }

    public void setChildList(List<Long> childList) {
        this.childList = childList;
    }
}
