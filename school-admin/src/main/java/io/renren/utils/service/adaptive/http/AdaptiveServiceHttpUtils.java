package io.renren.utils.service.adaptive.http;

import io.renren.utils.JSONUtil;
import io.renren.utils.R;
import io.renren.utils.http.HttpClientUtil4_3;
import io.renren.utils.service.adaptive.pojo.ASCyclicTopologyPOJO;
import io.renren.utils.service.adaptive.pojo.ASResponseModel;
import io.renren.utils.service.adaptive.pojo.AdlDecisionForestPOJO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 调用算法服务工具类
 * @author shihongjie
 */
@Component
public class AdaptiveServiceHttpUtils {
    public static final String SUCCESS_HEADER = "20";
    /**日志*/
    private static final Logger log = LoggerFactory.getLogger(AdaptiveServiceHttpUtils.class);
    /**地址*/
    private static String AS_DOMAIN = "";
    @Value("#{application['pom.adptive.service.domain']}")
    private void setAsDomain(String domain){
        log.info("ASHttpUtils init pom.adptive.service.domain AS_DOMAIN:{}" , domain);
         AS_DOMAIN = domain;
    }

    private static Map<String,String> headers = new HashMap<>();
    static {
        headers.put("Content-Type" ,"application/json;charset=UTF-8");
    }

    /**
     * 检查知识空间是否为有向无环图-知识点子集不需要包含自己
     * @param asCyclicTopologyPOJO
     * @return
     */
    public static R cyclicTopology(final ASCyclicTopologyPOJO asCyclicTopologyPOJO){
        String json = JSONUtil.beanToJson(asCyclicTopologyPOJO);
        String responseStr = null;
        String url = AS_DOMAIN + "/inner/cyclicStatusOfTopology";
        log.debug("cyclicTopology post url :{} json:{}" , url , json);
        try {
            responseStr = HttpClientUtil4_3.postStr(url, json, headers);
            log.info("cyclicTopology responseStr : {}" , responseStr);
            ASResponseModel asResponseModel = JSONUtil.jsonToBean(responseStr, ASResponseModel.class);
            if(asResponseModel != null && StringUtils.isNotBlank(asResponseModel.getCode())){
                if(asResponseModel.getCode().indexOf(SUCCESS_HEADER) == 0){
                    return R.ok();
                }else{
                    return R.error(asResponseModel.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.error("操作失败!错误信息:" + responseStr);
    }

    /**
     * 根据知识空间生成决策树-知识点子集需要包含自己
     * @return
     */
    public static R decisionForest(AdlDecisionForestPOJO adlDecisionForestPOJO){
        String json = JSONUtil.beanToJson(adlDecisionForestPOJO);
        String responseStr = null;
        String url = AS_DOMAIN + "/inner/decisionTree";
        log.debug("decisionForest post url :{} json:{}" , url , json);
        try {
            responseStr = HttpClientUtil4_3.postStr(url, json, headers);
            log.info("decisionForest responseStr : {}" , responseStr);
            ASResponseModel asResponseModel = JSONUtil.jsonToBean(responseStr, ASResponseModel.class);
            if(asResponseModel != null && StringUtils.isNotBlank(asResponseModel.getCode())){
                if(asResponseModel.getCode().indexOf(SUCCESS_HEADER) == 0){
                    return R.ok();
                }else{
                    return R.error(asResponseModel.getMessage());
                }
            }
        } catch (IOException e) {
            log.error("message=",e);
            return R.error(e.getMessage());
//            e.printStackTrace();
        }
        return R.error("操作失败!错误信息:" + responseStr);
    }

}
