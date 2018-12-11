package com.izhubo.rest.pushmsg.model;

//字段名称: push_action_name
//跳转的页面字典
//
//-----跳转到首页------------------（如果推送消息中不存在这两个字段，同样跳转到首页）  
//
//   push_action_name:defult
//
//-----跳转到问题列表------------------
//
//   push_action_name:mytopices
//
//-----跳转到问题列表--------具体某个问题---
//
//   push_action_name:mytopices_topic
//   define_info:问题id
//
//-----跳转到题库首页------------------
//
//   push_action_name:mytest
//
//-----跳转到题库首页------题库具体页面
//   push_action_name:mytest_other
//   define_info:题库的URL
//
//
//-----跳转到个人设置页面------------------
//
//   push_action_name:myinfo
//
//-----跳转到个人设置页面----关注
//   push_action_name:myinfo_guanzhu
//
//-----跳转到个人设置页面----收藏
//   push_action_name:myinfo_shouchang
//
//-----跳转到个人设置页面----邀请
//   push_action_name:myinfo_yaoqing
//
//-----跳转到个人设置页面----设置
//   push_action_name:myinfo_shezhi
//
//-----跳转到个人设置页面----收益
//   push_action_name:myinfo_shouyi
public enum PushMsgActionEnum {
	   defult(0),//-----跳转到首页------------------（如果推送消息中不存在这两个字段，同样跳转到首页） 
	   mytopices(1),//-----跳转到问题列表------------------
	   mytopices_topic(2),//-----跳转到问题列表--------具体某个问题---
	   mytest(3),//-----跳转到题库首页------------------
	   mytest_other(4),//-----跳转到题库首页------题库具体页面
	   myinfo(5),//-----跳转到个人设置页面------------------
	   myinfo_guanzhu(6),//-----跳转到个人设置页面----关注
	   myinfo_shouchang(7),//-----跳转到个人设置页面----收藏
	   myinfo_yaoqing(8),//-----跳转到个人设置页面----邀请
	   myinfo_shezhi(9),//-----跳转到个人设置页面----设置
	   myinfo_shouyi(10),//-----跳转到个人设置页面----收益
	   tourl(11),//跳转到某个url
	   donothing(12),//无消息行为
	   live_detail(13),//跳转到直播间
	   video(14);//跳转到微课
	   
	   
	   
	   private final int value;

	   //构造器默认也只能是private, 从而保证构造函数只能在内部使用
	   PushMsgActionEnum(int value) {
	       this.value = value;
	   }
	   
	   public int getValue() {
	       return value;
	   }
}
