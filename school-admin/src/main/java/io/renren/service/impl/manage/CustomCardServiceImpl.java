package io.renren.service.impl.manage;

import io.renren.dao.manage.CustomCardDao;
import io.renren.entity.manage.CustomCard;
import io.renren.pojo.manage.CustomCardPOJO;
import io.renren.service.manage.CustomCardService;
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
public class CustomCardServiceImpl implements CustomCardService {

    @Autowired
    private CustomCardDao customCardDao;
    
    @Override
    public int queryTotal(Map<String, Object> map) {
        return customCardDao.queryTotal(map);
    }

    @Override
    public List<CustomCardPOJO> queryPojoList(Map<String, Object> map) {
        return customCardDao.queryPojoList(map);
    }

    @Override
    public CustomCardPOJO queryPojoObject(Map<String, Object> map) {
        return customCardDao.queryPojoObject(map);
    }

    @Override
    public void save(CustomCard customCard) {
        //创建时间
        customCard.setCreationTime(new Date());
        //修改时间
        customCard.setModifiedTime(customCard.getCreationTime());
        //初始化上架状态
        customCard.setAppStatus(0);
        //初始化推送状态
        customCard.setPushStatus(Constant.Push.NOT.getValue());
        customCard.setDr(Constant.DR.NORMAL.getValue());
        customCardDao.insert(customCard);
    }

    @Override
    public void update(CustomCard customCard) {
        customCard.setDr(Constant.DR.NORMAL.getValue());
        customCardDao.updateByPrimaryKey(customCard);
    }

    @Override
    public void deleteBatch(Map<String, Object> map) {
        customCardDao.deleteBatch(map);
    }

    @Override
    public void pause(Long[] cardIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("list", cardIds);
        map.put("appStatus", Constant.Status.PAUSE.getValue());
        customCardDao.updateBatch(map);
    }

    @Override
    public void resume(Long[] cardIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("list", cardIds);
        map.put("appStatus", Constant.Status.RESUME.getValue());
        customCardDao.updateBatch(map);
    }

    @Override
    public void updatePushStatus(Long cardId, int pushStatus, Date pushTime, String pushMsgId) {
        Map<String, Object> map = new HashMap<>();
        map.put("cardId", cardId);
        map.put("pushStatus", pushStatus);
        map.put("pushTime", pushTime);
        map.put("pushMsgId", pushMsgId);
        customCardDao.updatePushStatus(map);
    }

    @Override
    public Map<String, Object> queryMsgMap(Long cardId) {
        return customCardDao.queryMsgMap(cardId);
    }
}
