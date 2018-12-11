package com.school.accountant.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.school.accountant.dao.ProductTypeMapper;
import com.school.accountant.entity.ProductType;
import com.school.accountant.entity.ProductTypeExample;
import com.school.accountant.service.ProductTypeService;

@Service
public class ProductTypeServiceImpl implements ProductTypeService {

	@Autowired
	private ProductTypeMapper productTypeMapper;
	
	@Override
	public List<ProductType> firstLevel() {
		//一级分类
		ProductTypeExample example = new ProductTypeExample();
		example.createCriteria().andDrEqualTo(0).andFatheridEqualTo(0).andTypeidNotEqualTo(147);
		example.setOrderByClause("sortorder asc");
		return productTypeMapper.selectByExample(example);
	}

	@Override
	public List<ProductType> secondLevel(List<Integer> fatherIds) {
		//二级分类
		ProductTypeExample example = new ProductTypeExample();
		example.createCriteria().andDrEqualTo(0).andFatheridIn(fatherIds);
		example.setOrderByClause("sortorder asc");
		return productTypeMapper.selectByExample(example);
	}

}
