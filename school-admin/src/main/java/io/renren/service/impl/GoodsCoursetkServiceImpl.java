package io.renren.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.GoodsCoursetkDao;
import io.renren.service.GoodsCoursetkService;
@Service("goodsCoursetkService")
public class GoodsCoursetkServiceImpl implements GoodsCoursetkService {
	@Autowired
	private GoodsCoursetkDao goodsCoursetkDao;

	@Override
	public int queryTotalByCommodityId(Long commodityId) {
		return this.goodsCoursetkDao.queryTotalByCommodityId(commodityId);
	}

	@Override
	public List<String> queryCodeListByCommodityId(Object object) {
		return this.goodsCoursetkDao.queryCodeListByCommodityId(object);
	}

	@Override
	public String tkCourseCode(List<String> codeList) {
		String result = null;
		if(null != codeList && !codeList.isEmpty()){
			StringBuffer sbf = new StringBuffer();
			for (String string : codeList) {
				sbf.append(string + ",");
			}
			result = sbf.substring(0, sbf.length() - 1);
		}
		return result;
	}

}
