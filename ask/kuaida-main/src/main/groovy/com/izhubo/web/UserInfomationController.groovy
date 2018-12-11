package com.izhubo.web

import static com.izhubo.rest.common.util.WebUtils.$$

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.hibernate.SessionFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import com.izhubo.model.DiscountIsUser
import com.izhubo.web.api.Web
import com.mongodb.DBCollection
import com.wordnik.swagger.annotations.ApiOperation


/**
 * 用户信息
* @ClassName: UserController 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author shihongjie
* @date 2016年3月16日 上午9:47:16 
*
 */
@Controller
@RequestMapping("/userInfo")
class UserInfomationController extends BaseController {
	
	
	@Resource
	private SessionFactory sessionFactory;
	
	/** 推送消息 */
	public DBCollection message_main() {
		return mainMongo.getCollection("message_main");
	}
	
	/** 消息接收用户 */
	public DBCollection message_user() {
		return mainMongo.getCollection("message_user");
	}
	
	/** 优惠券领取学员 */
	public DBCollection discount_user() {
		return mainMongo.getCollection("discount_user");
	}
	
	/** 优惠码 */
	public DBCollection discount() {
		return mainMongo.getCollection("discount");
	}
	
	/**报名表 */
	public DBCollection signs() {
		return mainMongo.getCollection("signs");
	}
	
	
	/**
	 * 消息列表
	 * @Description: 消息列表
	 * @date 2016年3月16日 上午10:41:56 
	 */
	def message_list(HttpServletRequest request){
		
	}
	
	
   
   
   
	
	/**
	 * 基本资料
	 * @Description: 基本资料 
	 * @date 2016年3月16日 上午10:50:06 
	 */
	@ResponseBody
	@RequestMapping(value = "user_info", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "基本资料", httpMethod = "POST",  notes = "基本资料")
//	@ApiOperation(value = "基本资料", httpMethod = "POST", response = HomeSearchVO.class, notes = "基本资料")
	def user_info(HttpServletRequest request){
		Integer user_id = Web.getCurrentUserId();
		//TODO 积分 
		//未使用优惠券数量
		def discount_count = discount_user().count($$("user_id" : user_id , "is_use" : DiscountIsUser.未使用.ordinal()));
		//TODO name vip有效期 存入session
		//vip信息
		def user = users().findOne($$("_id" : user_id) , $$("vip_flag" : 1 , "vip_validity" : 1));
		
		Map data = new HashMap();
		//是否是vip
		data["vip_flag"] = user["vip_flag"];
		//vip有效期
		data["vip_validity"] = user["vip_validity"];
		//优惠券数量
		data["discount_count"] = user["discount_count"];
		
		println data;
		
		return getResultOK(data);
	}
	
	/**
	 * 修改用户头像
	 * @Description: 修改用户头像 
	 * @date 2016年3月16日 上午10:50:49 
	 */
	def update_user_pic(
		String pic,
		HttpServletRequest request){
		
		users().update($$("_id" : Web.currentUserId()), $$("$set" : $$("pic" : pic)));
		return getResultOK();
	}
	
	
	
}
