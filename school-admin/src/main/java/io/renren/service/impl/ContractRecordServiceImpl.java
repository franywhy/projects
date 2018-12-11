package io.renren.service.impl;

import io.renren.dao.ContractDetailDao;
import io.renren.dao.ContractRecordDao;
import io.renren.dao.ContractTemplateDao;
import io.renren.entity.*;
import io.renren.service.ContractRecordService;
import io.renren.service.ContractService;
import io.renren.service.MallClassTypeService;
import io.renren.utils.LocationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContractRecordServiceImpl implements ContractRecordService {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ContractRecordDao contractRecordDao;
    @Autowired
    private ContractDetailDao contractDetailDao;
    @Autowired
    private ContractService contractService;

    @Autowired
    private ContractTemplateDao contractTemplateDao;

    @Autowired
    private MallClassTypeService mallClassTypeService;

    @Override
    public List<ContractRecord> queryList(Map<String, Object> map) {
        return contractRecordDao.queryList(map);
    }

    @Override
    public int setStatus(Long contractId) {
        if (contractId != null && contractId > 0) {
            return contractRecordDao.setStatus(contractId);
        } else {
            return 999;
        }

    }

    @Override
    public void setSignerId(ContractRecord cd) {

        if (cd == null) return;

        String phoneNo = cd.getMobile();
        String phoneRegion = String.valueOf(LocationUtil.getMobileLocation(cd.getMobile()));
        String certifyNum = cd.getIdCard();
        String identityRegion = String.valueOf(LocationUtil.getIdCardLocation(cd.getIdCard()));
        String username = cd.getRealName();
        try {
            cd.setSignerId(contractService.saveUser(username, identityRegion, certifyNum, phoneRegion, phoneNo));
        } catch (Exception e) {
            logger.error("set signerId has error!, Contractrecord is :{},error message is :{}0", cd.toString(), e);
            cd.setSignerId(0l);
        }

    }

    @Override
    public void updateSigner(ContractRecord cd) {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveContractRecord(ContractRecord cd) {
        if (cd == null) return;


        List<ContractRecord> list = this.queryByOrderId(String.valueOf(cd.getOrderId()));
        if (list != null && list.size() > 0) {
            return;
        }


        int companyId = getCompanyId(cd);

        if (companyId == 0) {
            cd.setDr(2);
            cd.setCompanyId(0);
        } else {
            cd.setCompanyId(companyId);
            cd.setDr(0);
        }

        try {
            cd.setCreateTime(new Date());

            contractRecordDao.savebackId(cd);
        } catch (Exception e) {
            logger.error("Save ContractRecord has error!, Contractrecord is :{},error message is : {}", cd.toString(), e);
        }


        if (cd.getContractDetail() != null && cd.getContractDetail().size() > 0) {
            for (ContractDetail c : cd.getContractDetail()) {
                c.setContractRecordId(cd.getId());
                try {
                    contractDetailDao.save(c);
                } catch (Exception e) {
                    logger.error("Save getContractDetail has error!, getContractDetail is :{},error message is : {}", c.toString(), e);
                }
            }
        }

    }

    @Override
    public void CheckContractRecord(ContractRecord cd) {


        int companyId = getCompanyId(cd);

        if (companyId == 0) {
            cd.setDr(2);
            cd.setCompanyId(0);
        }


        List<ContractRecord> list = this.queryByIdcard(cd.getIdCard());

        if (list == null || list.size() == 0) {//如果按身份证都取不到的话 说明这个是新学员 直接生成
            setSignerId(cd);
            saveContractRecord(cd);
        } else {//能取到身份证的 有三种情况:1.以前有报名了 现在报名的不是同一个订单 要生生成订单, 2.就是本单的 修改了电话号码或者姓名 3.姓名\电话没有变更的
            boolean isExist = false;
            long signerId = 0;
            for (ContractRecord c : list) {


                //判断orderid和学员名称和电话是否一致 一致说明是重复的单
                if (cd.getOrderId() == c.getOrderId() && cd.getRealName().equals(c.getRealName()) && cd.getMobile().equals(cd.getMobile())) {


                    isExist = true;
                }

                //只要名字和电话相同且singerid不为空的话 则取得signerid
                if (cd.getRealName().equals(c.getRealName()) && cd.getMobile().equals(c.getMobile()) && c.getSignerId() != null) {

                    signerId = c.getSignerId();
                }


            }
            if (isExist) { //电话名订单和名字都没有变 所以不需要再保存了 直接跳出
                return;
            }


            if (signerId == 0) {
                setSignerId(cd);
            } else {
                cd.setSignerId(signerId);
            }


            saveContractRecord(cd);


        }


    }


    @Override
    public void ContractRecordCheckNew(ContractRecord old_Cr, ContractRecord new_Cr) {
        //定义两个时间段的
        long contractRecordId = old_Cr.getId();
        new_Cr.setTemplateId(old_Cr.getTemplateId());
        long oldSyncTime = old_Cr.getSyncTime() == 0 ? 0 : old_Cr.getSyncTime();
        long newSyncTime = new_Cr.getSyncTime() == 0 ? new Date().getTime() : new_Cr.getSyncTime();


        if (newSyncTime > oldSyncTime) {

            int companyId = getCompanyId(new_Cr);
            if (companyId == 0) {
                new_Cr.setCompanyId(0);
                new_Cr.setDr(2);
            } else {
                new_Cr.setCompanyId(companyId);
                new_Cr.setDr(0);
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("contractRecordId", old_Cr.getId());
            //根据主表取子表
            List<ContractDetail> old_cr_detail = contractDetailDao.queryList(map);
            List<ContractDetail> new_cr_detail = new_Cr.getContractDetail();


            if (old_cr_detail != null && old_cr_detail.size() > 0) {
                for (int y = 0; y < old_cr_detail.size(); y++) {
                    old_cr_detail.get(y).setDr(1);

                }
                if (new_cr_detail != null && new_cr_detail.size() > 0) {
                    for (int i = 0; i < new_cr_detail.size(); i++) {
                        if (i >= old_cr_detail.size()) {
                            ContractDetail n = new_cr_detail.get(i);
                            n.setContractRecordId(old_Cr.getId());
                            contractDetailDao.save(n);

                        } else {
                            ContractDetail o = old_cr_detail.get(i);
                            ContractDetail n = new_cr_detail.get(i);
                            o.setDcost(n.getDcost());
                            o.setDdiscount(n.getDdiscount());
                            o.setDnshoulddcost(n.getDnshoulddcost());
                            o.setSubjectName(n.getSubjectName());
                            o.setDr(0);
                            contractDetailDao.update(o);
                        }


                    }
                    if (new_cr_detail.size() < old_cr_detail.size()) {
                        for (int y = 0; y < old_cr_detail.size(); y++) {
                            contractDetailDao.update(old_cr_detail.get(y));

                        }
                    }

                } else {//如果新的没有子表则删除全部
                    for (ContractDetail c : old_cr_detail) {
                        c.setDr(1);
                        contractDetailDao.update(c);
                    }
                }


            }


            old_Cr.setNew(old_Cr, new_Cr);
            contractRecordDao.update(old_Cr);
        }

        // TODO Auto-generated method stub

    }

    @Override
    public void changeContractRecord(String oldNcid, long oldCdid, ContractRecord newCd) {
        contractRecordDao.save(newCd);


    }

    @Override
    public void setLocation(ContractRecord cd) {
        cd.setIdcardLocation(LocationUtil.getIdCardLocation(cd.getIdCard()));
        cd.setMobileLocation(LocationUtil.getMobileLocation(cd.getMobile()));
    }

    @Override
    public List<ContractRecord> queryByIdcard(String idcard) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("idCard", idcard);
        return queryList(map);
    }

    @Override
    public List<ContractRecord> queryByOrderId(String orderId) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderId", orderId);
        map.put("dr2", 2);
        return queryList(map);
    }


    @Override
    public boolean CheckContractRecordByNcid(String ncid, ContractRecord cd) {
        boolean isexist = false;
        try {
            String studentName = cd.getRealName();
            String mobile = cd.getMobile();
            String idcard = cd.getIdCard();

            Map<String, Object> map = new HashMap<String, Object>();

            map.put("ncId", ncid);
            map.put("dr2", 2);
            List<ContractRecord> list = queryList(map);//根据ncid取得协议记录
            if (list == null || list.size() == 0) {
                return false;
            }

            for (ContractRecord c : list) {

                if (!idcard.equals(c.getIdCard()) && c.getStatus() == 0) {  //如果身份证不一致的话  删除协议记录并重新生成新的
                    long order_id = c.getOrderId();
                    long user_id = c.getUserId();
                    long temp_id = c.getTemplateId();
                    long product_id = c.getProductId();
                    //long signer_id= c.getSignerId();

                    ContractRecordDelete(c);//软删除主子表
                    //	cd.setSignerId(signer_id);
                    cd.setOrderId(order_id);
                    cd.setTemplateId(temp_id);
                    cd.setUserId(user_id);
                    cd.setProductId(product_id);
                    CheckContractRecord(cd);//重新检测生成新的单据
                    return true;
                }


                if (!studentName.equals(c.getRealName()) || !mobile.equals(mobile) && c.getStatus() == 0) {


                    String phoneRegion = String.valueOf(LocationUtil.getMobileLocation(cd.getMobile()));
                    try {
                        contractService.updateUser(studentName, phoneRegion, mobile);
                    } catch (Exception e) {

                        logger.error("UpdateUser  has error!,studentname is {},phoneRegion is {},mobile is {},idcard is {},error Message is {}  ",
                                studentName, phoneRegion, mobile, c.getIdCard(), e
                        );
                    }
                    try {
                        c.setMobile(mobile);
                        c.setRealName(studentName);
                        contractRecordDao.update(c);
                    } catch (Exception e) {

                        logger.error("update contractRecord  has error!,studentname is {},phoneRegion is {},mobile is {},idcard is {},error Message is {}  ",
                                studentName, phoneRegion, mobile, c.getIdCard(), e
                        );
                    }
                    //根据ts变更判断是否有变化 如果有则update新值


                    ContractRecordCheckNew(c, cd);

                    return true;
                }

                if (studentName.equals(c.getRealName()) || mobile.equals(mobile)) {
                    isexist = true;
                }
                if (c.getStatus() == 0) {

                    ContractRecordCheckNew(c, cd);
                }


            }

        } catch (Exception e) {
            logger.error("UpdateUser  has error!, ContractRecord is {},Error Message is {}",
                    cd, e
            );
        }

        return isexist;
    }

    @Override
    public void ContractRecordDelete(ContractRecord cd) {
        try {
            contractRecordDao.setDr(cd);
        } catch (Exception e) {
            logger.error("Set ContractRecord to dr=1 has error!, ContractRecord is :{},error message is : {}", cd.toString(), e.getMessage());

        }
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("contractRecordId", cd.getId());

        List<ContractDetail> list = contractDetailDao.queryList(map);
        for (ContractDetail c : list) {
            try {
                contractDetailDao.setDr(c);
            } catch (Exception e) {
                logger.error("Set getContractDetail to dr=1 has error!, getContractDetail is :{},error message is : {}", c.toString(), e);

            }
        }

    }

    @Override
    public void ContractRecordDeleteByOrderId(Long orderId) {
        if (orderId != null && orderId > 0) {

            try {

                Long[] orderIds = new Long[1];
                orderIds[0] = orderId;
                ContractRecordDeleteBatch(orderIds);
                logger.info("closeOrderDelContractRecord ... orderId={}", orderId);
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("closeOrderDelContractRecord has Errors. ... message={}", e);
            }
        }
    }


    @Override
    public void ContractRecordDeleteBatch(Long[] orderIds) {
        for (long orderId : orderIds) {
            List<ContractRecord> cds = queryByOrderId(String.valueOf(orderId));
            for (ContractRecord cd : cds) {

                ContractRecordDelete(cd);
            }
        }

    }

    @Override
    public void ContractRecordChangeInfo(OrderMessageConsumerEntity orderMessageConsumerEntity,
                                         MallOrderEntity mallOrderEntity) {
        //报读班型改为从蓝鲸处取得
        MallClassTypeEntity classType = mallClassTypeService.queryObject(mallOrderEntity.getClassTypeId());
        if (classType != null && classType.getClasstypeName() != null) {
            orderMessageConsumerEntity.getContractHead().setCourseName(classType.getClasstypeName());
        }


        //支付方式改为从蓝鲸的支付方式
        //1支付宝 2 微信
        if (mallOrderEntity.getPayType() == null) {

        } else if (mallOrderEntity.getPayType() == 1) {
            orderMessageConsumerEntity.getContractHead().setPayName("支付宝");
        } else if (mallOrderEntity.getPayType() == 2) {
            orderMessageConsumerEntity.getContractHead().setPayName("微信");
        }

    }

    @Override
    public void updateByContract(ContractRecord cd) {
        contractRecordDao.updateByContract(cd);
    }

    @Override
    public boolean ContractCompanyId(String companyName, Long contractTemplateId) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", contractTemplateId);
        map.put("companyName", companyName);
        List<ContractTemplateEntity> list = contractTemplateDao.queryList(map);
        if (list != null && list.size() > 0) { //如果能查到并且数量大于0的才算是有协议的
            return true;
        }
        return false;

    }

    public int getCompanyId(ContractRecord cd) {


        if (cd.getCompanyName() == null || cd.getTemplateId() == null) {
            logger.error("getCompanyId has error! CompanyName is null or TemplateId is null!, Contractrecord is :{} ", cd);
            return 0;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", cd.getTemplateId());
        map.put("companyName", cd.getCompanyName());


        List<ContractTemplateEntity> tempList = contractTemplateDao.queryList(map);
        if (tempList == null || tempList.size() == 0) { //没有找到的则为不存的公司的 则生成dr=2的协议记录
            logger.error("getCompanyId has error! CompanyName is not exist!, Contractrecord is :{} ", cd);
            return 0;
        } else {
            return tempList.get(0).getCompanyId();
        }
    }


}
