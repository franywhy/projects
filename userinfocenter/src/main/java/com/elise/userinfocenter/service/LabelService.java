package com.elise.userinfocenter.service;

import com.elise.userinfocenter.entity.LabelEntity;
import com.elise.userinfocenter.pojo.LabelPOJO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by DL on 2017/12/29.
 */

public interface LabelService {
    /*
    展示标签列表
    @Description:
    @Author:DL
    @Date:10:43 2017/12/29
    @params:
     * @param null
    */
    List<LabelPOJO> queryLabelList(Long productId);

    //判断用户是否有标签
    boolean isExistLabel(Long userId);

    //删除用户标签
    void delete(Long userId);

    //保存用户标签
    void save(Long userId, String labelIdString);
    //获取用户的标签
    List<LabelPOJO> getLabel(Long userId);
}
