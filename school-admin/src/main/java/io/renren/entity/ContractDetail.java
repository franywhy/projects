package io.renren.entity;

public class ContractDetail {
    private long id;//
    private long contractRecordId;//主表主键

    private String subjectName;//收支项目名称
    private int subjectId;//收支项目PK

    private String bid;//报名表BId
    private double dcost;//标准费用
    private double ddiscount;//优惠金额
    private double dnshoulddcost;//应收金额
    private int dr;//是否删除,0为不删除, 1 为删除

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getContractRecordId() {
        return contractRecordId;
    }

    public void setContractRecordId(long contractRecordId) {
        this.contractRecordId = contractRecordId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
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

    public int getDr() {
        return dr;
    }

    public void setDr(int dr) {
        this.dr = dr;
    }


}
