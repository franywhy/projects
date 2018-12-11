package io.renren.modules.job.service.impl;

import io.micrometer.core.instrument.Meter;
import io.renren.modules.job.dao.PushClassplanDetailRemindDao;
import io.renren.modules.job.entity.PushClassplanDetailRemindEntity;
import io.renren.modules.job.service.PushClassplanDetailRemindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;



@Service("pushClassplanDetailRemindService")
public class PushClassplanDetailRemindServiceImpl implements PushClassplanDetailRemindService {
	@Autowired
	private PushClassplanDetailRemindDao pushClassplanDetailRemindDao;


    @Override
    public List<PushClassplanDetailRemindEntity> queryListByTs(String ts) {
        return pushClassplanDetailRemindDao.queryListByTs(ts);
    }

    @Override
    public void updateMsgId(Integer id, String msgId) {
        PushClassplanDetailRemindEntity entity = new PushClassplanDetailRemindEntity();
        entity.setId(id);
        entity.setMsgId(msgId);
        pushClassplanDetailRemindDao.update(entity);
    }
}
