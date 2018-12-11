package io.renren.mongo.sync.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="commoditys")
public class CommodityEntity {
    //主键
    private Integer _id;
    //商品名称
    private String name;
    //班型
    private String page_name;
    //大图
    private String photo;
    //小图
    private String thumbnail;
    //售价
    private double price;
    //原价
    private double old_price;
    //适用对象
    private String reader;
    //学习周期
    private String learntime;
    //上课方式
    private String classmode;
    //dr
    private Integer dr;
    //专业
    private String type;
    //层次
    private Integer level_type;
    //nc_id
    private String nc_id;
    //课程
	private List<String> course_time_list;
	
	public Integer get_id() {
		return _id;
	}
	public void set_id(Integer _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPage_name() {
		return page_name;
	}
	public void setPage_name(String page_name) {
		this.page_name = page_name;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getOld_price() {
		return old_price;
	}
	public void setOld_price(double old_price) {
		this.old_price = old_price;
	}
	public String getReader() {
		return reader;
	}
	public void setReader(String reader) {
		this.reader = reader;
	}
	public String getLearntime() {
		return learntime;
	}
	public void setLearntime(String learntime) {
		this.learntime = learntime;
	}
	public String getClassmode() {
		return classmode;
	}
	public void setClassmode(String classmode) {
		this.classmode = classmode;
	}
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getLevel_type() {
		return level_type;
	}
	public void setLevel_type(Integer level_type) {
		this.level_type = level_type;
	}
	public String getNc_id() {
		return nc_id;
	}
	public void setNc_id(String nc_id) {
		this.nc_id = nc_id;
	}
	public List<String> getCourse_time_list() {
		return course_time_list;
	}
	public void setCourse_time_list(List<String> course_time_list) {
		this.course_time_list = course_time_list;
	}
	@Override
	public String toString() {
		return "CommodityEntity [_id=" + _id + ", name=" + name + ", page_name=" + page_name + ", photo=" + photo + ", thumbnail=" + thumbnail
				+ ", price=" + price + ", old_price=" + old_price + ", reader=" + reader + ", learntime=" + learntime + ", classmode=" + classmode
				+ ", dr=" + dr + ", type=" + type + ", level_type=" + level_type + ", nc_id=" + nc_id + ", course_time_list=" + course_time_list
				+ "]";
	}
	
}
