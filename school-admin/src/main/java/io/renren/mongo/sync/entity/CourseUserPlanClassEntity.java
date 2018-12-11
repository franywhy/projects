package io.renren.mongo.sync.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="classplan_user")
public class CourseUserPlanClassEntity {
	@Override
	public String toString() {
		return "CourseUserPlanClassEntity [_id=" + _id + ", user_id=" + user_id + ", classplan_id=" + classplan_id + ", order_id=" + order_id
				+ ", order_no=" + order_no + ", create_user_time=" + create_user_time + "]";
	}
	//自增
    private String _id;
    //用户ID
    private String user_id;
    //排课计划PK
    private String classplan_id;
    //订单id
    private String order_id;
    //订单编号
    private String order_no;
    //创建时间
    private String create_user_time;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getClassplan_id() {
		return classplan_id;
	}
	public void setClassplan_id(String classplan_id) {
		this.classplan_id = classplan_id;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getCreate_user_time() {
		return create_user_time;
	}
	public void setCreate_user_time(String create_user_time) {
		this.create_user_time = create_user_time;
	}
    
}
