package io.renren.controller;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import io.renren.common.doc.SysLog;
import io.renren.entity.CourseExamRecordDetailEntity;
import io.renren.entity.CourseExamRecordEntity;
import io.renren.rest.persistent.KGS;
import io.renren.service.CourseExamRecordDetailService;
import io.renren.service.CourseExamRecordService;
import io.renren.service.CourseUserplanDetailService;
import io.renren.service.CourseUserplanService;
import io.renren.service.CoursesService;
import io.renren.service.MallAreaService;
import io.renren.service.MallOrderService;
import io.renren.service.SysProductService;
import io.renren.service.UsersService;
import io.renren.utils.ExcelReaderJXL;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;
import net.sf.json.JSONObject;


/**
 * 报考登记
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-26 15:21:19
 */
@Controller
@RequestMapping("course/courseexamrecord")
public class CourseExamRecordController extends AbstractController {
	@Autowired
	private CourseExamRecordService courseExamRecordService;
	@Autowired
	private CourseExamRecordDetailService courseExamRecordDetailService;
	@Autowired
	private CourseUserplanService courseUserplanService;
	@Autowired
	private CourseUserplanDetailService courseUserplanDetailService;
	@Autowired
	private UsersService usersService;
	@Autowired
	private MallAreaService mallAreaService;
	@Autowired
	private CoursesService coursesService;
	@Autowired
	private MallOrderService mallOrderService;
	@Autowired
	private SysProductService sysProductService;
	@Resource
	KGS examRecordKGS;
	
