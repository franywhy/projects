package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import io.renren.dao.ClassGoodsDeptsDao;
import io.renren.entity.ClassGoodsDeptsEntity;
import io.renren.pojo.ClassGoodsDeptsPOJO;
import io.renren.service.ClassGoodsDeptsService;
import io.renren.utils.Constant;



@Service("classGoodsDeptsService")
public class ClassGoodsDeptsServiceImpl implements ClassGoodsDeptsService {
	@Autowired
	private ClassGoodsDeptsDao classGoodsDeptsDao;
	
	@Override
	public ClassGoodsDeptsEntity queryObject(Map<String, Object> map){
		return classGoodsDeptsDao.queryObject(map);
	}
	
	@Override
	public List<ClassGoodsDeptsEntity> queryList(Map<String, Object> map){
		return classGoodsDeptsDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return classGoodsDeptsDao.queryTotal(map);
	}
	
	@Override
	public void save(ClassGoodsDeptsEntity classGoodsDepts){
		classGoodsDeptsDao.save(classGoodsDepts);
	}
	
	@Override
	public void update(ClassGoodsDeptsEntity classGoodsDepts){
		//修改时间
		classGoodsDepts.setModifyTime(new Date());
		classGoodsDeptsDao.update(classGoodsDepts);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		classGoodsDeptsDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		classGoodsDeptsDao.deleteBatch(map);
	}

	@Override
	public List<ClassGoodsDeptsPOJO> queryListPOJO(Map<String, Object> map) {
		return this.classGoodsDeptsDao.queryListPOJO(map);
	}

	@Override
	public ClassGoodsDeptsPOJO queryObjectPOJO(Map<String, Object> map) {
		return this.classGoodsDeptsDao.queryObjectPOJO(map);
	}

	@Override
	public int queryNumByGoodIdAndDeptId(Long classId, Long goodId, Long deptId) {
		return this.classGoodsDeptsDao.queryNumByGoodIdAndDeptId(classId, goodId, deptId);
	}

	@Override
	public List<ClassGoodsDeptsEntity> queryClassList(Map<String, Object> classMap) {
		return this.classGoodsDeptsDao.queryClassList(classMap);
	}

	@Override
	public int queryNumBydeptIdAndGoodId(Long goodId, Long deptId) {
		return this.classGoodsDeptsDao.queryNumBydeptIdAndGoodId(goodId, deptId);
	}

	@Override
	public void deleteById(Long id) {
		this.classGoodsDeptsDao.deleteById(id);
	}
	
	
}
