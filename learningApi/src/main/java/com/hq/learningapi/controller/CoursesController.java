package com.hq.learningapi.controller;

import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.learningapi.service.CoursesService;
import com.hq.learningapi.service.StudentCourseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Set;

/**
 * @author hq
 */
@Controller
@RequestMapping("/api")
public class CoursesController extends AbstractRestController {
	
	@Autowired
	private CoursesService coursesService;

	@Autowired
	private StudentCourseService studentCourseService;

	/**
	 * 根据 userId,businessId 获取学员报读课程
	 * @param userId
	 * @param businessId
	 * @return
	 */
	@RequestMapping(value = "/getCourseNoList", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> getCourseNoList(Long userId, String businessId) {

		if(null == userId) {
			return this.error("参数有误:userId");
		}
		if(StringUtils.isBlank(businessId)) {
			return this.error("参数有误:businessId");
		}
		Set<String> result = null;
		if("kuaiji".equals(businessId)) {
			//根据 nc_user_id or 蓝鲸 userId  查关系表（解决NC修改手机号，新创建蓝鲸用户问题）
			result = studentCourseService.queryCourseNoList(userId, businessId);
		} else if("zikao".equals(businessId)) {
			result = coursesService.queryCourseNoList(userId, businessId);
		}
		if(null != result){
			return this.success(result);
		}else{
			return this.fail("内部错误",null);
		}
	}
}