	@RequestMapping("/courseexamrecord.html")
	public String list(){
		return "courseexamrecord/courseexamrecord.html";
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("courseexamrecord:list")
	public R list(String examRecordNo, Integer page, Integer limit, HttpServletRequest request){
		String schoolId = SchoolIdUtils.getSchoolId(request);
		
		Map<String, Object> map = getMapPage(request);
		
		//查询列表数据
		map.put("schoolId", schoolId);
		map.put("examRecordNo", examRecordNo);
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		List<CourseExamRecordEntity> courseExamRecordList = courseExamRecordService.queryList(map);
		int total = courseExamRecordService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(courseExamRecordList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{examRecordId}")
	@RequiresPermissions("courseexamrecord:info")
	public R info(@PathVariable("examRecordId")Long examRecordId , HttpServletRequest request){
		String schoolId = SchoolIdUtils.getSchoolId(request);
        //查询主表
		Map<String, Object> map = getMap(request);
		map.put("examRecordId", examRecordId);
		map.put("schoolId", schoolId);
		CourseExamRecordEntity courseExamRecord = courseExamRecordService.queryObject(map);
		return R.ok().put("examRecord",courseExamRecord);
        //查询子表
		/*Map<String, Object> mapDetail = getMap(request);
		mapDetail.put("examRecordId", examRecordId);
		mapDetail.put("schoolId", schoolId);
		List<Map<String, Object>> courseExamRecordDetail = courseExamRecordDetailService.queryAll(mapDetail);
		return R.ok().put("examRecord",courseExamRecord).put("examRecordDetail", courseExamRecordDetail);
		*/
	}

    /**
     * 报考子表查询
     */
    @ResponseBody
    @RequestMapping("queryExamRecordDetail")
    public R queryExamRecordDetail( HttpServletRequest request){
        String schoolId = SchoolIdUtils.getSchoolId(request);
        String userName = ServletRequestUtils.getStringParameter(request, "userName", "");
        String mobile = ServletRequestUtils.getStringParameter(request, "mobile", "");
        String examareaId = ServletRequestUtils.getStringParameter(request, "examareaId", "");
        Long examRecordId = ServletRequestUtils.getLongParameter(request, "examRecordId",0L);
        //查询子表
        Map<String, Object> mapDetail = getMapPage(request);
        mapDetail.put("examRecordId", examRecordId);
        mapDetail.put("schoolId", schoolId);
        mapDetail.put("userName", userName);
        mapDetail.put("mobile", mobile);
        mapDetail.put("examareaId", examareaId);
        List<Map<String, Object>> courseExamRecordDetail = courseExamRecordDetailService.queryAll(mapDetail);
        int total = courseExamRecordDetailService.queryTotal2(mapDetail);
        PageUtils pageUtil = new PageUtils(courseExamRecordDetail, total , request);
        return R.ok().put(pageUtil);
    }
	/**
	 * 获取系统生成的单号
	 */
	@ResponseBody
	@RequestMapping("/getNo")
	@RequiresPermissions("courseexamrecord:save")
	public R getNo(HttpServletRequest request){
		String no = "BK" + (long)examRecordKGS.nextId();
		CourseExamRecordEntity courseExamRecord = new CourseExamRecordEntity();
		courseExamRecord.setExamRecordNo(no);
		return R.ok().put(courseExamRecord);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存报考登记")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("courseexamrecord:save")
	public R save(@RequestBody CourseExamRecordEntity courseExamRecord , HttpServletRequest request){
	    //平台ID
	    courseExamRecord.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //创建用户
		courseExamRecord.setCreatePerson(getUserId());
	    //创建时间
		courseExamRecord.setCreateTime(new Date());
		//默认状态
		courseExamRecord.setDr(0);
		//默认审核状态
		courseExamRecord.setAudited(0);
		//保存
		courseExamRecordService.save(courseExamRecord);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改报考登记")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("courseexamrecord:update")
	public R update(@RequestBody CourseExamRecordEntity courseExamRecord , HttpServletRequest request){
		 //平台ID
	    courseExamRecord.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //修改用户
		courseExamRecord.setModifyPerson(getUserId());
		//修改时间
		courseExamRecord.setModifyTime(new Date());
		//修改
		courseExamRecordService.update(courseExamRecord);
		return R.ok();
	}
	
	/**
	 * 审核
	 */
	@SysLog("审核报考登记")
	@ResponseBody
	@RequestMapping("/accept")
	@RequiresPermissions("courseexamrecord:accept")
	public R accept(@RequestBody CourseExamRecordEntity courseExamRecord , HttpServletRequest request){
		 //平台ID
	    courseExamRecord.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //修改用户
		courseExamRecord.setModifyPerson(getUserId());
		//修改时间
		courseExamRecord.setModifyTime(new Date());
		//审核通过
		courseExamRecord.setAudited(1);
		//修改
		courseExamRecordService.accept(courseExamRecord);
		return R.ok();
	}
	
	/**
	 * 反审核
	 */
	@SysLog("反审核报考登记")
	@ResponseBody
	@RequestMapping("/reaccept")
	@RequiresPermissions("courseexamrecord:reaccept")
	public R reaccept(@RequestBody CourseExamRecordEntity courseExamRecord , HttpServletRequest request){
		 //平台ID
	    courseExamRecord.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //修改用户
		courseExamRecord.setModifyPerson(getUserId());
		//修改时间
		courseExamRecord.setModifyTime(new Date());
		//审核不通过
		courseExamRecord.setAudited(0);
		//修改
		courseExamRecordService.accept(courseExamRecord);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("保存报考登记")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("courseexamrecord:delete")
	public R delete(@RequestBody Long[] examRecordIds , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",examRecordIds);
		courseExamRecordService.deleteBatch(map);
		courseExamRecordDetailService.deleteDetailBatch(map);
		return R.ok();
	}
	
	/**
	 * 学员规划弹窗
	 */
	@ResponseBody
	@RequestMapping("/userplanList")
	public R userplanList(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		String productId = ServletRequestUtils.getStringParameter(request, "productId", "");
		map.put("productId", productId);
		//查询列表数据
		List<Map<String, Object>> courseUserplanList = courseUserplanService.queryList(map);
		int total = courseUserplanService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(courseUserplanList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	/**
	 * 学员规划-课程弹窗
	 */
	@ResponseBody
	@RequestMapping("/courseListByUserPlanId")
	public R courseListByUserPlanId(String userplanDetailIds, 
			Integer isRep , Integer isMatch ,
			HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		longQuery(map, request, "userPlanId");
		//考英语二
		boolean examEn2  = true;
		//专业对口
		boolean targetGrade  = true;
		if(1 == isRep){
			examEn2 = false;
		}
		if(1 == isMatch){
			targetGrade = false;
		}
		map.put("examEn2", examEn2);
		map.put("targetGrade", targetGrade);
		
		if(null != userplanDetailIds && userplanDetailIds.length() > 0){
			String[] userplanDetailIdsStr = new String[]{};
			userplanDetailIdsStr = userplanDetailIds.split(",");
			Long[] userplanDetailIdsLong = new Long[userplanDetailIdsStr.length];
			for(int i=0;i<userplanDetailIdsStr.length;i++){
				userplanDetailIdsLong[i] = Long.valueOf(userplanDetailIdsStr[i]);
			}
			map.put("userplanDetailIds", userplanDetailIdsLong);
		}
		List<Map<String , Object>> list = this.courseUserplanDetailService.courseListByUserPlanId(map);
		int total = courseUserplanDetailService.courseTotalByUserPlanId(map);
		PageUtils pageUtil = new PageUtils(list, total, request);
		return R.ok().put("page", pageUtil);
	}
	/**
	 * 报考登记EXCEL批量导入
	 * @throws Exception 
	 * */
	@SysLog("报考登记EXCEL批量导入")
	@ResponseBody
	@RequestMapping("/getExcelCourseExamRecordData")
	public R getExcelCourseExamRecordData(HttpServletRequest request) throws Exception
		{
		    String mobilePhoneNo=null;
		    Long  user_id=null;
            //报名省份
            Long  area_id=null;
            String provinceNane=null;
            //报考省份
            Long  examarea_id=null;
            String examProvinceName=null;
            String courseNo=null;
		    Long course_id=null;
		    String orderNo=null;
		    Long userplan_detail_id=null;
		    String remark=null;
		    String productName=null;
		    Long productId=null;
		    Long productIdLast=null;
			MultipartHttpServletRequest mpReq = (MultipartHttpServletRequest) request;
			MultipartFile file = mpReq.getFile("file_data");
			/*String fileName = file.getOriginalFilename();*/
			/*List<String> warningMesages=null;*/
			List<CourseExamRecordDetailEntity> detailList=new ArrayList();
			CourseExamRecordEntity courseExamRecord=new CourseExamRecordEntity();
				FileInputStream fio = (FileInputStream) file.getInputStream();
				List<String[]> dataList = ExcelReaderJXL.readExcel(fio);
				 //平台ID
			    courseExamRecord.setSchoolId(SchoolIdUtils.getSchoolId(request));
			    //创建用户
				courseExamRecord.setCreatePerson(getUserId());
				  //修改用户
				courseExamRecord.setModifyPerson(getUserId());
			    //创建时间
				courseExamRecord.setCreateTime(new Date());
				 //修改时间
				courseExamRecord.setModifyTime(new Date());
				//默认状态
				courseExamRecord.setDr(0);
				//默认审核状态
				courseExamRecord.setAudited(0);
                //自动生成单号
				String examRecordNo = "BK" + (long)examRecordKGS.nextId();
				courseExamRecord.setExamRecordNo(examRecordNo);
				String exceptMsg = "";
				String[] header=dataList.get(0);
				ArrayList<String> exceptList = new ArrayList<String>();
				if(header.length<7){
					exceptMsg="总列数不正确，请核对一下列数；";
					 exceptList.add(exceptMsg);
				}
				else if(!header[0].trim().equals("手机号")){
					exceptMsg="对应的列名不对，请核对一下是否与【手机号】匹配;";
					exceptList.add(exceptMsg);
				}
				else if(!header[1].trim().equals("报名省份")){
					exceptMsg="对应的列名不对，请核对一下是否与【省份】匹配;";
					exceptList.add(exceptMsg);
				}
				else if(!header[2].trim().equals("报考省份")){
					exceptMsg="对应的列名不对，请核对一下是否与【省份】匹配;";
					exceptList.add(exceptMsg);
				}
				else if(!header[3].trim().equals("课程编号")){
					exceptMsg="对应的列名不对，请核对一下是否与【课程编号】匹配;";
					exceptList.add(exceptMsg);
				}
				else if(!header[4].trim().equals("订单号")){
					exceptMsg="对应的列名不对，请核对一下是否与【订单号】匹配;";
					exceptList.add(exceptMsg);
				}
				else if(!header[5].trim().equals("备注")){
					exceptMsg="对应的列名不对，请核对一下是否与【备注】匹配;";
					exceptList.add(exceptMsg);
				}
				else if(!header[6].trim().equals("产品线(自考产品线、会计产品线、双师产品线)")){
					exceptMsg="对应的列名不对，请核对一下是否与【产品线(自考产品线、会计产品线、双师产品线)】匹配;";
					exceptList.add(exceptMsg);
				}
				else if(header.length>=7&&header[0].trim().equals("手机号")&&header[1].trim().equals("报名省份")&&header[2].trim().equals("报考省份")&&header[3].trim().equals("课程编号")
					&&header[4].trim().equals("订单号")&&header[5].trim().equals("备注")&&header[6].trim().equals("产品线(自考产品线、会计产品线、双师产品线)")) {
				    dataList.remove(0);
				   for (int i = 0; i < dataList.size(); i++) {
					   CourseExamRecordDetailEntity courseExamRecordDetail=new CourseExamRecordDetailEntity();
					   String[] dataArr=dataList.get(i);
					      Map<String, Object> map = getMapPage(request);
					       boolean flag = false;
					       exceptMsg="第" + new Integer(i+2).toString() + "行数据：";
					       mobilePhoneNo=dataArr[0];
						   provinceNane=dataArr[1];
						   examProvinceName=dataArr[2];
						   courseNo=dataArr[3];
						   orderNo=dataArr[4];
						   remark=dataArr[5];
						   productName=dataArr[6];
						   map.put("mobilePhoneNo", mobilePhoneNo);
						   map.put("provinceNane", provinceNane);
						   map.put("examProvinceName", examProvinceName);
						   map.put("courseNo", courseNo);
						   map.put("orderNo", orderNo);
						   map.put("remark", remark);
						   map.put("productName", productName);
						   
						   if (mobilePhoneNo == null || mobilePhoneNo.equals("")){
							   exceptMsg += "手机号不能为空";
							   exceptList.add(exceptMsg);
							   flag=true;
						   }else{
							    int count=usersService.countUserIdByMobilePhoneNo(map);
						        if(count<=0){
						             if(flag==true){
							            exceptMsg += ",手机号不存在";
                                     }else{
							            exceptMsg += "手机号不存在";
							            flag=true;
						             }
					            }else{
						        //根据手机号获取 用户的ID
						            user_id =usersService.getUserIdByMobilePhoneNo(map);
					             }
                           }
						   
						   
						   if (productName == null || productName.equals("")){
							   exceptMsg += "产品名不能为空";
							   exceptList.add(exceptMsg);
							   flag=true;
						   }else{
							    int count=sysProductService.queryTotal(map);
						        if(count<=0){
						            if(flag==true){
							            exceptMsg += ",产品名不存在";
						            }else{
							            exceptMsg += "产品名不存在";
                                        flag=true;
						            }
					            }else{
						            //根据手机号获取 用户的ID
						            productId =sysProductService.getProductIdByProductName(map);
                                    productIdLast=productId;
					            }
                           }
						   
						   if (provinceNane == null || provinceNane.equals("")){
							   if(flag==true){
							        exceptMsg += ",报名省份不能为空";
							   }else{
							        exceptMsg += "报名省份不能为空";
							        flag=true;
							   }
						   }else{
							    int count=mallAreaService.countAreaIdByprovinceNane(map);
						        if(count<=0){
						            if(flag==true){
							            exceptMsg += ",报名省份不存在";
						            }else{
							            exceptMsg += "报名省份不存在";
							            flag=true;
						            }
					            }else{
						            //根据省份名获取 省份的ID
							        area_id=mallAreaService.getAreaIdByprovinceNane(map);
                                }
				            }
						   if (examProvinceName == null || examProvinceName.equals("")){
							   if(flag==true){
							        exceptMsg += ",报考省份不能为空";
							   }else{
							        exceptMsg += "报考省份不能为空";
							        flag=true;
							   }
						   }else{
							    int count=mallAreaService.countAreaIdByExamProvinceName(map);
						        if(count<=0){
						            if(flag==true){
							            exceptMsg += ",报考省份不存在";
						            }else{
							            exceptMsg += "报考省份不存在";
							            flag=true;
						            }
					            }else{
						            //根据省份名获取 省份的ID
							        examarea_id=mallAreaService.getAreaIdByExamProvinceName(map);
                                }
				            }
						   if (courseNo == null || courseNo.equals("")){
							   if(flag==true){
							        exceptMsg += ",课程编号不能为空";
							   }else{
							        exceptMsg += "课程编号不能为空";
							        flag=true;
							   }
						   }else{
							   int count=coursesService.countCourseIdBycourseNo(map);
						            if(count<=0){
						                if(flag==true){
							                exceptMsg += ",课程编号不存在";
                                        }else{
							                exceptMsg += "课程编号不存在";
							                flag=true;
						                }
					                }else{
						                //根据课程名获取课程的ID
							            course_id=coursesService.getCourseIdBycourseNo(map);
					                 }
				            }
						   if (orderNo == null || orderNo.equals("")){
							   if(flag==true) {
							        exceptMsg += ",订单编号不能为空";
							   }else{
							        exceptMsg += "订单编号不能为空";
							        flag=true;
							   }
						   }else{
							   if(course_id!=null&&area_id!=null){
		                            //根据订单编号和课程ID获取学员规划详情ID
								    map.put("course_id", course_id);
								    map.put("area_id", area_id);
								    map.put("productId", productId);
								    int count=mallOrderService.countUserplanDetailId(map);
						            if(count<=0){
						                if(flag==true){
							                exceptMsg += ",订单号为【"+orderNo+"】在【"+provinceNane+"】的课程编号为【"+courseNo+"】并且产品线是【"+productName+"】的学员详情不存在";
						                }else{
							                exceptMsg += "订单号为【"+orderNo+"】在【"+provinceNane+"】的课程编号为【"+courseNo+"】并且产品线是【"+productName+"】的学员详情不存在";
							                flag=true;
                                        }
					                }else if(count>=2){
						                if(flag==true){
							                exceptMsg += "系统数据紊乱";
						                }else{
							                exceptMsg += ",系统数据紊乱";
							                flag=true;
						                }
                                    }else{
						                userplan_detail_id=mallOrderService.getUserplanDetailId(map);
						            }
                               }
                           }
						   if (flag){
							   exceptList.add(exceptMsg);
							   continue;
						   }
                            courseExamRecordDetail.setAreaId(area_id);
                            courseExamRecordDetail.setExamareaId(examarea_id);
                            courseExamRecordDetail.setUserId(user_id);
                            courseExamRecordDetail.setRemark(remark);
                            courseExamRecordDetail.setCourseId(course_id);
                            courseExamRecordDetail.setUserplanDetailId(userplan_detail_id);
                            detailList.add(courseExamRecordDetail);
                   }
				}
				/*return R.ok();*/
				String errMsgErr = "";
				for (int i = 0; i < exceptList.size(); i++) {
					errMsgErr += exceptList.get(i) + "<br>";
					/* System.out.println(warningMesages.get(i)); */
				}
				courseExamRecord.setDetailList(detailList);
				courseExamRecord.setProductId(productIdLast);
				if (errMsgErr.equals("")){
                    //保存
                    courseExamRecordService.save(courseExamRecord);
					return R.ok();
				}else{
					return R.ok(errMsgErr);
				}
		}
	
	/**
	 * 列表
	 * @throws IOException 
	 */
	@ResponseBody
	@RequestMapping("/exportExcelCourseExamRecordTemplate")
	public void exportExcelCourseExamRecordTemplate(HttpServletRequest request,HttpServletResponse res) throws IOException{
	    String arrStr = "0,0,0,0,手机号&0,1,0,0,报名省份&0,2,0,0,报考省份&0,3,0,0,课程编号&0,4,0,0,订单号&0,5,0,0,备注&0,6,0,0,产品线(自考产品线、会计产品线、双师产品线)";
	    /*String result=areaService.getDataByCondition(conditions);*/
		/*arrStr+=result;*/
	    String cellsStr = new String(arrStr.getBytes("GBK"), "GBK");
		String[] cells = cellsStr.split("&");
		String filename = "课程登记模板导出-" + Calendar.getInstance().getTimeInMillis() + ".xls";
		filename = new String(filename.getBytes("GBK"), "iso-8859-1");
		res.setContentType("text/html; charset=UTF-8");
		res.addHeader("Content-Disposition", (new StringBuilder())
                .append("attachment;filename=")
                .append(filename+";").toString());
		OutputStream toClient = new BufferedOutputStream(res.getOutputStream());
		ExcelReaderJXL.exportToJxlExcel(filename, "课程登记", cells, toClient);
		toClient.flush();
		toClient.close();
		/* return R.ok();*/
	}
}
