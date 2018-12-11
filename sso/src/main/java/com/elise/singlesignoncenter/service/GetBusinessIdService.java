package com.elise.singlesignoncenter.service;

/**
 * @author shihongjie
 */
public interface GetBusinessIdService {
    /**
     * 获取产品线ID
     *
     * @param token
     * @return
     */
    String getBusinessId(String token);
}
