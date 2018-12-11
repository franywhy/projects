
package com.izhubo.web.vo;

import com.wordnik.swagger.annotations.ApiModelProperty;


import com.izhubo.web.vo.MyMessageListVO.MyMessageListVOData;
import com.wordnik.swagger.annotations.ApiModelProperty;




public class MyMessageListVO  extends BasePageResultVO<MyMessageListVOData>{
	
	
	class MyMessageListVOData {
		
		
		class MsgAction
		{
			@ApiModelProperty(value = "-----跳转到首页------------------（如果推送消息中不存在这两个字段，同样跳转到首页")
			private String   defult;//-----跳转到首页------------------（如果推送消息中不存在这两个字段，同样跳转到首页） 
			@ApiModelProperty(value = "跳转到问题列表")
			private String   mytopices;//-----跳转到问题列表------------------
			@ApiModelProperty(value = "跳转到问题列表--------具体某个问题")
			private String   mytopices_topic;//-----跳转到问题列表--------具体某个问题---
			@ApiModelProperty(value = "跳转到题库首页")
			private String   mytest;//-----跳转到题库首页------------------
			@ApiModelProperty(value = "跳转到题库首页------题库具体页面")
			private String   mytest_other;//-----跳转到题库首页------题库具体页面
			//@ApiModelProperty(value = "跳转到个人设置页面")
			private String   myinfo;//-----跳转到个人设置页面------------------
			@ApiModelProperty(value = "跳转到个人设置页面----关注")
			private String   myinfo_guanzhu;//-----跳转到个人设置页面----关注
			@ApiModelProperty(value = "跳转到个人设置页面----收藏")
			private String   myinfo_shouchang;//-----跳转到个人设置页面----收藏
			@ApiModelProperty(value = "跳转到个人设置页面----邀请")
			private String   myinfo_yaoqing;//-----跳转到个人设置页面----邀请
			@ApiModelProperty(value = "跳转到个人设置页面----设置")
			private String   myinfo_shezhi;//-----跳转到个人设置页面----设置
			@ApiModelProperty(value = "跳转到个人设置页面----收益")
			private String   myinfo_shouyi;//-----跳转到个人设置页面----收益
			@ApiModelProperty(value = "跳转到某个url")
			private String   tourl;//跳转到某个url
			@ApiModelProperty(value = "无消息行为")
			private String   donothing;//无消息行为
			public String getDefult() {
				return defult;
			}
			public void setDefult(String defult) {
				this.defult = defult;
			}
			public String getMytopices() {
				return mytopices;
			}
			public void setMytopices(String mytopices) {
				this.mytopices = mytopices;
			}
			public String getMytopices_topic() {
				return mytopices_topic;
			}
			public void setMytopices_topic(String mytopices_topic) {
				this.mytopices_topic = mytopices_topic;
			}
			public String getMytest() {
				return mytest;
			}
			public void setMytest(String mytest) {
				this.mytest = mytest;
			}
			public String getMytest_other() {
				return mytest_other;
			}
			public void setMytest_other(String mytest_other) {
				this.mytest_other = mytest_other;
			}
			public String getMyinfo() {
				return myinfo;
			}
			public void setMyinfo(String myinfo) {
				this.myinfo = myinfo;
			}
			public String getMyinfo_guanzhu() {
				return myinfo_guanzhu;
			}
			public void setMyinfo_guanzhu(String myinfo_guanzhu) {
				this.myinfo_guanzhu = myinfo_guanzhu;
			}
			public String getMyinfo_shouchang() {
				return myinfo_shouchang;
			}
			public void setMyinfo_shouchang(String myinfo_shouchang) {
				this.myinfo_shouchang = myinfo_shouchang;
			}
			public String getMyinfo_yaoqing() {
				return myinfo_yaoqing;
			}
			public void setMyinfo_yaoqing(String myinfo_yaoqing) {
				this.myinfo_yaoqing = myinfo_yaoqing;
			}
			public String getMyinfo_shezhi() {
				return myinfo_shezhi;
			}
			public void setMyinfo_shezhi(String myinfo_shezhi) {
				this.myinfo_shezhi = myinfo_shezhi;
			}
			public String getMyinfo_shouyi() {
				return myinfo_shouyi;
			}
			public void setMyinfo_shouyi(String myinfo_shouyi) {
				this.myinfo_shouyi = myinfo_shouyi;
			}
			public String getTourl() {
				return tourl;
			}
			public void setTourl(String tourl) {
				this.tourl = tourl;
			}
			public String getDonothing() {
				return donothing;
			}
			public void setDonothing(String donothing) {
				this.donothing = donothing;
			}
			   
		}

