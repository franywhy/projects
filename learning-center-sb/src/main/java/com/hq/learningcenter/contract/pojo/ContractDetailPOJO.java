package com.hq.learningcenter.contract.pojo;

/**
 * @auther linchaokai
 * @description 支付项目
 * @date 2018/6/6
 */
public class ContractDetailPOJO {
    private String subjectName;//收支项目名称
    private double dcost;//标准费用
    private double ddiscount;//优惠金额
    private double dnshoulddcost;//应收金额

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public double getDcost() {
        return dcost;
    }

    public void setDcost(double dcost) {
        this.dcost = dcost;
    }

    public double getDdiscount() {
        return ddiscount;
    }

    public void setDdiscount(double ddiscount) {
        this.ddiscount = ddiscount;
    }

    public double getDnshoulddcost() {
        return dnshoulddcost;
    }

    public void setDnshoulddcost(double dnshoulddcost) {
        this.dnshoulddcost = dnshoulddcost;
    }

    @Override
    public String toString() {
        return "ContractDetailPOJO{" +
                "subjectName='" + subjectName + '\'' +
                ", dcost=" + dcost +
                ", ddiscount=" + ddiscount +
                ", dnshoulddcost=" + dnshoulddcost +
                '}';
    }
}

