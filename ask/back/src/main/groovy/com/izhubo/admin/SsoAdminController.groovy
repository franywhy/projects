package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$

import java.text.SimpleDateFormat
import java.util.regex.Pattern

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.commons.lang.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.servlet.ModelAndView

import com.izhubo.rest.anno.Rest
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.common.util.MsgDigestUtil
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.QueryBuilder

/**
 * 外皮系统接口
 * @author Administrator
 *
 */
@Rest
class SsoAdminController extends BaseController {
	
	private static final String SSO_USERID_KEY = "userId";
	private static final String SSO_USERNAME_KEY = "userName";
	private static final String SSO_GUID_KEY = "guid";
	
	/** sso密钥 */
	@Value("#{application['sso.key']}")
	private String SSO_KEY_STRING = "";
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");
	
	public DBCollection admins(){return adminMongo.getCollection("admins");}
	
	/*
		GetUsers(秘钥,用户账号,用户名称)
		秘钥：GUID 用于防治其他用户攻击。
		接口名称:
			 getUsers
		参数:
			 String ssoKey 密钥
		
		返回值(json):
		[
		  code : 1 ,
		  data:[
							{ "userId" : "id1" , "userName" : "张三" , "guid" : "" },
							{ "userId" : "id2" , "userName" : "李四" , "guid" : "" },
							{ "userId" : "id3" , "userName" : "王五" , "guid" : "xxxxx" },
							{ "userId" : "id4" , "userName" : "赵四" , "guid" : "xxxxx" }
				]
		]
	*/
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	def getUsers(HttpServletRequest request,HttpServletResponse response){
//		String ssoKey = request["ssoKey"];
		
		if(checkSsoKey(request)){
			//翻页查询
//			int size = ServletRequestUtils.getIntParameter(request, "size", 20);
//			int page = ServletRequestUtils.getIntParameter(request, "page", 1);
			
			String nick_name = request["nick_name"];
			def query = QueryBuilder.start();
//			query.and("is_open").notEquals(0);
			if(StringUtils.isNotBlank(nick_name)){
//				query.append("nick_name", page)
				Pattern pattern = Pattern.compile("^.*" + nick_name + ".*\$", Pattern.CASE_INSENSITIVE);
				query.and("nick_name").regex(pattern);
			}
			
			return Crud.list(request,admins(),query.get(),$$("_id" : 1 , "nick_name" : 1 , "ssoguid" : "1"),MongoKey.SJ_DESC){List<BasicDBObject> data->
				for(BasicDBObject obj: data){
					obj[SSO_USERID_KEY] = obj["_id"];
					obj[SSO_USERNAME_KEY] = obj["nick_name"];
					obj[SSO_GUID_KEY] = obj["ssoguid"];
					
					obj.remove("_id");
					obj.remove("nick_name");
					obj.remove("ssoguid");
				}
			}
			
		}
		return getKeyErr();
	}
	
	/*
	upLoadSSOKey(秘钥,用户账号,GUID)
	秘钥：GUID 用于防治其他用户攻击。
	方法名:
				  upLoadSSOKey
	参数:
				  String userId   用户账号
				  String guid         GUID
				  String ssoKey 密钥
	返回值(json):
				[code : 1 ]
	
	
	备注: code 等于1时代表操作成功 ;
		  code为2000时代表参数校验失败 ;
		  code为3000时验签失败 ;
		  code为其他时请指明错误.
		  msg为错误原因,code为1时可为空;
	例子:["code" : 1 , data : "参数校验失败!", msg : "参数校验失败!"]
	*/
	
	def upLoadSSOKey(HttpServletRequest request,HttpServletResponse response){
		if(checkSsoKey(request)){
			String ssoGuid = request["guid"];
//			String userId = request["userId"];
			int userId = ServletRequestUtils.getIntParameter(request, "userId", 0)
//			String userId = request["userId"];
			if(StringUtils.isNotBlank(ssoGuid) && userId > 0){
				//去除原来对应的GUID
				admins().update(
						$$("_id" : $$('$ne' : userId) , "ssoguid" : ssoGuid) , 
						$$('$set' : $$("ssoguid" : null))
					);
				//更新新的guid
				admins().update(
						$$("_id" : userId ) , 
						$$('$set' : $$("ssoguid" : ssoGuid))
						);
				
				return OK();
			}
			return getParamsErr();//参数校验失败
		}
		return getKeyErr();//验签失败
	}
	
