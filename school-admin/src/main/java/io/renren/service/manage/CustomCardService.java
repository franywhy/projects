package io.renren.service.manage;

import io.renren.entity.manage.CustomCard;
import io.renren.pojo.manage.CustomCardPOJO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
public interface CustomCardService {

    int queryTotal(Map<String, Object> map);

    List<CustomCardPOJO> queryPojoList(Map<String, Object> map);

    CustomCardPOJO queryPojoObject(Map<String, Object> map);

    void save(CustomCard customCard);

    void update(CustomCard customCard);

    void deleteBatch(Map<String, Object> map);

    void pause(Long[] cardIds);

    void resume(Long[] cardIds);

    void updatePushStatus(Long cardId, int pushStatus, Date pushTime, String pushMsgId);

    Map<String, Object> queryMsgMap(Long cardId);
}
