package io.renren.service;

import java.util.List;
import java.util.Map;

import io.renren.entity.ContractRecord;
import io.renren.entity.MallOrderEntity;
import io.renren.entity.OrderMessageConsumerEntity;

public interface ContractRecordService {


    /**
     * 根据map去查协议记录表map的key有:username,ncId,mobile,idCard</code>
     *
     * @param <code>
     * @return
     * @author lintf
     * 2018年6月6日
     */
    List<ContractRecord> queryList(Map<String, Object> map);

    /**
     * 更新为已签
     *
     * @param contractId
     * @return
     * @author lintf
     * 2018年6月6日
     */
    int setStatus(Long contractId);

    /**
     * 取得注册用户ID
     *
     * @param cd
     * @author lintf
     * 2018年6月6日
     */


    void setLocation(ContractRecord cd);

    /**
     * 根据身份证取得全部记录
     *
     * @param idcard
     * @return
     * @author lintf
     * 2018年6月6日
     */
    List<ContractRecord> queryByIdcard(String idcard);


    void setSignerId(ContractRecord cd);

    /**
     * 当身份证没有变化,电话或者名字有变时请求接口更新用户信息
     *
     * @param cd
     * @author lintf
     * 2018年6月6日
     */
    void updateSigner(ContractRecord cd);


    /**
     * 当身份证有变化时,删除原来的协议记录主子表并生成新的
     *
     * @param oldNcid
     * @param oldCdid
     * @param newCd
     * @author lintf
     * 2018年6月6日
     */
    void changeContractRecord(String oldNcid, long oldCdid, ContractRecord newCd);

    /**
     * 生成记录
     *
     * @param cd
     * @author lintf
     * 2018年6月7日
     */
    void saveContractRecord(ContractRecord cd);

    /**
     * 判断并保存协议记录 先判断是否有signerid了
     *
     * @param cd
     * @author lintf
     * 2018年6月7日
     */
    void CheckContractRecord(ContractRecord cd);

    /**
     * 根据NCid判断是否存在协议记录了 如果存在则修改名字和电话  返回true 如果没有存在则返回false;
     *
     * @param ncid
     * @param cd
     * @return
     * @author lintf
     * 2018年6月8日
     */
    boolean CheckContractRecordByNcid(String ncid, ContractRecord cd);

    void ContractRecordDelete(ContractRecord cd);

    /**
     * 查询协议的订单是否已经生成过了
     *
     * @param orderId
     * @return
     */
    List<ContractRecord> queryByOrderId(String orderId);

    /**
     * 根据syncTime判断协议记录是信息是否变更  如果有变更则修改协议记录主子表
     *
     * @param old_Cr 旧的协议记录表 已经保存在数据库的
     * @param new_Cr 本次同步过来的协议信息
     * @author lintf
     * 2018年6月27日
     */
    void ContractRecordCheckNew(ContractRecord old_Cr, ContractRecord new_Cr);


    /**
     * 根据传入的oreder_id删除协议记录
     *
     * @param orderIds
     * @author lintf
     * 2018年7月2日
     */
    void ContractRecordDeleteBatch(Long[] orderIds);

    /**
     * 变更来源的信息
     *
     * @param orderMessageConsumerEntity
     * @param mallOrderEntity
     * @author lintf
     * 2018年9月19日
     */
    void ContractRecordChangeInfo(OrderMessageConsumerEntity orderMessageConsumerEntity, MallOrderEntity mallOrderEntity);

    /**
     * 根据订单id删除在线协议
     *
     * @param orderId
     * @author lintf
     * 2018年9月20日
     */
    void ContractRecordDeleteByOrderId(Long orderId);

    void updateByContract(ContractRecord cd);

    /**
     * 根据公司名和协议模板判断一下是否有开这个公司的协议模板
     *
     * @param companyName
     * @param contractTemplateId
     * @return true 有开通了这个公司的模板  false 没有开通这个公司的模板
     */
    boolean ContractCompanyId(String companyName, Long contractTemplateId);

}
 