		@ApiModelProperty(value = "商品图片地址")
		private int _id;
		
		@ApiModelProperty(value = "消息内容")
		private String content;
		
		@ApiModelProperty(value = "消息来源")
		private String from_type;
		
		@ApiModelProperty(value = "消息时间")
		private int timestamp;
		
		@ApiModelProperty(value = "消息标题")
		private String title;
		
		@ApiModelProperty(value = "消息类型  0：系统消息   1：个人肖像")
		private int type;
		
		@ApiModelProperty(value = "用户id")
		private int user_id;
		
		@ApiModelProperty(value = "消息行为，这个消息行为，主要是APP客户端点击打开操作的时候，进行的一些操作，如果没有值，或者为 donothing 代表没有跳转行为")
		private String msg_action;
		
		@ApiModelProperty(value = "消息行为附属字段，如果跳转的是具体某个问题，则里面是问题id")
		private String define_info;
		
		
		@ApiModelProperty(value = "跳转按钮的文字，如  去评价  去查看 等等")
		private String msg_button_text;

		@ApiModelProperty(value = "如果有跳转行为，则进行跳转")
		private String url;
		
		@ApiModelProperty(value = "是否打开  0:未打开 1:打开")
		private String is_open;
		
		@ApiModelProperty(value = "消息行为详细说明")
		private MsgAction msg_action_detail;

		public void set_id(int _id){
		this._id = _id;
		}
		public int get_id(){
		return this._id;
		}
	
		public void setContent(String content){
		this.content = content;
		}
		public String getContent(){
		return this.content;
		}
		public void setFrom_type(String from_type){
		this.from_type = from_type;
		}
		public String getFrom_type(){
		return this.from_type;
		}
		public void setTimestamp(int timestamp){
		this.timestamp = timestamp;
		}
		public int getTimestamp(){
		return this.timestamp;
		}
		public void setTitle(String title){
		this.title = title;
		}
		public String getTitle(){
		return this.title;
		}
		public void setType(int type){
		this.type = type;
		}
		public int getType(){
		return this.type;
		}
		public void setUser_id(int user_id){
		this.user_id = user_id;
		}
		public int getUser_id(){
		return this.user_id;
		}
		public void setMsg_action(String msg_action){
		this.msg_action = msg_action;
		}
		public String getMsg_action(){
		return this.msg_action;
		}
		public void setUrl(String url){
		this.url = url;
		}
		public String getUrl(){
		return this.url;
		}
		public String getIs_open() {
			return is_open;
		}
		public void setIs_open(String is_open) {
			this.is_open = is_open;
		}
		public String getMsg_button_text() {
			return msg_button_text;
		}
		public void setMsg_button_text(String msg_button_text) {
			this.msg_button_text = msg_button_text;
		}
		public MsgAction getMsg_action_detail() {
			return msg_action_detail;
		}
		public void setMsg_action_detail(MsgAction msg_action_detail) {
			this.msg_action_detail = msg_action_detail;
		}
		public String getDefine_info() {
			return define_info;
		}
		public void setDefine_info(String define_info) {
			this.define_info = define_info;
		}

	
		

	}
}
