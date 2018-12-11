package com.hq.courses.service.impl;

import com.hq.courses.dao.CsCourseDao;
import com.hq.courses.entity.CsCourseEntity;
import com.hq.courses.pojo.CsCourseExcelInitializationColumnEnum;
import com.hq.courses.pojo.CsCoursePOJO;
import com.hq.courses.pojo.query.CsCourseQuery;
import com.hq.courses.pojo.query.CsSectionQuery;
import com.hq.courses.service.CsChapterService;
import com.hq.courses.service.CsCourseService;
import com.hq.courses.service.CsSectionService;
import com.hq.courses.util.CodeValidateUtils;
import io.renren.common.doc.ParamKey;
import io.renren.common.validator.Assert;
import io.renren.entity.SysDeptEntity;
import io.renren.entity.SysProductEntity;
import io.renren.service.SysDeptService;
import io.renren.service.SysProductService;
import io.renren.utils.ExcelReaderJXL;
import io.renren.utils.RRException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 课程
 *
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-06 16:48:13
 */
@Service("csCourseService")
public class CsCourseServiceImpl implements CsCourseService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CsCourseDao csCourseDao;
    @Autowired
    private CsChapterService csChapterService;
    @Autowired
    private CsSectionService csSectionService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private SysProductService sysProductService;


    private static final int MAX_COURSENO_LENGTH = 5;
    /**
     * 根据ID查询查询课程
	 * @param courseId	id
	 * @return	课程
     */
    @Override
	public CsCoursePOJO queryObject(Long courseId){
        CsCourseEntity csCourseEntity = csCourseDao.queryObject(courseId);
        return entityToPojo(csCourseEntity);
    }

    /**
     * 查询课程列表
     * @param query
	 * @return	课程列表
     */
    @Override
	public List<CsCoursePOJO> queryList(CsCourseQuery query){
		List<CsCoursePOJO> coursePOJOs = null ;
        List<CsCourseEntity> courseEntities = csCourseDao.queryList(query);
		if(null != courseEntities && !courseEntities.isEmpty()) {
            coursePOJOs = new ArrayList<>();
            for (CsCourseEntity csCourseEntity : courseEntities) {
                CsCoursePOJO pojo = entityToPojo(csCourseEntity);
                pojo.setChapterNum(queryChapterNum(pojo.getCourseId()));
                pojo.setSectionNum(querySectionNum(pojo.getCourseId()));
                coursePOJOs.add(pojo);
            }
        }
        return coursePOJOs;
    }

    private int queryChapterNum(Long courseId) {
		if(null != courseId) {
            return this.csChapterService.queryTotal(courseId);
        }
        return 0;
    }
    private int querySectionNum(Long courseId) {
		if(null != courseId) {
            CsSectionQuery csSectionQuery = new CsSectionQuery();
            csSectionQuery.setCourseId(courseId);
            return this.csSectionService.queryTotal(csSectionQuery);
        }
        return 0;
    }

    /**
     * entity转pojo
     * @param csCourseEntity
     */
    private CsCoursePOJO entityToPojo(CsCourseEntity csCourseEntity) {
        CsCoursePOJO pojo = new CsCoursePOJO();
		if(null != csCourseEntity) {
            //ID
            pojo.setCourseId(csCourseEntity.getCourseId());
            //名称
            pojo.setCourseName(csCourseEntity.getCourseName());
            //编码
            pojo.setCourseNo(csCourseEntity.getCourseNo());
            pojo.setCreateTime(csCourseEntity.getCreateTime());
            //部门
            Long deptId = csCourseEntity.getDeptId();
			if(null != deptId) {
                pojo.setDeptId(deptId);
                String deptName = queryDeptName(deptId);
                pojo.setDeptName(deptName);
            }
            //蓝鲸ID
            pojo.setLjId(csCourseEntity.getLjId());
            //ncId
            pojo.setNcId(csCourseEntity.getNcId());
            //产品线
            Long productId = csCourseEntity.getProductId();
			if(null != productId) {
                pojo.setProductId(productId);
                String productName = queryProductName(productId);
                pojo.setProductName(productName);
            }
            //备注
            pojo.setRemark(csCourseEntity.getRemark());
            //状态
            pojo.setStatus(csCourseEntity.getStatus());
            //题库
            pojo.setTkId(csCourseEntity.getTkId());
            //修改
            pojo.setUpdateTime(csCourseEntity.getUpdateTime());
            //章
            pojo.setChapterNum(0);
            //节
            pojo.setSectionNum(0);
        }
        return pojo;
    }

    /**
     * 查询产品线名称
     * @param productId
     * @return
     */
    private String queryProductName(Long productId) {
		Map<String, Object> queryProductMap = new HashMap<>();;
        queryProductMap.put("productId", productId);
        SysProductEntity sysProductEntity = this.sysProductService.queryObject(queryProductMap);
        String productName = null;
		if(null != sysProductEntity) {
            productName = sysProductEntity.getProductName();
        }
        return productName;
    }

    /**
     * 查询部门名称
     * @param deptId
     * @return
     */
    private String queryDeptName(Long deptId) {
        String deptName = null;
        SysDeptEntity sysDeptEntity = sysDeptService.queryObject(deptId);
		if(sysDeptEntity != null) {
            deptName = sysDeptEntity.getName();
        }
        return deptName;
    }

    /**
     * 查询课程数量
	 * @return	课程数量
     */
    @Override
	public int queryTotal(CsCourseQuery query){
        return csCourseDao.queryTotal(query);
    }

    /**
     * 新增课程
     */
    @Override
	public int save(CsCourseEntity csCourse){
        this.verifyForm(csCourse);
        csCourse.setStatus(1);
        csCourse.setDr(0);
		CodeValidateUtils.regexMatches(csCourse.getCourseNo(),CodeValidateUtils.CSCOURSE_PATTERN,CodeValidateUtils.CSCOURSE_TYPE);
        return csCourseDao.save(csCourse);
    }

    /**
     * 更新课程
     */
	@Transactional(value = ParamKey.Transactional.hq_courses ,readOnly = false, propagation = Propagation.REQUIRES_NEW,rollbackFor={RuntimeException.class, Exception.class})
    @Override
	public int update(CsCourseEntity csCourse){
        this.verifyForm(csCourse);
        Assert.isNull(csCourse.getCourseId(), "课程ID不能为空!");
        CodeValidateUtils.regexMatches(csCourse.getCourseNo(),CodeValidateUtils.CSCOURSE_PATTERN,CodeValidateUtils.CSCOURSE_TYPE);
        int update = csCourseDao.update(csCourse);
        //更新子集code
        this.csChapterService.updateCodeByParentId(csCourse);
        return update;
    }

    /**
     * 按照ID删除
	 * @param courseId	id
     */
    @Override
	public int delete(Long courseId){
        this.checkQuote(courseId);
        return csCourseDao.delete(courseId);
    }
    /**
     * 校验课程编号是否合法
	 * @param courseId	课程ID
	 * @param courseNo	课程编号
     */
    @Override
    public void checkCourseNo(Long courseId, String courseNo) {
		if(StringUtils.isBlank(courseNo)) {
            throw new RRException("课程编号不能为空!");
        }
        CodeValidateUtils.courseCodeValidate(courseNo);
        CsCourseEntity csCourseEntity = this.csCourseDao.queryByCourseNo(courseId, courseNo);
		if(csCourseEntity != null) {
            throw new RRException("课程编号已经存在!");
        }
    }

    private void verifyForm(CsCourseEntity csCourse) {
        Assert.isNull(csCourse, "参数错误!");
        Assert.isBlank(csCourse.getCourseName(), "名称不能为空!");
//		Assert.isBlank(csCourse.getCourseNo(), "编号不能为空!");
		checkCourseNo(csCourse.getCourseId(),csCourse.getCourseNo());
        Assert.isNull(csCourse.getProductId(), "请选择产品线!");
        Assert.isNull(csCourse.getDeptId(), "请选择公司!");
    }

    @Override
    public int enable(Long courseId) {
        Assert.isNull(courseId, "ID不能为空!");
        return updateStatus(courseId, 1);
    }

    @Override
    public int unenable(Long courseId) {
        Assert.isNull(courseId, "ID不能为空!");
        return updateStatus(courseId, 0);
    }
    /**
     * 更新状态
     * @param courseId
     * @param status
     * @return
     */
	private int updateStatus(Long courseId , Integer status) {
        return this.csCourseDao.updateStatus(courseId, status);
    }
    /**
     * 知识点是否被引用
	 * @param courseId	知识点ID
	 * @return				被引用说明从;未被引用返回NULL
     */
    @Override
    public void checkQuote(Long courseId) {
//		return null;
    }

	private String saveCourseExcelColumn( String kcbm,String kcmc ,
			String zt,String dr,String cpxId,String bmId,String bz , String ncId,String ljId,String tkId,Integer line) {
        //非空校验
		if(StringUtils.isBlank(kcbm)) return CsCourseExcelInitializationColumnEnum.kcbm.getColumnName()+"不能为空！";
		if(StringUtils.isBlank(kcmc)) return CsCourseExcelInitializationColumnEnum.kcmc.getColumnName()+"不能为空！";
		if(StringUtils.isBlank(zt)) return CsCourseExcelInitializationColumnEnum.zt.getColumnName()+"不能为空！";
		if(StringUtils.isBlank(dr)) return CsCourseExcelInitializationColumnEnum.dr.getColumnName()+"不能为空！";
		if(StringUtils.isBlank(cpxId)) return CsCourseExcelInitializationColumnEnum.cpxId.getColumnName()+"不能为空！";
		if(StringUtils.isBlank(bmId)) return CsCourseExcelInitializationColumnEnum.bmId.getColumnName()+"不能为空！";
        CsCourseEntity csCourseEntity = this.csCourseDao.queryByCourseNo(null, kcbm);
		if(csCourseEntity == null) {
            try {
                int count = csCourseDao.queryByCourseName(kcmc);
				if(count > 0) {
					return CsCourseExcelInitializationColumnEnum.kcmc.getColumnName()+"已存在！";
                }
                CsCourseEntity csCourse = new CsCourseEntity();
                csCourse.setCourseNo(kcbm);
                csCourse.setCourseName(kcmc);
                csCourse.setStatus(Integer.valueOf(zt));
                csCourse.setDr(Integer.valueOf(dr));
                csCourse.setProductId(Long.valueOf(cpxId));
                csCourse.setDeptId(Long.valueOf(bmId));
                csCourse.setRemark(bz);
                csCourse.setNcId(ncId);
                csCourse.setLjId(ljId);
                csCourse.setTkId(tkId);
                CodeValidateUtils.regexMatches(kcbm,CodeValidateUtils.CSCOURSE_PATTERN,CodeValidateUtils.CSCOURSE_TYPE);
                csCourseDao.save(csCourse);
            } catch (RRException e) {
                logger.error("e.getMessageis{ }", e);
                return e.getMsg();
            }
        }
        return null;
    }

    @Override
    public CsCourseEntity queryObject(String courseNo) {
        return csCourseDao.queryByCourseNo(null, courseNo);
    }

    @Override
    public String importCourseExcelInitialization(MultipartFile file) {
		if(null != file) {
            StringBuffer errorMsg = new StringBuffer();
            try {
                FileInputStream fio = (FileInputStream) file.getInputStream();
                List<String[]> dataList = ExcelReaderJXL.readExcel(fio);
                String[] header = dataList.get(0);
				if(null == dataList || dataList.isEmpty() || dataList.size() <= 1) {
                    throw new RRException("文件或内容有问题，请重新导入！");
                }
                int columnLength = 11;
                //总共六行数据
                //章名称	节名称	知识点名称	难度	考点	题型(多个请用逗号隔开)
				if(header.length < columnLength){
                    throw new RRException("总列数不正确，请核对一下列数或重新下载导入模板！");
                }
				for(int i = 1 , length = dataList.size() ; i < length ; i++) {
					int line = i+1;
                    String[] dataItem = dataList.get(i);
                    //列数校验
					if(dataItem.length < columnLength) {
                        errorMsg.append("第" + line + "行错误：" + "列数不正确！<br/>");
                        continue;
                    }
                    //列
                    String kcbm = dataItem[CsCourseExcelInitializationColumnEnum.kcbm.getExcelIndex()];
                    String kcmc = dataItem[CsCourseExcelInitializationColumnEnum.kcmc.getExcelIndex()];
                    String zt = dataItem[CsCourseExcelInitializationColumnEnum.zt.getExcelIndex()];
                    String dr = dataItem[CsCourseExcelInitializationColumnEnum.dr.getExcelIndex()];
                    String cpxId = dataItem[CsCourseExcelInitializationColumnEnum.cpxId.getExcelIndex()];
                    String bmId = dataItem[CsCourseExcelInitializationColumnEnum.bmId.getExcelIndex()];
                    String bz = dataItem[CsCourseExcelInitializationColumnEnum.bz.getExcelIndex()];
                    String ncId = dataItem[CsCourseExcelInitializationColumnEnum.ncId.getExcelIndex()];
                    String ljId = dataItem[CsCourseExcelInitializationColumnEnum.ljId.getExcelIndex()];
                    String tkId = dataItem[CsCourseExcelInitializationColumnEnum.tkId.getExcelIndex()];
					String saveExcelColumnString = saveCourseExcelColumn(kcbm ,kcmc, zt,dr, cpxId, bmId, bz, ncId , ljId,tkId,line);
					if(StringUtils.isNotBlank(saveExcelColumnString)) {
						errorMsg.append("第" + line + "行错误：" + saveExcelColumnString +"<br/>");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return errorMsg.toString();
        }
        return null;
    }
}
