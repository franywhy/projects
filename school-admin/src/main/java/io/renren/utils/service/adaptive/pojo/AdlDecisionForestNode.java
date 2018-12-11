package io.renren.utils.service.adaptive.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * 根据知识空间生成决策树
 */
public class AdlDecisionForestNode {
//    private int nextNodeQuantity;
    //知识点ID
    private long nodeId;
    //知识点包含知识点ID
    private List<Long> priorNodeSet;

    public AdlDecisionForestNode() {
//        nextNodeQuantity = 0;
    }

    public AdlDecisionForestNode(long nodeId, List<Long> priorNodeSet) {
        this.nodeId = nodeId;
        this.priorNodeSet = priorNodeSet;
    }

    @Override
    public String toString() {
        return "AdlDecisionForestNode{" +
//                "nextNodeQuantity=" + nextNodeQuantity +
                ", nodeId=" + nodeId +
                ", priorNodeSet=" + priorNodeSet +
                '}';
    }

//    public int getNextNodeQuantity() {
//        return nextNodeQuantity;
//    }
//
//    public void setNextNodeQuantity(int nextNodeQuantity) {
//        this.nextNodeQuantity = nextNodeQuantity;
//    }

    public long getNodeId() {
        return nodeId;
    }

    public void setNodeId(long nodeId) {
        this.nodeId = nodeId;
    }

    public List<Long> getPriorNodeSet() {
        return priorNodeSet;
    }

    public void setPriorNodeSet(List<Long> priorNodeSet) {
        this.priorNodeSet = priorNodeSet;
    }
}