	/*
	登录并跳转到系统ssoLogin
	ssoLogin(String sso_date,String guid, String sign_cipher ,long validity )
		接口名:ssoLogin
		访问方式:post
		参数:
			日期           String    sso_date     yyyyMMddHHmmss
			guid           String    guid        guid
			密文	       String	 sign_cipher md5M
			有效期-秒      long      validity
			
		备注:
			sign_cipher 加密说明
				sso_date + guid + validity  + KEY 生成md5
			其中key="6512bd43d9caa6e02c990b0a82652dca"
		算法：
			1.sso_date + guid + validity+KEY  算出md5值
			2.验证该MD5值=sign_cipher
			3.系统当前时间-sso_date<validity
*/
			
	def ssoLogin(HttpServletRequest request,HttpServletResponse response){
		String loginUrl = "/index.html";
		//日期
		String sso_date = request["sso_date"];
//		//guid
		String guid = request["guid"];
		//密文
		String sign_cipher = request["sign_cipher"];
		//validity
		long validity = ServletRequestUtils.getLongParameter(request, "validity", 0);
		//校验key值是否正确
		// && sso_date + guid + validity+KEY  算出md5值
		if(checkSsoKey(request) && checkSignCipher(sso_date, guid, validity, sign_cipher)){
			//校验通过-查询数据库-跳转到主页
			if(loginByGuid(guid, request)){
				loginUrl = "/main.html";
			}
		}
		return new ModelAndView("redirect:" + loginUrl);
	}
	
	/**
	 * 通过Guid 登录
	 * @param ssoguid
	 * @param request
	 * @return
	 */
	def loginByGuid(String ssoguid , HttpServletRequest request){
		
				if(StringUtils.isBlank(ssoguid)){
					return false;
				}
				
				def user = adminMongo.getCollection("admins").findOne(new BasicDBObject("ssoguid" : ssoguid))
				if (null == user){
					return false;
				}
		
				Map menus = user.get("menus") as Map;
				if (menus == null || menus.isEmpty()){
					return false;
				}
		
				Map<String,String> sMap = new HashMap()
				sMap.put(_id,user.get(_id) as String)
				sMap.put("nick_name",user.get("nick_name") as String)
				sMap.put("name",user.get("name") as String)
				sMap.put("company_id",user.get("company_id") as String);
				request.getSession().setAttribute("user",sMap)
				request.getSession().setAttribute("menus",new HashMap(menus))
				Map modules = user.get("modules") as Map
				if (modules != null){
					request.getSession().setAttribute("modules",new HashMap(modules))
				}
		
				return true;
			}
		
	
	
	
	/**
	 * 校验KEY是否合法
	 * @param request
	 * @return
	 */
	private boolean checkSsoKey(HttpServletRequest request){
		String ssoKey = request["ssoKey"];
		if(StringUtils.isNotBlank(ssoKey) && SSO_KEY_STRING.equals(ssoKey)){
			return true;
		}
		return false;
//		return true;
	}
	
	/**
	 * 是否存在GUID
	 * @param guid
	 * @return
	 */
	private boolean isGuid(String guid){
		return StringUtils.isNotBlank(guid);
	}
	
	//sso_date + guid + validity+KEY  算出md5值
	private boolean checkSignCipher(String sso_date,String guid ,long validity , String sign_cipher){
		if(StringUtils.isNotBlank(sso_date) && StringUtils.isNotBlank(guid) && StringUtils.isNotBlank(sign_cipher) && validity > 0){
			String str = sso_date + guid + validity + this.SSO_KEY_STRING;
			String signValue =  MsgDigestUtil.MD5.digest2HEX(str);
			//加密匹配
			if(signValue.equals(sign_cipher) && checkSsoDate(sso_date , validity)){
				return true;
			}
		}
		return false;
	}
	/**
	 * 时间校验
	 * @param sso_date
	 * @param validity
	 * @return
	 */
	private boolean checkSsoDate(String sso_date , long validity){
		Long ssoDate = SDF.parse(sso_date).getTime();
		Long current = System.currentTimeMillis();
		validity = validity * 1000;
		boolean res = (current - ssoDate) < validity;
		return res;
//		return (current - ssoDate) < validity;
	}
}
