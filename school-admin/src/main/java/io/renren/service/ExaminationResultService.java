package io.renren.service;

import io.renren.entity.ExaminationResultEntity;
import io.renren.entity.SysUserEntity;
import io.renren.pojo.ExaminationResultPOJO;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

/**
 * 统考成绩
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-08-07 09:21:13
 */
public interface ExaminationResultService {
	
		
	ExaminationResultPOJO queryObject(Map<String, Object> map);
	
	List<ExaminationResultPOJO> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(ExaminationResultEntity examinationResult);
	
	void update(ExaminationResultEntity examinationResult);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
	public int getRegistrationNum(Map<String, Object> map);
	
	public int importData(MultipartFile file,SysUserEntity userInfo) throws Exception;	
}
