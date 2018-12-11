package io.renren.service;

import java.util.List;

public interface GoodsCoursetkService {
	int queryTotalByCommodityId(Long commodityId);

	List<String> queryCodeListByCommodityId(Object object);
	
	String tkCourseCode(List<String> codeList);
}
