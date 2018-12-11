package io.renren.pojo.manage;

/**
 * Created by Administrator on 2018/8/27 0027.
 */
public class TipBonusRelationPOJO {

    private Integer id;

    private Integer min;

    private Integer max;

    private Integer veryMin;

    private Integer veryMax;

    private Integer tid;

    private String tidName;

    private Long createTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getVeryMin() {
        return veryMin;
    }

    public void setVeryMin(Integer veryMin) {
        this.veryMin = veryMin;
    }

    public Integer getVeryMax() {
        return veryMax;
    }

    public void setVeryMax(Integer veryMax) {
        this.veryMax = veryMax;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public String getTidName() {
        return tidName;
    }

    public void setTidName(String tidName) {
        this.tidName = tidName;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
