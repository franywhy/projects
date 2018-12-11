package io.renren.service.impl.manage;

import io.renren.dao.manage.AppCommodityMapper;
import io.renren.entity.manage.AppCommodity;
import io.renren.entity.manage.AppCommodityExample;
import io.renren.service.manage.AppCommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/4/24 0024.
 */
@Service
public class AppCommodityServiceImpl implements AppCommodityService {

    @Autowired
    private AppCommodityMapper appCommodityMapper;

    @Override
    public AppCommodity selectByExampleFetchOne(String ncCommodityId) {
        AppCommodityExample example = new AppCommodityExample();
        example.createCriteria().andNcCommodityIdEqualTo(ncCommodityId);
        return appCommodityMapper.selectByExampleFetchOne(example);
    }
}
