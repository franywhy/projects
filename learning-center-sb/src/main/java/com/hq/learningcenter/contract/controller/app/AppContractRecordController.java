package com.hq.learningcenter.contract.controller.app;

import com.hq.learningcenter.config.LocalConfig;
import com.hq.learningcenter.contract.service.ContractRecordService;
import com.hq.learningcenter.school.controller.AbstractBaseController;
import com.hq.learningcenter.school.pojo.UserInfoPOJO;
import com.hq.learningcenter.school.web.model.WrappedResponse;
import com.hq.learningcenter.utils.BusinessIdUtils;
import com.hq.learningcenter.utils.DateUtils;
import com.hq.learningcenter.contract.pojo.ContractDetailPOJO;
import com.hq.learningcenter.contract.pojo.ContractRecordPOJO;
import com.hq.learningcenter.contract.service.ContractDetailService;
import com.hq.learningcenter.utils.LocationUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @auther linchaokai
 * @description 云合同接口
 * @date 2018/6/5 
 */
@CrossOrigin
@Controller
@RequestMapping("/learningCenter/app")
@Api(description = "云合同接口")
public class AppContractRecordController extends AbstractBaseController {
    @Autowired
    private ContractRecordService contractRecordService;

    @Autowired
    private ContractDetailService contractDetailService;
    @Autowired
    private LocalConfig localConfig;

    //平台云合同id
    private Long appSignerId;
    @Value("")
    private void setURL(){
        appSignerId = localConfig.getOnlineContractAppsignerid();
    }

