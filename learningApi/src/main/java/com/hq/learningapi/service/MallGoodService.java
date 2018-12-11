package com.hq.learningapi.service;

import com.hq.learningapi.pojo.MallGoodDetailPOJO;
import com.hq.learningapi.pojo.MallGoodPOJO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by DL on 2018/9/10.
 */
public interface MallGoodService {

    List<MallGoodPOJO> getMallGoodList();

    MallGoodDetailPOJO getMallGoodDetail(HttpServletRequest request, String token, Long goodId);
}
