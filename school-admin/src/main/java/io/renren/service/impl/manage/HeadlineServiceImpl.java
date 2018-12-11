package io.renren.service.impl.manage;

import io.renren.dao.manage.HeadlineDao;
import io.renren.entity.manage.Headline;
import io.renren.pojo.manage.HeadlinePOJO;
import io.renren.service.manage.HeadlineService;
import io.renren.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
@Service
public class HeadlineServiceImpl implements HeadlineService {

    @Autowired
    private HeadlineDao headlineDao;

    @Override
    public int queryTotal(Map<String, Object> map) {
        return headlineDao.queryTotal(map);
    }

    @Override
    public List<HeadlinePOJO> queryPojoList(Map<String, Object> map) {
        return headlineDao.queryPojoList(map);
    }

    @Override
    public HeadlinePOJO queryPojoObject(Map<String, Object> map) {
        return headlineDao.queryPojoObject(map);
    }

    @Override
    public void save(Headline headline) {
        headline.setReadNumber(0);
        headline.setCommentNumber(0);
        //创建时间
        headline.setCreationTime(new Date());
        //修改时间
        headline.setModifiedTime(headline.getCreationTime());
        //初始化上架状态
        headline.setAppStatus(0);
        //初始化推送状态
        headline.setPushStatus(Constant.Push.NOT.getValue());
        headline.setDr(Constant.DR.NORMAL.getValue());
        headlineDao.insert(headline);
    }

    @Override
    public void update(Headline headline) {
        headline.setDr(Constant.DR.NORMAL.getValue());
        headlineDao.updateByPrimaryKey(headline);
    }

    @Override
    public void deleteBatch(Map<String, Object> map) {
        headlineDao.deleteBatch(map);
    }

    @Override
    public void pause(Long[] headlineIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("list", headlineIds);
        map.put("appStatus", Constant.Status.PAUSE.getValue());
        headlineDao.updateBatch(map);
    }

    @Override
    public void resume(Long[] headlineIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("list", headlineIds);
        map.put("appStatus", Constant.Status.RESUME.getValue());
        headlineDao.updateBatch(map);
    }

    @Override
    public void updateAppStatus(Long[] headlineIds, int appStatus) {
        Map<String, Object> map = new HashMap<>();
        map.put("list", headlineIds);
        map.put("appStatus", appStatus);
        headlineDao.updateBatch(map);
    }

    @Override
    public void updatePushStatus(Long headlineId, int pushStatus, Date pushTime, String pushFindMsgId, int appStatus) {
        Map<String, Object> map = new HashMap<>();
        map.put("headlineId", headlineId);
        map.put("pushStatus", pushStatus);
        map.put("pushTime", pushTime);
        map.put("pushFindMsgId", pushFindMsgId);
        map.put("appStatus", appStatus);
        headlineDao.updatePushStatus(map);
    }

    @Override
    public Map<String, Object> queryMsgMap(Long headlineId) {
        Map<String, Object> map = headlineDao.queryMsgMap(headlineId);
        //内容类型  0：视频，1：语音，2：观点，3：文章
        String contentTypeText;
        int contentType = (int)map.get("contentType");
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
        map.put("contentTypeText",contentTypeText);
        return map;
    }
}
