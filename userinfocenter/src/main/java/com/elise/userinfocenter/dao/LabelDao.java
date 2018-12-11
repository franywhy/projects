package com.elise.userinfocenter.dao;

import com.elise.userinfocenter.entity.LabelEntity;
import com.elise.userinfocenter.pojo.LabelPOJO;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by DL on 2017/12/29.
 */
@Repository
public interface LabelDao {
    List<LabelPOJO> queryLabelList(Long productId);

    int isExistLabel(Long userId);

    void delete(Long userId);

    void save(LabelEntity entity);

    List<LabelPOJO> getLabel(Long userId);

    List<LabelPOJO> queryLabelListByParentId(Long parentId);
}
