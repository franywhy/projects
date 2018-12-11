package com.izhubo.web.vo;

/**
 * 问题的图片
* @ClassName: TopicsPicVO 
* @Description: 问题的图片
* @author shihongjie
* @date 2015年5月25日 下午2:36:40 
*
 */
public class TopicsPicVO {
	
	private String _id;
	
	private int pic_seq;
	
	private String pic_url;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public int getPic_seq() {
		return pic_seq;
	}

	public void setPic_seq(int pic_seq) {
		this.pic_seq = pic_seq;
	}

	public String getPic_url() {
		return pic_url;
	}

	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

	@Override
	public String toString() {
		return "TopicsPicVO [_id=" + _id + ", pic_seq=" + pic_seq
				+ ", pic_url=" + pic_url + "]";
	}

	public TopicsPicVO(String _id, int pic_seq, String pic_url) {
		super();
		this._id = _id;
		this.pic_seq = pic_seq;
		this.pic_url = pic_url;
	}

	public TopicsPicVO() {
		super();
	}
	
	
	
}
