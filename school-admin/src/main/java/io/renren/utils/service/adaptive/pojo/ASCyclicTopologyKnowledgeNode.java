package io.renren.utils.service.adaptive.pojo;

import java.util.List;

/**
 * 检查知识空间是否为有向无环图
 */
public class ASCyclicTopologyKnowledgeNode {

    //节点数量
    private int nextNodeQuantity;
    //当前节点ID
    private Long nodeId;
    //子节点ID集合
    private List<Long> priorNodeSet;
    //权重
    private int weight;

    public ASCyclicTopologyKnowledgeNode() {
        this.weight = AsConfig.WEIGHT_DEFAULT;
    }

    @Override
    public String toString() {
        return "ASCyclicTopologyKnowledgeNode{" +
                "nextNodeQuantity=" + nextNodeQuantity +
                ", nodeId=" + nodeId +
                ", priorNodeSet=" + priorNodeSet +
                ", weight=" + weight +
                '}';
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getNextNodeQuantity() {
        return nextNodeQuantity;
    }

    public void setNextNodeQuantity(int nextNodeQuantity) {
        this.nextNodeQuantity = nextNodeQuantity;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public List<Long> getPriorNodeSet() {
        return priorNodeSet;
    }

    public void setPriorNodeSet(List<Long> priorNodeSet) {
        this.priorNodeSet = priorNodeSet;
    }
}
