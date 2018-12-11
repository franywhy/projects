

package com.izhubo.web.jyxt

import org.json.JSONArray;
import javax.servlet.http.HttpServletRequest
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.izhubo.rest.AppProperties
import com.izhubo.rest.common.util.JSONUtil
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import com.izhubo.rest.anno.RestWithSession;
import com.izhubo.rest.common.util.http.HttpClientUtil4_3;
import com.izhubo.web.BaseController
import org.springframework.beans.factory.annotation.Value
import com.wordnik.swagger.annotations.ApiOperation
@RestWithSession
class JyxtController extends BaseController {

	private static Logger logger = LoggerFactory
	.getLogger(JyxtController.class);
	
	@Value("#{application['hqonlineh5.domain']}")
	private String hqonlineh5_domain ="http://wap.hqjy.com/";
	private String jyxt_action="jyxt/lib/jyxt_home.ashx";
	private String jyxt_pay_url = "";
	private String jyxt_video_record_url = "";
	/**↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓  经验学堂接口   ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ */
	/**
	 * 首页商品列表
	 * @date 2016年3月28日
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "jyxtdata/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "经验学堂接口", httpMethod = "GET",  notes = "会答2.0 首页商品列表API")
	def jyxtdata(HttpServletRequest request){
		Map map = new HashMap();
		String username =getUserPhoneByUserId();
		
		String mberid = "";
		String domain = AppProperties.get("kjcityapi.domain").toString();
		
		   try {
			    String resultString = HttpClientUtil4_3.get(domain+"/api/account/getmemberid?username="+username, null);
				mberid = URLEncoder.encode(resultString);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
			}
		hqonlineh5_domain = hqonlineh5_domain.trim();
		String reqResult = HttpClientUtil4_3.get(hqonlineh5_domain+jyxt_action+"?p="+mberid,null);
		Map _jsonMap = JSONUtil.jsonToMap(reqResult);
		_jsonMap.put("requesturl",domain+"/api/course/SetCourseProgress");
		_jsonMap.put("price",680);
		_jsonMap.put("buyurl",hqonlineh5_domain+"/jyxt/lib/jyxt_payurl.ashx?type=1&name="+username);
		//_jsonMap.put("buyurl","http://wap.hqjy.com/150.html");
		_jsonMap.get("course_data").each { def item->
			def itemlist = item["item_list"];
			if(itemlist)
			{
				itemlist.each {def sitem->
				   if(sitem["video"])
				   {
                       String picurl = getCCVideoPic(sitem["video"].toString());
					   if(picurl) {
						   sitem["video_pic"] = getCCVideoPic(sitem["video"].toString());
					   }
				   }
				  	def sitemlist = sitem["item_list"];
					  sitemlist.each {def ssitem->
						  if(ssitem["video"])
						  {
							  String picurl = getCCVideoPic(ssitem["video"].toString());
							  if(picurl) {
								  ssitem["video_pic"] = getCCVideoPic(ssitem["video"].toString());
							  }

						  }
					}
				}
			}
		}
		getResultOK(_jsonMap);
	}


	
	
	


}