    @ApiOperation(value = "获取云协议用户token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/contractRecord/getToken", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getToken(HttpServletRequest request, HttpServletResponse response) {
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        long signerId = contractRecordService.querySignerId(userInfo.getUserId());
        return this.success(contractRecordService.getToken(signerId));
    }
    @ApiOperation(value = "获取人脸识别token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/contractRecord/getVerifaceToken", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getIdCardToken(HttpServletRequest request, HttpServletResponse response) {
        return this.success(contractRecordService.getVerifaceToken());
    }
    @ApiOperation(value = "更新云协议状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "contractId", value = "在线协议id", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/contractRecord/updateStatus", method = RequestMethod.PUT)
    public ResponseEntity<WrappedResponse> updateStatus(HttpServletRequest request, HttpServletResponse response) {
        Long contractId = Long.parseLong(request.getParameter("contractId"));
        contractRecordService.sign(contractId);
        boolean status = contractRecordService.getStatus(contractId);
        if(status){
            contractRecordService.updateStatus(contractId);
        }
        return this.success(status);
    }

    @ApiOperation(value = "生成在线协议")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/contractRecord/saveContract", method = RequestMethod.PUT)
    public ResponseEntity<WrappedResponse> saveContract(HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> map = new HashMap<>();
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        String businessId = BusinessIdUtils.getBusinessId(request);
        map.put("userId",userInfo.getUserId());
        map.put("businessId",businessId);
        Long signerId = contractRecordService.querySignerId(userInfo.getUserId());
        List<ContractRecordPOJO> recordList = contractRecordService.queryPojoList(map);
        List<ContractDetailPOJO> detailList = null;
        StringBuffer contractIds = new StringBuffer();
        if(recordList != null && recordList.size() > 0){
            if(signerId.equals(0l)){
                ContractRecordPOJO firstContract = recordList.get(0);
                //获取身份证、手机地区
                String identityRegion = LocationUtil.getIdCardLocation(firstContract.getIdCard())+"";
                String phoneRegion = LocationUtil.getIdCardLocation(firstContract.getMobile())+"";
                signerId = contractRecordService.saveUser(firstContract.getRealName(),identityRegion,firstContract.getIdCard(),phoneRegion,firstContract.getMobile());
                Map<String,Object> map1 = new HashMap<String,Object>();
                map1.put("userId",userInfo.getUserId());
                map1.put("signerId",signerId);
                contractRecordService.update(map1);
            }
            for (ContractRecordPOJO contractRecordPOJO : recordList){
                detailList = contractDetailService.queryPojoList(contractRecordPOJO.getId());
                //生成收支项目table数组
                List<JSONObject> tableList = new ArrayList<JSONObject>();
                if(detailList != null && detailList.size() > 0){
                    for (ContractDetailPOJO contractDetailPOJO: detailList) {
                        JSONObject tableJson = new JSONObject();
                        tableJson.put("1",contractDetailPOJO.getSubjectName());
                        tableJson.put("2",contractDetailPOJO.getDcost());
                        tableJson.put("3",contractDetailPOJO.getDdiscount());
                        tableJson.put("4",contractDetailPOJO.getDnshoulddcost());
                        tableList.add(tableJson);
                    }
                }
                //生成表格数组
                List<JSONObject> tablesList = new ArrayList<JSONObject>();
                JSONObject tablesJson = new JSONObject();
                tablesJson.put("index",2);
                tablesJson.put("table",tableList);
                tablesList.add(tablesJson);

                //生成协议内容
                JSONObject contractDataJson = new JSONObject();
                contractDataJson.put("${vbillcode}", contractRecordPOJO.getVbillCode());
                contractDataJson.put("${studentname}", contractRecordPOJO.getRealName());
                contractDataJson.put("${idcard}", contractRecordPOJO.getIdCard());
                contractDataJson.put("${sex}", contractRecordPOJO.getSexName());
                contractDataJson.put("${email}", "");
                contractDataJson.put("${teachername}", contractRecordPOJO.getTeacherName());
                contractDataJson.put("${phone}", contractRecordPOJO.getMobile());
                contractDataJson.put("${qq}", contractRecordPOJO.getQq());
                contractDataJson.put("${payway}",contractRecordPOJO.getPayName());
                contractDataJson.put("${record}", contractRecordPOJO.getRecord());
                contractDataJson.put("${provincename}", contractRecordPOJO.getProvinceName());
                contractDataJson.put("${coursename}", contractRecordPOJO.getCoursename());
                contractDataJson.put("${bdyx}", contractRecordPOJO.getBdyx());
                contractDataJson.put("${regdate}", contractRecordPOJO.getRegdate());
                contractDataJson.put("${zy}", contractRecordPOJO.getZy());
                contractDataJson.put("${classname}", contractRecordPOJO.getClassName());
                contractDataJson.put("${emergencycall}", contractRecordPOJO.getEmergencyCall());
                contractDataJson.put("${regMoney}", contractRecordPOJO.getRegMoney()+"");
                contractDataJson.put("${date}", DateUtils.format(new Date()));
                contractDataJson.put("${signerDate}", DateUtils.format(new Date()));
                contractDataJson.put("tables", tablesList);

                //生成协议参数
                JSONObject paramsJson = new JSONObject();
                paramsJson.put("contractTitle", "恒企自考电子合同");
                paramsJson.put("templateId", contractRecordPOJO.getTemplateId());
                paramsJson.put("contractData",contractDataJson);
                Long contractId = contractRecordService.saveContract(paramsJson,signerId);
                if(contractId != null){
                    Map<String,Object> map1 = new HashMap<String,Object>();
                    map1.put("id",contractRecordPOJO.getId());
                    map1.put("contractId",contractId);
                    contractRecordService.update(map1);
                    saveSigner(signerId,contractId);
                    contractIds.append(contractId+",");
                }
            }
        }
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("contractId",contractIds);
        if(StringUtils.isBlank(contractIds.toString())){
            result.put("msg","生成协议失败");
        }
        result.put("token",contractRecordService.getToken(signerId));
        result.put("appId",contractRecordService.getAPPID());
        return this.success(result);
    }
    /**
     * 添加签署者
     * @author linchaokai
     * @date 2018/6/11 17:21
     * @param signerId 云合同用户id
     * @param contractId 协议id
     * @return
     */
    private boolean saveSigner(Long signerId,Long contractId){
        List<JSONObject> list = new ArrayList<JSONObject>();
        JSONObject appJson = new JSONObject();
        appJson.put("signerId",appSignerId);
        appJson.put("signPositionType","1");
        appJson.put("positionContent","盖章");
        appJson.put("signValidateType","0");

        JSONObject userJson = new JSONObject();
        userJson.put("signerId",signerId);
        userJson.put("signPositionType","1");
        userJson.put("positionContent","签名");
        userJson.put("signValidateType","0");
        list.add(appJson);
        list.add(userJson);

        JSONObject paramsJson = new JSONObject();
        paramsJson.put("idType","0");
        paramsJson.put("idContent",contractId);
        paramsJson.put("signers",list);
        return contractRecordService.saveSigner(paramsJson,signerId);
    };

    @ApiOperation(value = "获取协议未签订数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"), 
    })
    @RequestMapping(value = "/contractRecord/getContractUnSignNum", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> getContractUnSignNum(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> map = new HashMap<String,Object>();
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        String businessId = BusinessIdUtils.getBusinessId(request);
        map.put("userId",userInfo.getUserId());
        map.put("businessId",businessId);
        map.put("status", "0");
        int result = contractRecordService.getContractSignNum(map);
        Map<String,Object> m = new HashMap<>();
        m.put("count", result);
		return this.success(m);
	}

}

