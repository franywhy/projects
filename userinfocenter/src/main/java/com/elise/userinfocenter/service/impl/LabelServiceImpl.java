package com.elise.userinfocenter.service.impl;

import com.elise.userinfocenter.dao.LabelDao;
import com.elise.userinfocenter.entity.LabelEntity;
import com.elise.userinfocenter.pojo.LabelPOJO;
import com.elise.userinfocenter.service.LabelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by DL on 2017/12/29.
 */
@Service("labelService")
public class LabelServiceImpl implements LabelService {
    @Autowired
    private LabelDao labelDao;
    @Override
    public List<LabelPOJO> queryLabelList(Long productId) {
        List<LabelPOJO> labelPOJOList = labelDao.queryLabelList(productId);
        if (labelPOJOList != null && labelPOJOList.size() > 0){
            for (LabelPOJO labelPOJO : labelPOJOList) {
                List<LabelPOJO> childrenLabelPOJOList = labelDao.queryLabelListByParentId(labelPOJO.getId());
                labelPOJO.setChildrenLabelList(childrenLabelPOJOList);
            }
        }
        return labelPOJOList;
    }

    @Override
    public boolean isExistLabel(Long userId) {
        return labelDao.isExistLabel(userId) > 0;
    }

    @Override
    public void delete(Long userId) {
        labelDao.delete(userId);
    }

    @Override
    public void save(Long userId, String labelIdString) {
        String[] labelIdList = labelIdString.split(",");
        for (String labelId : labelIdList) {
            LabelEntity entity = new LabelEntity();
            entity.setUserId(userId);
            entity.setLabelId(Long.valueOf(labelId));
            labelDao.save(entity);
        }
    }

    @Override
    public List<LabelPOJO> getLabel(Long userId) {
        return labelDao.getLabel(userId);
    }
}
