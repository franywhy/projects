package io.renren.entity;

public class MallGoodsInfoLayEntity {
	 private int id;
	 //小图
     private String thumbPath;
     //售价
     private String presentPrice; 
     //大图
     private String originPath;
     //原价
     private String originalPrice;
     
     private String name;
     //班级
     private String classtypeName;
     //专业
     private String professionName;
     //层次
     private String levelName;
     //专业ID
     private Integer professionId;
    
     
	
	public Integer getProfessionId() {
		return professionId;
	}
	public void setProfessionId(Integer professionId) {
		this.professionId = professionId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getThumbPath() {
		return thumbPath;
	}
	public void setThumbPath(String thumbPath) {
		this.thumbPath = thumbPath;
	}
	public String getPresentPrice() {
		return presentPrice;
	}
	public void setPresentPrice(String presentPrice) {
		this.presentPrice = presentPrice;
	}
	public String getOriginPath() {
		return originPath;
	}
	public void setOriginPath(String originPath) {
		this.originPath = originPath;
	}
	public String getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClasstypeName() {
		return classtypeName;
	}
	public void setClasstypeName(String classtypeName) {
		this.classtypeName = classtypeName;
	}
	public String getProfessionName() {
		return professionName;
	}
	public void setProfessionName(String professionName) {
		this.professionName = professionName;
	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
     
}
