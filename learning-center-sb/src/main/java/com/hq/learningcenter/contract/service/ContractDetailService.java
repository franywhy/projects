package com.hq.learningcenter.contract.service;

import com.hq.learningcenter.contract.pojo.ContractDetailPOJO;

import java.util.List;

/**
 * @auther linchaokai
 * @description 收支项目接口
 * @date 2018/6/5
 */
public interface ContractDetailService {
    /**
     * 协议记录字表-收支项目列表
     * @author linchaokai
     * @date 2018/6/6 16:42
     * @param  * @param null
     * @return
     */
    List<ContractDetailPOJO> queryPojoList(Long contractRecordId);
}
