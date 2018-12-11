package io.renren.service.impl;

import io.renren.entity.CourseClassplanEntity;
import io.renren.enums.AuditStatusEnum;
import io.renren.enums.CourseAbormalTypeEnum;
import io.renren.pojo.CourseAbnormalClassplanPOJO;
import io.renren.pojo.query.CourseAbnormalClassplanQuery;
import io.renren.rest.persistent.KGS;
import io.renren.service.CourseClassplanService;
import io.renren.service.UsersService;
import io.renren.utils.ExcelReaderJXL;
import io.renren.utils.RRException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.renren.common.validator.Assert;

import io.renren.dao.CourseAbnormalClassplanDao;
import io.renren.entity.CourseAbnormalClassplanEntity;
import io.renren.service.CourseAbnormalClassplanService;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;


@Service("courseAbnormalClassplanService")
public class CourseAbnormalClassplanServiceImpl implements CourseAbnormalClassplanService {

	@Autowired
	private CourseAbnormalClassplanDao courseAbnormalClassplanDao;

	//排课计划service
	@Autowired
	private CourseClassplanService courseClassplanService;


	//学员service
	@Autowired
	private UsersService usersService;

	@Resource
	private KGS courseAbnormalClassplanNoKGS;

	private static final String ABNORMALCLASSPLANNO_HEAD = "HQAC";


	/**
	 * 查询详情
	 * @param id
	 * @return
	 */
	@Override
	public CourseAbnormalClassplanPOJO queryObject(Long id){
		CourseAbnormalClassplanPOJO courseAbnormalClassplanPOJO = this.courseAbnormalClassplanDao.queryObject(id);
		initPOJO(courseAbnormalClassplanPOJO);
		return courseAbnormalClassplanPOJO;
	}

	/**
	 * 取消
	 *
	 * @param id 主键
	 */
	@Override
	public void updateCancle(Long id){
		if(null == id){
			throw new RRException("缺少主键");
		}
		this.courseAbnormalClassplanDao.updateCancle(id);
	}

	/**
	 * 查询列表
	 * @param query
	 * @return
	 */
	@Override
	public List<CourseAbnormalClassplanPOJO> queryList(CourseAbnormalClassplanQuery query){
		List<CourseAbnormalClassplanPOJO> courseAbnormalClassplanPOJOS = this.courseAbnormalClassplanDao.queryList(query);
		if(courseAbnormalClassplanPOJOS != null && courseAbnormalClassplanPOJOS.size() > 0){
			for (int i = 0; i < courseAbnormalClassplanPOJOS.size(); i++) {
				CourseAbnormalClassplanPOJO courseAbnormalClassplanPOJO = courseAbnormalClassplanPOJOS.get(i);
				initPOJO(courseAbnormalClassplanPOJO);
			}
		}
		return courseAbnormalClassplanPOJOS;
	}

	/**
	 * 查询数量
	 * @param query
	 * @return
	 */
	@Override
	public int queryTotal(CourseAbnormalClassplanQuery query){
		return this.courseAbnormalClassplanDao.queryTotal(query);
	}

	/**
	 * 新增
	 * @param courseAbnormalClassplanPOJO
	 */
	@Override
	public void save(CourseAbnormalClassplanPOJO courseAbnormalClassplanPOJO){
		//校验
		verifyForm(courseAbnormalClassplanPOJO);
		//转换
		CourseAbnormalClassplanEntity courseAbnormalClassplanEntity = pojoToEntity(courseAbnormalClassplanPOJO);
		//状态校验
		verifyStatus(courseAbnormalClassplanEntity.getStudentId() , courseAbnormalClassplanEntity.getClassplanId());
		//编号
		courseAbnormalClassplanEntity.setAbnormalClassplanNo(getAbnormalClassplanNo());
		//保存
		this.courseAbnormalClassplanDao.save(courseAbnormalClassplanEntity);
	}

