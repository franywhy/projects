package com.hq.adaptive.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import io.renren.entity.SysProductEntity;
import io.renren.service.SysProductService;
import io.renren.utils.EncryptionUtils;
import io.renren.utils.HttpUtils;
import io.renren.utils.JSONUtil;
import io.renren.utils.SpringContextUtils;
/**
 * 保利视频
 * @author linchaokai
 * @email linchaokai@hengqijy.com
 * @date 2017年12月29日上午11:35:18
 */
public class PolyvUtils {
	/**
	 * 获取单个视频信息
	 * @param productId 产品线pk
	 * @param vid 视频vid
	 * @return
	 */
	public static Map<String,Object> getVideoObject(Long productId,String vid) {
		SysProductService sysProductService = (SysProductService) SpringContextUtils
				.getBean("sysProductService");
		
		Map<String, Object> productSelectMap = new HashMap<>();
		//产品线获取保利威视参数
		productSelectMap.put("productId", productId);
		SysProductEntity productEntity = sysProductService.queryObject(productSelectMap);
		if(null != productEntity){
			//保利威视参数
			String userid = productEntity.getPolyvuserid();
			String secureKey = productEntity.getPolyvsecretkey();
			Long ptime = new Date().getTime();
			String sign = EncryptionUtils.shaHex("ptime=" + ptime + "&userid=" + userid + "&vid=" + vid + secureKey).toUpperCase();
			String url = "http://api.polyv.net/v2/video/"+userid+"/get-video-msg";
			String param = "vid=" + vid + "&ptime=" + ptime + "&userid=" + userid + "&sign=" + sign;
			String result = HttpUtils.sendGet(url, param);
			if(StringUtils.isNotBlank(result)){
				Map<String,Object> resultMap = JSONUtil.jsonToMap(result);
				//获得结果code
				Integer code = (Integer) resultMap.get("code");
				if(code == 200){
					ArrayList<Map<String, Object>> dataList = (ArrayList<Map<String, Object>>) resultMap.get("data");
					return dataList.get(0);
				}
				return null;
			}
		}
		return null;
	}

}
