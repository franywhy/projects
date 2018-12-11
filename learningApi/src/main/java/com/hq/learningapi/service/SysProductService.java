package com.hq.learningapi.service;

import java.util.Map;

/**
 * Created by Administrator on 2018/3/29 0029.
 */
public interface SysProductService {

    Map<String,Object> queryByProductId(Long productId);
}
