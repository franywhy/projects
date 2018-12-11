package io.renren.mongo.sync.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="live_room")
public class LiveRoomEntity {
	 //直播间pk
     private String _id;
     //直播间名称
     private String live_name;
     //直播间id
     private String live_id;
     //直播间备注
     private String live_info_text;
     private Integer dr;
     
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getLive_name() {
		return live_name;
	}
	public void setLive_name(String live_name) {
		this.live_name = live_name;
	}
	public String getLive_id() {
		return live_id;
	}
	public void setLive_id(String live_id) {
		this.live_id = live_id;
	}
	public String getLive_info_text() {
		return live_info_text;
	}
	public void setLive_info_text(String live_info_text) {
		this.live_info_text = live_info_text;
	}
     
     
}
