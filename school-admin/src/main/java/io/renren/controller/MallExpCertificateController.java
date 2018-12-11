package io.renren.controller;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.stereotype.Controller;
import io.renren.common.doc.SysLog;
import io.renren.entity.MallExpCertificateEntity;
import io.renren.entity.SysDeptEntity;
import io.renren.service.MallExpCertificateService;
import io.renren.service.SysDeptService;
import io.renren.service.UsersService;
import io.renren.utils.DateUtils;
import io.renren.utils.ExcelReaderJXL;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-11-04 10:20:59
 */
@Controller
@RequestMapping("/mall/mallexpcertificate")
public class MallExpCertificateController extends AbstractController {
	@Autowired
	private MallExpCertificateService mallExpCertificateService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private SysDeptService sysDeptService;

	@RequestMapping("/mallexpcertificate.html")
	public String list() {
		return "mallexpcertificate/mallexpcertificate.html";
	}

	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("mallexpcertificate:list")
	public R list(HttpServletRequest request) {
		// Map<String, Object> map = new HashMap<>();
		// map.put("offset", (page - 1) * limit);
		// map.put("limit", limit);
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "name");
		stringQuery(map, request, "course");
		stringQuery(map, request, "card");
		intQuery(map, request, "sendStatus");
		intQuery(map, request, "school");
		stringQuery(map,request,"schoolIdList");
        String schoolIdList = (String) map.get("schoolIdList");
        if (schoolIdList!= null && !"".equals(schoolIdList.trim())){
            String[] split = schoolIdList.split(",");
            map.put("schoolIdList", Arrays.asList(split));
        }
        intQuery(map, request, "type");
		// 查询列表数据
		List<MallExpCertificateEntity> mallExpCertificateList = mallExpCertificateService.queryList(map);
		int total = mallExpCertificateService.queryTotal(map);

