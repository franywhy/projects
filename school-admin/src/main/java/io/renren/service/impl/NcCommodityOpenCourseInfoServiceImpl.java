package io.renren.service.impl;

import io.renren.dao.CourseOliveAuthorityDao;
import io.renren.dao.NcCommodityOpenCourseInfoDao;
import io.renren.entity.NcCommodityOpenCourseInfoEntity;
import io.renren.pojo.NcCommodityOpenCourseInfoPOJO;
import io.renren.service.NcCommodityOpenCourseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("ncCommodityOpenCourseInfoService")
public class NcCommodityOpenCourseInfoServiceImpl implements NcCommodityOpenCourseInfoService {
    @Autowired
    private NcCommodityOpenCourseInfoDao ncCommodityOpenCourseInfoDao;
    @Autowired
    private CourseOliveAuthorityDao courseOliveAuthorityDao;

    @Override
    public List<NcCommodityOpenCourseInfoPOJO> queryList(Map<String, Object> params) {
        List<NcCommodityOpenCourseInfoPOJO> resultList = ncCommodityOpenCourseInfoDao.queryPOJOList(params);

        for (NcCommodityOpenCourseInfoPOJO entity:resultList) {
            String authorityName = courseOliveAuthorityDao.queryObjectByAuthorityId(entity.getAuthorityId()).getAuthorityName();
            entity.setAuthorityName(authorityName);
        }
        return resultList;
    }

    @Override
    public int queryTotal(Map<String, Object> params) {
        return ncCommodityOpenCourseInfoDao.queryTotal(params);
    }

    @Override
    public void insert(NcCommodityOpenCourseInfoEntity ncCommodityOpenCourseInfoEntity) {
        ncCommodityOpenCourseInfoDao.insert(ncCommodityOpenCourseInfoEntity);
    }

    @Override
    public int update(NcCommodityOpenCourseInfoEntity ncCommodityOpenCourseInfoEntity) {
        return ncCommodityOpenCourseInfoDao.update(ncCommodityOpenCourseInfoEntity);
    }

    @Override
    public void deleteBatch(Map<String, Object> map) {
        ncCommodityOpenCourseInfoDao.deleteBatch(map);
    }

    @Override
    public NcCommodityOpenCourseInfoEntity queryObject(Integer id) {
        return ncCommodityOpenCourseInfoDao.queryObject(id);
    }

    @Override
    public NcCommodityOpenCourseInfoPOJO queryPOJOObject(Integer id) {
        NcCommodityOpenCourseInfoPOJO pojo = ncCommodityOpenCourseInfoDao.queryPOJOObject(id);
        String authorityName = courseOliveAuthorityDao.queryObjectByAuthorityId(pojo.getAuthorityId()).getAuthorityName();
        pojo.setAuthorityName(authorityName);
        return pojo;
    }

}
