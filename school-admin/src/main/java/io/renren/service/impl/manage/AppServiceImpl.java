package io.renren.service.impl.manage;

import io.renren.dao.manage.AppMapper;
import io.renren.entity.manage.App;
import io.renren.entity.manage.AppExample;
import io.renren.entity.manage.AppExample.Criteria;
import io.renren.service.manage.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppServiceImpl implements AppService {

	@Autowired
	private AppMapper appMapper;

	@Override
	public App findByCodeProvince(String code, String province) {
		AppExample example = new AppExample();
		Criteria criteria = example.createCriteria();
		criteria.andCodeEqualTo(code);
		if("A001".equals(code) || "A004".equals(code)) {
			criteria.andProvinceEqualTo("all");
		}
		/*else {
			criteria.andProvinceEqualTo(province);
		}*/
		criteria.andDrEqualTo(0);
		
		return appMapper.selectByExampleFetchOne(example);
	}

	@Override
	public App findByCode(String code) {
		AppExample example = new AppExample();
		example.createCriteria().andCodeEqualTo(code).andCourseidEqualTo("").andDrEqualTo(0);
		return appMapper.selectByExampleFetchOne(example);
	}

	@Override
	public List<App> findListByCode(String code) {
		AppExample example = new AppExample();
		Criteria criteria = example.createCriteria();
		criteria.andCodeEqualTo(code).andDrEqualTo(0);
		return appMapper.selectByExample(example);
	}
}
