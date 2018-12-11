package com.hq.learningcenter.utils;

import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther linchaokai
 * @description
 * @date 2018/6/7
 */
public class MyTest {
    public static void main(String[] args) {
        List<JSONObject> list = new ArrayList<JSONObject>();
        JSONObject appJson = new JSONObject();
        appJson.put("signerId","1111");
        appJson.put("signPositionType","1");
        appJson.put("positionContent","signer");
        appJson.put("signValidateType","0");

        JSONObject userJson = new JSONObject();
        userJson.put("signerId","2");
        userJson.put("signPositionType","1");
        userJson.put("positionContent","signer");
        userJson.put("signValidateType","0");
        list.add(appJson);
        list.add(userJson);

        JSONObject paramsJson = new JSONObject();
        paramsJson.put("idType","0");
        paramsJson.put("idContent","1");
        paramsJson.put("signers",list);
        System.out.println(paramsJson);
    }
}

