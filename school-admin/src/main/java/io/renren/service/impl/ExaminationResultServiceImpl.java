package io.renren.service.impl;

import java.io.FileInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hq.courses.pojo.ExaminationEnum;

import io.renren.dao.ExaminationResultDao;
import io.renren.entity.ExaminationResultEntity;
import io.renren.entity.SysUserEntity;
import io.renren.pojo.ExaminationResultPOJO;
import io.renren.service.ExaminationResultService;
import io.renren.utils.ExcelReaderJXL;



@Service("examinationResultService")
public class ExaminationResultServiceImpl implements ExaminationResultService {
	@Autowired
	private ExaminationResultDao examinationResultDao;
	
	@Override
	public ExaminationResultPOJO queryObject(Map<String, Object> map){
		return  examinationResultDao.queryObject(map);
	}
	
	@Override
	public List<ExaminationResultPOJO> queryList(Map<String, Object> map){
		return examinationResultDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return examinationResultDao.queryTotal(map);
	}
	
	@Override
	public void save(ExaminationResultEntity examinationResult){
		examinationResult.setCreateTime(new Date());
		examinationResultDao.save(examinationResult);
	}
	
	@Override
	public void update(ExaminationResultEntity examinationResult){
		examinationResult.setModifyTime(new Date());
		examinationResultDao.update(examinationResult);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		examinationResultDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		examinationResultDao.deleteBatch(map);
	}

	@Override
	public int getRegistrationNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return examinationResultDao.getRegistrationNum(map);
	}

	@Override
	@Transactional
	public int importData(MultipartFile file,SysUserEntity userInfo) throws Exception {
		FileInputStream fio = (FileInputStream) file.getInputStream();
		List<String[]> dataList = ExcelReaderJXL.readExcel(fio);
		int num = 0;
		for(int i = 1 ; i < dataList.size() ; i++){
			String[] array = dataList.get(i);
			
			ExaminationResultEntity examinationResult = new ExaminationResultEntity();
			
			String userId = array[ExaminationEnum.userId.getIndex()];
			String registrationNo = array[ExaminationEnum.registrationNo.getIndex()];
			String score = array[ExaminationEnum.score.getIndex()];
			String examType = array[ExaminationEnum.examType.getIndex()];
			
			if(StringUtils.isBlank(registrationNo) && StringUtils.isBlank(userId) 
					&& StringUtils.isBlank(score) && StringUtils.isBlank(examType))
				continue;
			
			if(StringUtils.isBlank(registrationNo)){ 
				throw new Exception("第" + i + "行，报考单号不能为空");
			}
			if(StringUtils.isBlank(userId) || !checkOut(userId)){
				throw new Exception("第" + i + "行，学员Id不能为空,且必须是数字");
			} 
			if(StringUtils.isBlank(score) || !checkOut(score)){
				throw new Exception("第" + i + "行，统考成绩不能为空,且必须是数字");
			}
			if(StringUtils.isBlank(examType)){
				throw new Exception("第" + i + "行，成绩类型不能为空");
			}
			userId = userId.trim();
			registrationNo = registrationNo.trim();
			score = score.trim();
			examType = examType.trim();
			
			Map<String,Object> map = new HashMap<>();
			map.put("registrationNo", registrationNo);
			List<ExaminationResultPOJO> list = examinationResultDao.queryList(map);
			
			if(list.size() == 0 ){
				throw new Exception("第" + i + "行，找不到对应的报考单号");
			}
			examinationResult.setRegistrationId(list.get(0).getRegistrationId());	
			examinationResult.setId(list.get(0).getExamId());
			examinationResult.setUserId(Long.valueOf(userId));
			examinationResult.setScore(Integer.valueOf(score));
			
			if("非补考".equals(examType)){
				examinationResult.setExamType(0);
			}else if("补考".equals(examType)){
				examinationResult.setExamType(1);
			}else{
				throw new Exception("第" + i + "行，成绩类型必须为：非补考/补考");
			}
			if(examinationResult.getId() == null){
				if(userInfo.getUserId() != null){
					examinationResult.setCreater(userInfo.getUserId());
				}
				this.save(examinationResult);
			}else{
				this.update(examinationResult);
			}
			num++;
		}
		 return num;
	}
	
	private boolean checkOut(String value){
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    Matcher matcher = pattern.matcher(value);
	    return matcher.matches();
	}
}
