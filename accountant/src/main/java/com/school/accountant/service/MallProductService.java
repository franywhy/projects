package com.school.accountant.service;

import java.util.List;

public interface MallProductService {

	/**
	 * 查询所有实训账套
	 * @return
	 */
	List queryAllProducts();
	
	/**
	 * 查询某学员已开通的账套
	 * @return
	 */
	List queryUserProducts(String loginName);
}