	/**
	 * 更新
	 * @param courseAbnormalClassplanPOJO
	 */
	@Override
	public void update(CourseAbnormalClassplanPOJO courseAbnormalClassplanPOJO) throws RRException{
		//非空校验
		verifyForm(courseAbnormalClassplanPOJO);
		//状态校验
		verifyStatus(courseAbnormalClassplanPOJO.getStudentId() , courseAbnormalClassplanPOJO.getClassplanId());
		CourseAbnormalClassplanEntity courseAbnormalClassplanEntity = pojoToEntity(courseAbnormalClassplanPOJO);
		//更新时间
		this.courseAbnormalClassplanDao.update(courseAbnormalClassplanEntity);
	}

	/**
	 * 审核通过
	 *
	 * @param id
	 * @param userId
	 */
	@Override
	public void updateAdopt(Long id, Long userId) {
		CourseAbnormalClassplanPOJO courseAbnormalClassplanPOJO = this.queryObject(id);
		verifyStatus(courseAbnormalClassplanPOJO.getStudentId() , courseAbnormalClassplanPOJO.getClassplanId());
		this.courseAbnormalClassplanDao.updateAdopt(id , userId , new Date());
	}

	/**
	 * 批量上传
	 *
	 * @param file 文件流
	 * @return 处理结果
	 */
	@Override
	public String importExcelTemplateCourse(MultipartFile file , Long createUserId) {
		StringBuffer errorMsg = new StringBuffer();
		if (null != file){
			List<String[]> dataList = null;
			try {
				FileInputStream fio = (FileInputStream) file.getInputStream();
				dataList = ExcelReaderJXL.readExcel(fio);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(CollectionUtils.isNotEmpty(dataList) && dataList.size() > 1){
				for(int i = 1 , length = dataList.size() ; i < length ; i++) {
					String[] dataItem = dataList.get(i);
					//学员手机号码
					String userMobile = StringUtils.trim(dataItem[0]);
					//课程名称
					String courseName = StringUtils.trim(dataItem[1]);
					//类型-弃考、免考
					String abnormalTypeName = StringUtils.trim(dataItem[2]);
					//原因
					String abnormalReason = StringUtils.trim(dataItem[3]);
					//备注
					String remark = StringUtils.trim(dataItem[4]);

					try {
						this.importSave(userMobile, courseName, abnormalTypeName, abnormalReason, remark, createUserId);
					}catch (RRException e){
						errorMsg.append("第" + (i+1) + "行错误：" + e + "<br/>");
					}catch (Exception e){
						errorMsg.append("第" + (i+1) + "行错误：" + e + "<br/>");
					}
				}
			}else{
				errorMsg.append("数据为空!");
			}
		}else{
			errorMsg.append("数据为空!");
		}
		return errorMsg.toString();
	}

	/**
	 * 课程名称查询最近排课计划entity
	 *
	 * @param courseName 课程名称
	 * @return 排课计划entity
	 * @throws RRException 业务异常信息
	 */
	@Override
	public CourseClassplanEntity queryCourseClassplanEntityByCourseName(String courseName) {
		return this.courseAbnormalClassplanDao.queryCourseClassplanEntityByCourseName(courseName);
	}

	/**
	 * 校验学员是否有休学或者弃考同一门排课计划
	 *
	 * @param userId      用户PK
	 * @param classplanId 排课计划PK
	 */
	@Override
	public void verifyStatus(Long userId, String classplanId) {
		if(null != userId && StringUtils.isNotBlank(classplanId)){
			CourseAbnormalClassplanEntity courseAbnormalClassplanEntity = this.courseAbnormalClassplanDao.queryObjectByClassplan(userId, classplanId);
			//如果存在已审核类似的单据(学员PK 排课计划PK)
			if(null != courseAbnormalClassplanEntity){
				throw new RRException("存在类似单据！单据单号为：" + courseAbnormalClassplanEntity.getAbnormalClassplanNo());
			}
		}
	}

	/**
	 * @param userId
	 * @param classplanIds
	 */
	@Override
	public boolean verifyQikao(Long userId, List classplanIds) {
		return this.verifyStatus(userId , classplanIds , CourseAbormalTypeEnum.qikao);
	}

	/**
	 * @param userId
	 * @param classplanIds
	 */
	@Override
	public boolean verifyMiankao(Long userId, List classplanIds) {
		return this.verifyStatus(userId , classplanIds , CourseAbormalTypeEnum.miankao);
	}


	/**
	 * 校验学员排课是否都是一个状态
	 *
	 * @param userId
	 * @param classplanIds
	 * @param courseAbormalType
	 */
	private boolean verifyStatus(Long userId, List classplanIds, CourseAbormalTypeEnum courseAbormalType) {
		Assert.isNull(userId , "用户PK不能为空");
		if(CollectionUtils.isEmpty(classplanIds)){
			throw new RRException("排课计划PK集合不能为空!");
		}
		if(CourseAbormalTypeEnum.qikao != courseAbormalType && CourseAbormalTypeEnum.miankao != courseAbormalType){
			throw new RRException("请传入正确的状态类型!");
		}
		int total = this.courseAbnormalClassplanDao.queryTotalByClassplans(userId, classplanIds, courseAbormalType.getValue());
		return total == classplanIds.size();
	}

	/**
	 * 批量导入中的新增方法
	 * @param userMobile	学员手机号码
	 * @param courseName	课程名称
	 * @param abnormalTypeName	类型-弃考、免考
	 * @param abnormalReason	原因
	 * @param remark			备注
	 * @param createUserId		创建用户PK
	 */
	private void importSave(String userMobile , String courseName , String abnormalTypeName , String abnormalReason , String remark, Long createUserId){
		//查询条件校验
		Assert.isBlank(userMobile , "学员手机号码为空!");
		Assert.isBlank(courseName , "课程名称为空!");
		Assert.isBlank(abnormalTypeName , "类型为空!");

		if(!CourseAbormalTypeEnum.qikao.getText().equals(abnormalTypeName) && !CourseAbormalTypeEnum.miankao.getText().equals(abnormalTypeName)){
			throw new RRException("类型错误,请填写【弃考】或【免考】!");
		}

		//学员PK
		Map<String , Object> queryUserIdMap = new HashMap<>();
		queryUserIdMap.put("mobile" , userMobile);
		Integer userId = usersService.queryUserId(queryUserIdMap);
		Assert.isNull(userId , "未查询到该手机号码的学员!");

		//排课计划
		CourseClassplanEntity courseClassplanEntity = this.queryCourseClassplanEntityByCourseName(courseName);
		Assert.isNull(courseClassplanEntity , "课程对应排课计划查询失败!");

		//新增对象
		CourseAbnormalClassplanPOJO courseAbnormalClassplanPOJO = new CourseAbnormalClassplanPOJO();
		//操作用户PK
		courseAbnormalClassplanPOJO.setUpdatePerson(createUserId);
		courseAbnormalClassplanPOJO.setCreatePerson(createUserId);
		courseAbnormalClassplanPOJO.setModifyPerson(createUserId);
		//类型-弃考、免考
		courseAbnormalClassplanPOJO.setAbnormalType(CourseAbormalTypeEnum.getE(abnormalTypeName).getValue());
		//状态-通过
		courseAbnormalClassplanPOJO.setAuditStatus(AuditStatusEnum.tongguo.getValue());
//		原因
		courseAbnormalClassplanPOJO.setAbnormalReason(abnormalReason);
		//排课计划PK
		courseAbnormalClassplanPOJO.setClassplanId(courseClassplanEntity.getClassplanId());
		//备注
		courseAbnormalClassplanPOJO.setRemark(remark);
		//学员PK
		courseAbnormalClassplanPOJO.setStudentId(Long.valueOf(userId));
		//新增
		this.save(courseAbnormalClassplanPOJO);
	}

	/**
	 * 字段类型校验
	 * @param courseAbnormalClassplanPOJO
	 */
	private void verifyForm(CourseAbnormalClassplanPOJO courseAbnormalClassplanPOJO) {
		Assert.isNull(courseAbnormalClassplanPOJO , "courseAbnormalClassplanPOJO对象错误!");
		Assert.isNull(courseAbnormalClassplanPOJO.getStudentId() , "请选择学员！");
		Assert.isNull(courseAbnormalClassplanPOJO.getAbnormalType() , "请选择异常类型！");
		Assert.isNull(courseAbnormalClassplanPOJO.getClassplanId() , "请选择排课计划！");
	}

	/**
	 * pojo->entity
	 * @param courseAbnormalClassplanPOJO
	 * @return
	 */
	private CourseAbnormalClassplanEntity pojoToEntity(CourseAbnormalClassplanPOJO courseAbnormalClassplanPOJO){
		CourseAbnormalClassplanEntity courseAbnormalClassplanEntity = null;
		if(null != courseAbnormalClassplanPOJO){
			courseAbnormalClassplanEntity = new CourseAbnormalClassplanEntity();
			//pojo->entity
			//主键
			courseAbnormalClassplanEntity.setId(courseAbnormalClassplanPOJO.getId());
			//原因
			courseAbnormalClassplanEntity.setAbnormalReason(courseAbnormalClassplanPOJO.getAbnormalReason());
			//类型
			courseAbnormalClassplanEntity.setAbnormalType(courseAbnormalClassplanPOJO.getAbnormalType());
			//状态
			courseAbnormalClassplanEntity.setAuditStatus(courseAbnormalClassplanPOJO.getAuditStatus());
			//排课计划PK
			String classplanId = courseAbnormalClassplanPOJO.getClassplanId();
			courseAbnormalClassplanEntity.setClassplanId(classplanId);
			if(StringUtils.isNotBlank(classplanId)){
				//排课计划entity
				CourseClassplanEntity courseClassplanEntity = this.courseClassplanService.queryObjectByClassplanId(classplanId);
				if(null != courseClassplanEntity){
					//课程PK
					courseAbnormalClassplanEntity.setCourseId(courseClassplanEntity.getCourseId());
				}
			}
			//备注
			courseAbnormalClassplanEntity.setRemark(courseAbnormalClassplanPOJO.getRemark());
			//学员PK
			courseAbnormalClassplanEntity.setStudentId(courseAbnormalClassplanPOJO.getStudentId());
			//创建用户
			courseAbnormalClassplanEntity.setCreatePerson(courseAbnormalClassplanPOJO.getCreatePerson());
			//更新用户
			courseAbnormalClassplanEntity.setUpdatePerson(courseAbnormalClassplanPOJO.getCreatePerson());
			//审核用户
			courseAbnormalClassplanEntity.setModifyPerson(courseAbnormalClassplanPOJO.getModifyPerson());

		}
		return courseAbnormalClassplanEntity;
	}

	/**
	 * pojo 类型转换成名称
	 * @param courseAbnormalClassplanPOJO
	 */
	private void initPOJO(CourseAbnormalClassplanPOJO courseAbnormalClassplanPOJO){
		if(null != courseAbnormalClassplanPOJO){
			courseAbnormalClassplanPOJO.setAbnormalTypeName(CourseAbormalTypeEnum.getText(courseAbnormalClassplanPOJO.getAbnormalType()));
			courseAbnormalClassplanPOJO.setAuditStatusName(AuditStatusEnum.getText(courseAbnormalClassplanPOJO.getAuditStatus()));
		}
	}

	private String getAbnormalClassplanNo(){
		return ABNORMALCLASSPLANNO_HEAD + courseAbnormalClassplanNoKGS.nextId();
	}


}
