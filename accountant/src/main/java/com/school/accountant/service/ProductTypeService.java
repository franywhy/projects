package com.school.accountant.service;

import java.util.List;

import com.school.accountant.entity.ProductType;


public interface ProductTypeService {

	List<ProductType> firstLevel();//课程 - 一级分类
	
	List<ProductType> secondLevel(List<Integer> fatherIds);//课程 - 二级分类
	
}
