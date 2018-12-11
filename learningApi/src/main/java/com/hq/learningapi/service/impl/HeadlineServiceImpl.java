package com.hq.learningapi.service.impl;

import com.hq.learningapi.config.LocalConfigEntity;
import com.hq.learningapi.dao.HeadlineDao;
import com.hq.learningapi.pojo.HeadlinePOJO;
import com.hq.learningapi.service.HeadlineService;
import com.hq.learningapi.service.LikeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
@Service
public class HeadlineServiceImpl implements HeadlineService {

    @Autowired
    private HeadlineDao headlineDao;
    @Autowired
    private LocalConfigEntity config;
    @Autowired
    private LikeUserService likeUserService;

    @Override
    public List<HeadlinePOJO> queryPojoList(Map<String, Object> map) {
        return headlineDao.queryPojoList(map);
    }

    @Override
    public HeadlinePOJO queryPojoObject(Long headlineId) {
        headlineDao.addReadNumber(headlineId);
        return headlineDao.queryPojoObject(headlineId);
    }

    @Override
    public void addCommentNumber(Long headlineId) {
        headlineDao.addCommentNumber(headlineId);
    }

    @Override
    public HeadlinePOJO queryPojoObjectForNumber(Long headlineId) {
        return headlineDao.queryPojoObject(headlineId);
    }

    @Override
    public List<Map<String,Object>> queryMapList(Map<String, Object> map, String token) {
        List<Map<String,Object>> headlineList = headlineDao.queryMapList(map);
        if(null != headlineList && headlineList.size() > 0) {
            for (Map<String,Object> headlineMap : headlineList) {
                //内容类型  0：视频，1：语音，2：观点，3：文章
                String contentTypeText;
                int contentType = (int)headlineMap.get("contentType");
                switch (contentType) {
                    case 0:contentTypeText="视频";
                        break;
                    case 1:contentTypeText="语音";
                        break;
                    case 2:contentTypeText="观点";
                        break;
                    case 3:contentTypeText="文章";
                        break;
                    default:contentTypeText="";
                }
                headlineMap.put("contentTypeText",contentTypeText);
                String detailsUrl = null;
                Long pkTotalNumber = 0L;
                Long headlineId = Long.parseLong(headlineMap.get("headlineId").toString());
                detailsUrl = "/entry?headlineId=" + headlineId + "&token=" + token;
                pkTotalNumber = likeUserService.queryTotal(headlineId, -1);
                Timestamp timestamp = (Timestamp) headlineMap.get("creationTime");
                double hours = (double) (System.currentTimeMillis() - timestamp.getTime()) / 3600 / 1000;
                if (hours < 1) {
                    headlineMap.put("pushTime", (int) (hours * 60) + "分钟前");
                } else if (hours < 24) {
                    headlineMap.put("pushTime", (int) hours + "小时前");
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
                    headlineMap.put("pushTime", sdf.format(timestamp));
                }
                headlineMap.put("detailsUrl", config.getApph5Url() + detailsUrl);
                headlineMap.put("pkTotalNumber", pkTotalNumber);
            }
        }
        return headlineList;
    }
}