		// PageUtils pageUtil = new PageUtils(mallExpCertificateList, total,
		// limit, page);
		PageUtils pageUtil = new PageUtils(mallExpCertificateList, total, request);
		return R.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{id}")
	@RequiresPermissions("mallexpcertificate:info")
	public R info(@PathVariable("id") Integer id) {
		MallExpCertificateEntity mallExpCertificate = mallExpCertificateService.queryObject(id);

		return R.ok().put("mallExpCertificate", mallExpCertificate);
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("mallexpcertificate:save")
	public R save(@RequestBody MallExpCertificateEntity mallExpCertificate, HttpServletRequest request) {

		if (StringUtils.isBlank(mallExpCertificate.getName())) {
			return R.error("姓名必填！！！");

		}
		if (StringUtils.isBlank(mallExpCertificate.getCard())) {
			return R.error("身份证必填！！！");

		}
		
		if (StringUtils.isNotBlank(mallExpCertificate.getMobile()) && !isMobile(mallExpCertificate.getMobile())) {			
			return R.error("手机号的格式不正确（必须为11位的数字）！！！");
		}
						
		if (null == mallExpCertificate.getReadDate()
				|| StringUtils.isBlank(mallExpCertificate.getReadDate().toString())) {
			return R.error("报读日期必填！！！");

		}
		if (null == mallExpCertificate.getEndDate()
				|| StringUtils.isBlank(mallExpCertificate.getEndDate().toString())) {
			return R.error("结课日期必填！！！");

		}
		if (StringUtils.isBlank(mallExpCertificate.getCourse())) {
			return R.error("报读课程必填！！！");

		}
		/*if (null == mallExpCertificate.getExp() || mallExpCertificate.getExp() < 0) {
			return R.error("经验等级必填！！！");

		}*/
		
		if (StringUtils.isNotBlank(mallExpCertificate.getCertNo()) && StringUtils.length(mallExpCertificate.getCertNo()) > 20) {			
			return R.error("证书编号的长度要小于18！！！");
		}
		
		
		if (null == mallExpCertificate.getSendDate()
				|| StringUtils.isBlank(mallExpCertificate.getSendDate().toString())) {
			return R.error("颁发日期必填！！！");

		}
		/*
		if (null == mallExpCertificate.getSchool() || mallExpCertificate.getSchool() < 0) {
			return R.error("校区必填！！！");

		}*/
		if (null == mallExpCertificate.getSendStatus() || mallExpCertificate.getSendStatus() < 0) {
			return R.error("发放状态必填！！！");

		}
		/*if (StringUtils.isBlank(mallExpCertificate.getCourseRemark())) {
			return R.error("主要课程必填！！！");

		}*/
		if (null == mallExpCertificate.getType() || mallExpCertificate.getType() < 0) {
			return R.error("证书类型必填！！！");

		}

		if (StringUtils.length(mallExpCertificate.getName()) > 20) {
			return R.error("姓名的长度要小于20！！！");

		}
		if (!isCard(mallExpCertificate.getCard())) {
			return R.error("身份证的格式不正确（长度必须为16位或18位）！！！");

		}
				

		if (StringUtils.length(mallExpCertificate.getCourse()) > 100) {
			return R.error("报读课程的长度要小于100！！！");

		}

		//
		Map map = new HashMap();
		map.put("mobile", mallExpCertificate.getMobile());
		boolean isExists = this.usersService.checkMobile(map);
		if (!isExists) {
			//学员不存在的情况下，直接保存
			// 默认状态
			mallExpCertificate.setStatus(1);
			// 平台ID
			mallExpCertificate.setSchoolId(SchoolIdUtils.getSchoolId(request));
			// 创建用户
			mallExpCertificate.setCreatePerson(getUserId());
			// 创建时间
			mallExpCertificate.setCreationTime(new Date());
			mallExpCertificateService.save(mallExpCertificate);
			return R.ok();
		}
		// 默认状态
		mallExpCertificate.setStatus(1);
		// 平台ID
		mallExpCertificate.setSchoolId(SchoolIdUtils.getSchoolId(request));
		// 创建用户
		mallExpCertificate.setCreatePerson(getUserId());
		// 创建时间
		mallExpCertificate.setCreationTime(new Date());
		// 保存
		mallExpCertificateService.save(mallExpCertificate);
		return R.ok();
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("mallexpcertificate:update")
	public R update(@RequestBody MallExpCertificateEntity mallExpCertificate, HttpServletRequest request) {

		if (StringUtils.isBlank(mallExpCertificate.getName())) {
			return R.error("姓名必填！！！");

		}
		if (StringUtils.isBlank(mallExpCertificate.getCard())) {
			return R.error("身份证必填！！！");

		}
		if (StringUtils.isNotBlank(mallExpCertificate.getMobile()) && !isMobile(mallExpCertificate.getMobile())) {			
			return R.error("手机号的格式不正确（必须为11位的数字）！！！");
		}
		
		if (null == mallExpCertificate.getReadDate()
				|| StringUtils.isBlank(mallExpCertificate.getReadDate().toString())) {
			return R.error("报读日期必填！！！");

		}
		if (null == mallExpCertificate.getEndDate()
				|| StringUtils.isBlank(mallExpCertificate.getEndDate().toString())) {
			return R.error("结课日期必填！！！");

		}
		if (StringUtils.isBlank(mallExpCertificate.getCourse())) {
			return R.error("报读课程必填！！！");

		}
		/*if (null == mallExpCertificate.getExp() || mallExpCertificate.getExp() < 0) {
			return R.error("经验等级必填！！！");

		}*/
		
		if (StringUtils.isNotBlank(mallExpCertificate.getCertNo()) && StringUtils.length(mallExpCertificate.getCertNo()) > 20) {			
			return R.error("证书编号的长度要小于18！！！");
		}
		
		if (null == mallExpCertificate.getSendDate()
				|| StringUtils.isBlank(mallExpCertificate.getSendDate().toString())) {
			return R.error("颁发日期必填！！！");
		}
		/*
		if (null == mallExpCertificate.getSchool() || mallExpCertificate.getSchool() < 0) {
			return R.error("校区必填！！！");

		}*/
		if (null == mallExpCertificate.getSendStatus() || mallExpCertificate.getSendStatus() < 0) {
			return R.error("发放状态必填！！！");

		}
		/*if (StringUtils.isBlank(mallExpCertificate.getCourseRemark())) {
			return R.error("主要课程必填！！！");

		}*/
		if (null == mallExpCertificate.getType() || mallExpCertificate.getType() < 0) {
			return R.error("证书类型必填！！！");

		}

		if (StringUtils.length(mallExpCertificate.getName()) > 20) {
			return R.error("姓名的长度要小于20！！！");

		}
		if (!isCard(mallExpCertificate.getCard())) {
			return R.error("身份证的格式不正确（必须为16位或18位）！！！");

		}
		

		if (StringUtils.length(mallExpCertificate.getCourse()) > 100) {
			return R.error("报读课程的长度要小于100！！！");

		}

		//
		Map map = new HashMap();
		map.put("mobile", mallExpCertificate.getMobile());
		boolean isExists = this.usersService.checkMobile(map);
		if (!isExists) {
			// 平台ID
			mallExpCertificate.setSchoolId(SchoolIdUtils.getSchoolId(request));
			// 修改用户
			mallExpCertificate.setModifyPerson(getUserId());
			// 修改时间
			mallExpCertificate.setModifiedTime(new Date());

			mallExpCertificateService.update(mallExpCertificate);

			return R.ok();
		}

		// 平台ID
		mallExpCertificate.setSchoolId(SchoolIdUtils.getSchoolId(request));
		// 修改用户
		mallExpCertificate.setModifyPerson(getUserId());
		// 修改时间
		mallExpCertificate.setModifiedTime(new Date());

		mallExpCertificateService.update(mallExpCertificate);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("mallexpcertificate:delete")
	public R delete(@RequestBody Integer[] ids) {
		mallExpCertificateService.deleteBatch(ids);

		return R.ok();
	}

	/**
	 * 更新
	 */
	@ResponseBody
	@RequestMapping("/updateStatus")
	@RequiresPermissions("mallexpcertificate:updateStatus")
	public R updateStatus(@RequestBody Integer[] ids) {
		mallExpCertificateService.updateStatus(ids);

		return R.ok();
	}

	/**
	 * 列表
	 * 
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/exportExcelMallExpCertificateTemplate")
	public void exportExcelMallExpCertificateTemplate(HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		
		String arrStr = "0,0,0,0,手机号&0,1,0,0,姓名&0,2,0,0,身份证号&0,3,0,0,报读日期(例:1991/02/30)&0,4,0,0,结课日期(例:1991/02/30)&0,5,0,0,报读课程&0,6,0,0,经验等级（数字1-13）&0,7,0,0,证书编号&0,8,0,0,颁发日期(例:1991/02/30)&0,9,0,0,所属校区(例:永州校区)" +
                "&0,10,0,0,主要课程&0,11,0,0,发放状态(0:未发放 1:发放中 2:已发放)&0,12,0,0,证书类型(1:恒企经验 2:中央财大)";
		/* String result=areaService.getDataByCondition(conditions); */
		/* arrStr+=result; */
		/*Integer type = Integer.valueOf( request.getParameter("type"));
		String title = "恒企经验证书";
		if (type==2) {
			title = "中央财大证书";
		}*/
        String title = "经验证书";
		String cellsStr = new String(arrStr.getBytes("GBK"), "GBK");
		String[] cells = cellsStr.split("&");
		String filename = title+"档案模板导出-" + Calendar.getInstance().getTimeInMillis() + ".xls";
		filename = new String(filename.getBytes("GBK"), "iso-8859-1");
		res.setContentType("text/html; charset=UTF-8");
		res.addHeader("Content-Disposition",
				(new StringBuilder()).append("attachment;filename=").append(filename + ";").toString());
		OutputStream toClient = new BufferedOutputStream(res.getOutputStream());
		ExcelReaderJXL.exportToJxlExcel1(filename, title+"档案", cells, toClient);
		toClient.flush();
		toClient.close();
		/* return R.ok(); */
	}

	/**
	 * EXCEL批量导入
	 * 
	 * @throws Exception
	 */
	@SysLog("EXCEL批量导入")
	@ResponseBody
	@RequestMapping("/getExcelMallExpCertificateData")
	public R getExcelMallExpCertificateData(HttpServletRequest request) throws Exception {
		String mobile = null;
		String name = null;
		String card = null;
		String readDate = null;
		String endDate = null;
		String course = null;
		String exp = null;
		String certNo = null;
		String sendDate = null;
		String school = null;
		String courseRemark = null;
		String sendStatus = null;
        Long userId = null;
        String type = null;
		
		MultipartHttpServletRequest mpReq = (MultipartHttpServletRequest) request;
		MultipartFile file = mpReq.getFile("file_data");
		//Integer type = Integer.valueOf( mpReq.getParameter("type"));
		/* String fileName = file.getOriginalFilename(); */
		/* List<String> warningMesages=null; */
		List<MallExpCertificateEntity> detailList = new ArrayList<MallExpCertificateEntity>();
		MallExpCertificateEntity mallExpCertificate = null;
		FileInputStream fio = (FileInputStream) file.getInputStream();
		List<String[]> dataList = ExcelReaderJXL.readExcel(fio);

		String exceptMsg = "";
		String[] header = dataList.get(0);
		ArrayList<String> exceptList = new ArrayList<String>();

		if (header.length < 13) {
			exceptMsg = "总列数不正确，请核对一下列数；";
			exceptList.add(exceptMsg);
		} else if (!header[0].trim().equals("手机号")) {
			exceptMsg = "对应的列名不对，请核对一下是否与【手机号】匹配;";
			exceptList.add(exceptMsg);
		} else if (!header[1].trim().equals("姓名")) {
			exceptMsg = "对应的列名不对，请核对一下是否与【姓名】匹配;";
			exceptList.add(exceptMsg);
		} else if (!header[2].trim().equals("身份证号")) {
			exceptMsg = "对应的列名不对，请核对一下是否与【身份证号】匹配;";
			exceptList.add(exceptMsg);
		} else if (!header[3].trim().equals("报读日期(例:1991/02/30)")) {
			exceptMsg = "对应的列名不对，请核对一下是否与【报读日期】匹配;";
			exceptList.add(exceptMsg);
		} else if (!header[4].trim().equals("结课日期(例:1991/02/30)")) {
			exceptMsg = "对应的列名不对，请核对一下是否与【结课日期】匹配;";
			exceptList.add(exceptMsg);
		}
		// 报读课程&0,6,0,0,经验等级（数字1-13）&0,7,0,0,证书编号&0,8,0,0,颁发日期&0,9,0,0,所属校区&0,10,0,0,主要课程";
		else if (!header[5].trim().equals("报读课程")) {
			exceptMsg = "对应的列名不对，请核对一下是否与【报读课程】匹配;";
			exceptList.add(exceptMsg);
		} else if (!header[6].trim().equals("经验等级（数字1-13）")) {
			exceptMsg = "对应的列名不对，请核对一下是否与【经验等级（数字1-13）】匹配;";
			exceptList.add(exceptMsg);
		} else if (!header[7].trim().equals("证书编号")) {
			exceptMsg = "对应的列名不对，请核对一下是否与【证书编号】匹配;";
			exceptList.add(exceptMsg);
		} else if (!header[8].trim().equals("颁发日期(例:1991/02/30)")) {
			exceptMsg = "对应的列名不对，请核对一下是否与【颁发日期】匹配;";
			exceptList.add(exceptMsg);
		} else if (!header[9].trim().equals("所属校区(例:永州校区)")) {
			exceptMsg = "对应的列名不对，请核对一下是否与【所属校区(例:永州校区)】匹配;";
			exceptList.add(exceptMsg);
		} else if (!header[10].trim().equals("主要课程")) {
			exceptMsg = "对应的列名不对，请核对一下是否与【主要课程】匹配;";
			exceptList.add(exceptMsg);
		}else if (!header[11].trim().equals("发放状态(0:未发放 1:发放中 2:已发放)")) {
			exceptMsg = "对应的列名不对，请核对一下是否与【发放状态(0:未发放 1:发放中 2:已发放)】匹配;";
			exceptList.add(exceptMsg);
		}else if (!header[12].trim().equals("证书类型(1:恒企经验 2:中央财大)")){
            exceptMsg = "对应的列名不对，请核对一下是否与【证书类型(1:恒企经验 2:中央财大)】匹配;";
            exceptList.add(exceptMsg);
        }
		else if (header.length >= 13 && header[0].trim().equals("手机号") && header[1].trim().equals("姓名")
				&& header[2].trim().equals("身份证号") && header[3].trim().equals("报读日期(例:1991/02/30)")
				&& header[4].trim().equals("结课日期(例:1991/02/30)") && header[5].trim().equals("报读课程")
				&& header[6].trim().equals("经验等级（数字1-13）") && header[7].trim().equals("证书编号")
				&& header[8].trim().equals("颁发日期(例:1991/02/30)") && header[9].trim().equals("所属校区(例:永州校区)")
				&& header[10].trim().equals("主要课程")
				&& header[11].trim().equals("发放状态(0:未发放 1:发放中 2:已发放)")
                && header[12].trim().equals("证书类型(1:恒企经验 2:中央财大)")) {
			dataList.remove(0);
			for (int i = 0; i < dataList.size(); i++) {
				mallExpCertificate = new MallExpCertificateEntity();
				// 默认状态
				mallExpCertificate.setStatus(1);
				// 平台ID
				mallExpCertificate.setSchoolId(SchoolIdUtils.getSchoolId(request));
				// 创建用户
				mallExpCertificate.setCreatePerson(getUserId());
				// 创建时间
				mallExpCertificate.setCreationTime(new Date());

				String[] dataArr = dataList.get(i);
				if (dataArr.length==13) {
					
				
				Map<String, Object> map = getMapPage(request);
				boolean flag = false;
				exceptMsg = "第" + new Integer(i + 2).toString() + "行数据不能导入原因是：";

				mobile = dataArr[0];
				name = dataArr[1];
				card = dataArr[2];
				readDate = dataArr[3];
				endDate = dataArr[4];
				course = dataArr[5];
				exp = dataArr[6];
				certNo = dataArr[7];
				sendDate = dataArr[8];
				school = dataArr[9];
				courseRemark = dataArr[10];
				sendStatus = dataArr[11];
				type = dataArr[12];
								
				
				if (name == null || name.equals("")) {
                    if(flag==true){
                        exceptMsg += ",姓名不能为空";
                    }else {
                        exceptMsg += "姓名不能为空";
                        //exceptList.add(exceptMsg);
                        flag = true;
                    }

				} else {
					if (StringUtils.length(name) > 20) {
						if (flag == true) {
							exceptMsg += ",姓名长度不能大于20个字符";
						} else {
							exceptMsg += "姓名长度不能大于20个字符";
							flag = true;
						}
					}

				}
				if (card == null || card.equals("")) {
                    if(flag==true){
                        exceptMsg += ",身份证号不能为空";
                    }else {
                        exceptMsg += "身份证号不能为空";
                        //exceptList.add(exceptMsg);
                        flag = true;
                    }
				} else {
					if (!isCard(card)) {
						if (flag == true) {
							exceptMsg += ",身份证的格式不正确（长度必须为16位或18位）";
						} else {
							exceptMsg += "身份证的格式不正确（长度必须为16位或18位）";
							flag = true;
						}
					}

				}
				Date readDates = null;
				if (readDate == null || readDate.equals("")) {
                    if(flag==true){
                        exceptMsg += ",报读日期不能为空";
                    }else {
                        exceptMsg += "报读日期不能为空";
                        //exceptList.add(exceptMsg);
                        flag = true;
                    }
				} else {
					readDates = DateUtils.parse(readDate.replaceAll(" ", ""), "yyyy/MM/dd");
					if (null == readDates) {
						if (flag == true) {
							exceptMsg += ",报读日期格式不正确";
						} else {
							exceptMsg += "报读日期格式不正确";
							flag = true;
						}
					}
				}
				Date endDates = null;
				if (endDate == null || endDate.equals("")) {
                    if(flag==true){
                        exceptMsg += ",结课日期不能为空";
                    }else {
                        exceptMsg += "结课日期不能为空";
                        //exceptList.add(exceptMsg);
                        flag = true;
                    }
				} else {
					endDates = DateUtils.parse(endDate.replaceAll(" ", ""), "yyyy/MM/dd");
					if (null == endDates) {
						if (flag == true) {
							exceptMsg += ",结课日期格式不正确";
						} else {
							exceptMsg += "结课日期格式不正确";
							flag = true;
						}
					}
				}
				if (course == null || course.equals("")) {
                    if(flag==true){
                        exceptMsg += ",报读课程不能为空";
                    }else {
                        exceptMsg += "报读课程不能为空";
                        //exceptList.add(exceptMsg);
                        flag = true;
                    }
				} else {
					if (StringUtils.length(mallExpCertificate.getCourse()) > 100) {
						if (flag == true) {
							exceptMsg += ",报读课程的长度要小于100字符";
						} else {
							exceptMsg += "报读课程的长度要小于100字符";
							flag = true;
						}
					}

				}
				
				//证书编号
				if (certNo != null && !certNo.equals("")) {
                   
					if (StringUtils.length(mallExpCertificate.getCertNo()) > 20) {
						if (flag == true) {
							exceptMsg += ",证书编号的长度要小于20字符";
						} else {
							exceptMsg += "证书编号的长度要小于20字符";
							flag = true;
						}
					}
				} 
				Date sendDates = null;
				if (sendDate != null && !sendDate.equals("")) {                  
					sendDates = DateUtils.parse(sendDate.replaceAll(" ", ""), "yyyy/MM/dd");
					if (null == sendDates) {
						if (flag == true) {
							exceptMsg += ",颁发日期格式不正确";
						} else {
							exceptMsg += "颁发日期格式不正确";
							flag = true;
						}
					}					
				} else if(sendDate == null || sendDate.equals("")){
					if (flag == true) {
						exceptMsg += ",颁发日期不能为空";
					} else {
						exceptMsg += "颁发日期不能为空";
						flag = true;
					}
				}
				//校区id				
				Integer intSendStatus = 0;
				if (sendStatus == null || sendStatus.equals("")) {
					intSendStatus = 0;
				}else{
					try {
						intSendStatus = Integer.valueOf(sendStatus);
						if (intSendStatus<0 || intSendStatus>2) {
                            if(flag==true){
                                exceptMsg += ",发送状态的值不正确";
                            }else {
                                exceptMsg += "发送状态的值不正确";
                                //exceptList.add(exceptMsg);
                                flag = true;
                            }
						}
					} catch (Exception e) {
						// TODO: handle exception
						exceptMsg += ",发送状态格式不正确";
						//exceptList.add(exceptMsg);
						flag = true;
					}
				}
				Integer intType = 0;
				if (type == null || type.equals("")) {
                    if (flag == true) {
                        exceptMsg += ",发送证书类型的值不能为空";
                    } else {
                        exceptMsg += "发送证书类型的值不能为空";
                        flag = true;
                    }
				}else{
					try {
                        intType = Integer.valueOf(type);
						if ( intType < 1 || intType > 2) {
                            if(flag==true){
                                exceptMsg += ",发送证书类型的值不正确";
                            }else {
                                exceptMsg += "发送证书类型的值不正确";
                                //exceptList.add(exceptMsg);
                                flag = true;
                            }
						}
					} catch (Exception e) {
						// TODO: handle exception
						exceptMsg += "发送证书类型格式不正确";
						//exceptList.add(exceptMsg);
						flag = true;
					}
				}				
				//手机号
				if (mobile != null && !mobile.equals("")) {
					if (!isMobile(mobile)) {
						if (flag == true) {
							exceptMsg += ",手机号的格式不正确（必须为11位的数字）";
						} else {
							exceptMsg += "手机号的格式不正确（必须为11位的数字）";
							flag = true;
						}
					} else {
						map.put("mobile", mobile);
						boolean isExists = this.usersService.checkMobile(map);
						if (!isExists) {
							//非学员的手机号也得保存	
							mallExpCertificate.setMobile(mobile);							
						}else{																						
                            Map<String,Object> queryMap = new HashMap<String,Object>();
                            queryMap.put("mobilePhoneNo",mobile);
                            userId = this.usersService.getUserIdByMobilePhoneNo(queryMap);
                            mallExpCertificate.setUserId(userId);
                        }
					}					
				} 

				if (flag) {
					exceptList.add(exceptMsg);
					continue;
				}
								
				mallExpCertificate.setCard(card);
				mallExpCertificate.setMobile(mobile);
				mallExpCertificate.setName(name);
				mallExpCertificate.setReadDate(readDates);
				mallExpCertificate.setEndDate(endDates);
				mallExpCertificate.setCourse(course);
				//经验等级
				Integer exps = null;
				if (exp != null && !exp.equals("")) {
					exps = Integer.valueOf(exp);
                    if (exps < 1 || exps >13 ) {
                        if (flag == true) {
                            exceptMsg += ",经验等级值不正确";
                        } else {
                            exceptMsg += "经验等级值不正确";
                            flag = true;
                        }
                        //经验等级选填；
                    }
					mallExpCertificate.setExp(exps);
				} 
				
				mallExpCertificate.setCertNo(certNo);
				mallExpCertificate.setSendDate(sendDates);
				Long schools = null;
				if (school != null && !school.equals("")) {                  
					school = school.trim().replaceAll(" ", "");
					map.put("name", school);
					List result = this.sysDeptService.queryTotalByNcCode(map);
					if (result == null || result.size() <= 0) {
						if (flag == true) {
							exceptMsg += ",所属校区不存在";
						} else {
							exceptMsg += "所属校区不存在";
							flag = true;
						}
					}else{
						schools =  ((SysDeptEntity)result.get(0)).getDeptId();
						int value = schools.intValue();
						mallExpCertificate.setSchool(value);
					}
				} 
				mallExpCertificate.setCourseRemark(courseRemark);
				mallExpCertificate.setSendStatus(intSendStatus);
				mallExpCertificate.setType(intType);			
				detailList.add(mallExpCertificate);
			}
			}
		}
		/* return R.ok(); */
		String errMsgErr = "";
		for (int i = 0; i < exceptList.size(); i++) {
			errMsgErr += exceptList.get(i) + "<br>";
			/* System.out.println(warningMesages.get(i)); */
		}
		//courseExamRecord.setDetailList(detailList);
		//courseExamRecord.setProductId(productIdLast);

        if (errMsgErr.equals("")) {
            // 保存
            if (!detailList.isEmpty()) {
                this.mallExpCertificateService.batchSave(detailList);
            }
			return R.ok("");
		} else {
			return R.error(1,errMsgErr);
		}
	}

	private static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {

			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	private static boolean isMobile(String str) {
		if (null != str && isNumeric(str.trim()) && str.trim().length() == 11) {
			return true;
		}
		return false;
	}

	private static boolean isCard(String str) {
		if (null != str && (str.trim().length() == 16 || str.trim().length() == 18)) {
			return true;
		}
		return false;
	}

}
