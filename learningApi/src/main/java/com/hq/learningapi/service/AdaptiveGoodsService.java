package com.hq.learningapi.service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by DL on 2018/10/26.
 */
public interface AdaptiveGoodsService {

    int isAdaptiveUsers(HttpServletRequest request, String token);
}
