package io.renren.entity;

public class AppLabel {
    private Long id;

    private Long parentId;

    private String labelName;

    private Long productId;

	private Integer isCommon;

    private Integer dr;
    
    private String courseCode;
    
    private String parentName;
    
    private String smallPicUrl;
    
    private String bigPicUrl;
    
    private Integer type;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getSmallPicUrl() {
		return smallPicUrl;
	}

	public void setSmallPicUrl(String smallPicUrl) {
		this.smallPicUrl = smallPicUrl;
	}

	public String getBigPicUrl() {
		return bigPicUrl;
	}

	public void setBigPicUrl(String bigPicUrl) {
		this.bigPicUrl = bigPicUrl;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode == null ? null : courseCode.trim();
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName == null ? null : labelName.trim();
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public Integer getIsCommon() {
		return isCommon;
	}

	public void setIsCommon(Integer isCommon) {
		this.isCommon = isCommon;
	}
}