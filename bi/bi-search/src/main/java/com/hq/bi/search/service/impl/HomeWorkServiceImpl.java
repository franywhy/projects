package com.hq.bi.search.service.impl;


import com.github.pagehelper.PageHelper;
import com.hq.bi.search.entity.HomeWorkStaticEntity;
import com.hq.bi.search.mapper.HomeWorkMapper;
import com.hq.bi.search.service.HomeWorkService;
import com.hq.bi.search.util.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * description:作业服务实现
 * @author: wl
 * @date 2018/12/15 11:59
 */
@Service
public class HomeWorkServiceImpl implements HomeWorkService {

    @Autowired
    private HomeWorkMapper homeWorkMapper;

    @Override
    public List<HomeWorkStaticEntity> getPreHomeWorkStaticList(PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getPageOrder());
        List<HomeWorkStaticEntity> list = homeWorkMapper.getPreHomeWorkStaticList(pageQuery);
        return list;
    }

    @Override
    public List<HomeWorkStaticEntity> getAfterClassStaticList(PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getPageOrder());
        List<HomeWorkStaticEntity> list = homeWorkMapper.getAfterClassStaticList(pageQuery);
        return list;
    }



}
