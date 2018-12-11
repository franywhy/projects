package com.izhubo.web.vo;
/**
 * 标签
* @ClassName: TipVO 
* @Description: 标签 
* @author shihongjie
* @date 2015年5月25日 下午2:46:39 
*
 */
public class TipVO {
	
	private int _id;
	
	private String tip_name;


	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getTip_name() {
		return tip_name;
	}

	public void setTip_name(String tip_name) {
		this.tip_name = tip_name;
	}

	@Override
	public String toString() {
		return "TipVO [_id=" + _id + ", tip_name=" + tip_name + "]";
	}
	
	
}
