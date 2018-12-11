package io.renren.modules.job.utils;

import io.renren.modules.job.entity.CourseClassplanLivesEntity;
import org.apache.commons.lang.StringUtils;

public class BackUtils {
	
	private static final String BACK_REG = ".*http.*play-.*";
	
	/**
	 * 获取展示互动的回放ID
	 * @param backUrl
	 * @return
	 */
	public static String getBackId(String backUrl){
		String res = null;
		if (BACK_REG.matches(backUrl)) {
			String a[] = backUrl.split("-");
			res = a[1];
		}
		return res;
	}
	/**
	 * 设置回放ID
	 * @param classPlanLive
	 */
	public static void setBackId(CourseClassplanLivesEntity classPlanLive){
		if(null != classPlanLive && StringUtils.isNotBlank(classPlanLive.getBackUrl())){
			String backId = getBackId(classPlanLive.getBackUrl());
			// 回放ID
			classPlanLive.setBackId(backId);
			// 回放类型0.CC 1.展视互动
			classPlanLive.setBackType(StringUtils.isNotBlank(backId) ? 0: 1);
		}
	}
	
}
