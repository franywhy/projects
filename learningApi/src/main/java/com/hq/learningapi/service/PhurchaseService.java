package com.hq.learningapi.service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by DL on 2018/9/11.
 */
public interface PhurchaseService {

    String phurchase(HttpServletRequest request, String token, String ncId, Long goodId, double price);
}
