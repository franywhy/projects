package com.izhubo.excel;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2017/9/14 0014.
 */
@Controller
@RequestMapping("/excel")
public class ExcelController {

    @Resource
    private MongoTemplate mainMongo;

    @Resource
    private MongoTemplate qquserMongo;

    private DBCollection qQUser() {
        return qquserMongo.getCollection("qQUser");
    }

    private DBCollection users() {
        return mainMongo.getCollection("users");
    }

    private DBCollection tip_content() {
        return mainMongo.getCollection("tip_content");
    }

    /**
     * 导入Excel数据
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "importFile", method = RequestMethod.POST)
    public Object importFile(HttpServletRequest request) {
        CommonsMultipartResolver parse = new CommonsMultipartResolver();
        MultipartHttpServletRequest req = parse.resolveMultipart(request);
        MultipartFile file = req.getMultiFileMap().getFirst("file");
        int headerNum =  ServletRequestUtils.getIntParameter(req,"headerNum",0);
        int sheetIndex = ServletRequestUtils.getIntParameter(req,"sheetIndex",0);
        int product = ServletRequestUtils.getIntParameter(req,"product",0);
        int openType = ServletRequestUtils.getIntParameter(req,"openType",0);

        Map map = new HashMap<>();
        List data = new ArrayList();
        try {
            ImportExcel ei = new ImportExcel(file, headerNum, sheetIndex);
            for (int i = ei.getDataRowNum(); i <= ei.getLastDataRowNum(); i++) {
                Row row = ei.getRow(i);
                Map tip = new HashMap();
                List<String> list = new ArrayList<>();
                for (int j = 0; j < ei.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    if(null != cell) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        Object val = ei.getCellValue(row, j);
                        if(0 == j) {
                            tip.put("mobile",val.toString());
                        } else if(1 == j && 0 == openType) {
                            //开通方式（0：按科目，1：按专业）
                            tip.put("profession",val.toString());
                        } else {
                            list.add(val.toString());
                        }
                    }
                }
                tip.put("tipStrList",list);
                if(0 == product) { //会计
                    if(updateUserTip(tip)) {
                        data.add(tip);
                    }
                } else if(1 == product) { //自考
                    if(updateUserTipZikao(tip)) {
                        data.add(tip);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.put("code",200);
        map.put("message","success");
        map.put("导入失败记录",data.size());
        map.put("data",data);
        return map;
    }

    private boolean updateUserTipZikao(Map map) {

        DBObject qqUser = qQUser().findOne(new BasicDBObject("username",map.get("mobile")),new BasicDBObject("tuid",1));
        if(null == qqUser) return true;

        DBObject user = users().findOne(new BasicDBObject("tuid", qqUser.get("tuid")), new BasicDBObject("user_industry_zikao",1));
        if(null == user) return true;

        List<String> strTips = (List<String>) map.get("tipStrList");
        if(strTips.size()==0) return false;

        boolean has1111 = false;
        List tipOldID = new ArrayList();

        List<DBObject> user_industry_zikao = (List<DBObject>) user.get("user_industry_zikao");

        if(null != user_industry_zikao) {
            for(int i=0; i<user_industry_zikao.size(); i++) {
                DBObject object = user_industry_zikao.get(i);
                if(null != object) {
                    int industry_id = (Integer) object.get("industry_id");
                    if(1111 == industry_id) {
                        has1111 = true;
                        List<DBObject> users_industry_tip = (List<DBObject>)object.get("users_industry_tip");
                        if(null != users_industry_tip) {
                            for(int j=0; j<users_industry_tip.size(); j++) {
                                tipOldID.add(users_industry_tip.get(j).get("industry_tip_id"));
                            }
                        } else {
                            users_industry_tip = new ArrayList<>();
                            object.put("users_industry_tip",users_industry_tip);
                        }
                        //按专业开通
                        if(null == map.get("profession")) {
                            for (int k=0; k<strTips.size(); k++) {
                                DBObject tipNew = tip_content().findOne(new BasicDBObject("parent_tip_id",0).append("tip_name",strTips.get(k)).append("product",1),new BasicDBObject("_id",1));
                                List<DBObject> childTip = tip_content().find(new BasicDBObject("parent_tip_id",tipNew.get("_id")).append("product",1),new BasicDBObject("_id",1)).toArray();
                                for(int n=0; n<childTip.size(); n++) {
                                    DBObject child = childTip.get(n);
                                    if(null != child && !tipOldID.contains(child.get("_id"))) {
                                        DBObject idMap = new BasicDBObject();
                                        idMap.put("industry_tip_id",child.get("_id"));
                                        users_industry_tip.add(idMap);
                                    }
                                }
                            }
                        }
                        //按科目开通
                        else {
                            DBObject tipNew = tip_content().findOne(new BasicDBObject("parent_tip_id",0).append("tip_name",map.get("profession")).append("product",1),new BasicDBObject("_id",1));
                            for (int k=0; k<strTips.size(); k++) {
                                DBObject childTip = tip_content().findOne(new BasicDBObject("parent_tip_id",tipNew.get("_id")).append("tip_name",strTips.get(k)).append("product",1),new BasicDBObject("_id",1));
                                if(null != childTip && !tipOldID.contains(childTip.get("_id"))) {
                                    DBObject idMap = new BasicDBObject();
                                    idMap.put("industry_tip_id",childTip.get("_id"));
                                    users_industry_tip.add(idMap);
                                }
                            }
                        }
                        break;
                    }
                }
            }
        } else {
            user_industry_zikao = new ArrayList<>();
        }

        if(!has1111) {
            List<DBObject> listTip = new ArrayList<>();
            DBObject mapTip = new BasicDBObject();
            //按专业开通
            if(null == map.get("profession")) {
                for (int i=0; i<strTips.size(); i++) {
                    DBObject tipNew = tip_content().findOne(new BasicDBObject("parent_tip_id",0).append("tip_name",strTips.get(i)).append("product",1),new BasicDBObject("_id",1));
                    List<DBObject> childTip = tip_content().find(new BasicDBObject("parent_tip_id",tipNew.get("_id")).append("product",1),new BasicDBObject("_id",1)).toArray();
                    for(int n=0; n<childTip.size(); n++) {
                        DBObject child = childTip.get(n);
                        if(null != child) {
                            DBObject idMap = new BasicDBObject();
                            idMap.put("industry_tip_id",child.get("_id"));
                            listTip.add(idMap);
                        }
                    }
                }
            }
            //按科目开通
            else {
                DBObject tipNew = tip_content().findOne(new BasicDBObject("parent_tip_id",0).append("tip_name",map.get("profession")).append("product",1),new BasicDBObject("_id",1));
                for (int i=0; i<strTips.size(); i++) {
                    DBObject childTip = tip_content().findOne(new BasicDBObject("parent_tip_id",tipNew.get("_id")).append("tip_name",strTips.get(i)).append("product",1),new BasicDBObject("_id",1));
                    if(null != childTip) {
                        DBObject idMap = new BasicDBObject();
                        idMap.put("industry_tip_id",childTip.get("_id"));
                        listTip.add(idMap);
                    }
                }
            }
            mapTip.put("industry_id",1111);
            mapTip.put("users_industry_tip",listTip);

            user_industry_zikao.add(mapTip);
        }
        users().update(new BasicDBObject("tuid", qqUser.get("tuid")),
                new BasicDBObject("$set",new BasicDBObject("user_industry_zikao",user_industry_zikao).append("business_id", Arrays.asList("zikao")).append("priv1",1).append("priv",2)));

        return false;
    }

    private boolean updateUserTip(Map map) {

        DBObject qqUser = qQUser().findOne(new BasicDBObject("username",map.get("mobile")),new BasicDBObject("tuid",1));
        if(null == qqUser) return true;

        DBObject user = users().findOne(new BasicDBObject("tuid", qqUser.get("tuid")), new BasicDBObject("user_industry",1));
        if(null == user) return true;

        List<String> strTips = (List<String>) map.get("tipStrList");
        if(strTips.size()==0) return false;

        boolean has1111 = false;
        List tipOldID = new ArrayList();

        List<DBObject> user_industry = (List<DBObject>) user.get("user_industry");

        if(null != user_industry) {
            for(int i=0; i<user_industry.size(); i++) {
                DBObject object = user_industry.get(i);
                if(null != object) {
                    int industry_id = (Integer) object.get("industry_id");
                    if(1111 == industry_id) {
                        has1111 = true;
                        List<DBObject> users_industry_tip = (List<DBObject>)object.get("users_industry_tip");
                        if(null != users_industry_tip) {
                            for(int j=0; j<users_industry_tip.size(); j++) {
                                tipOldID.add(users_industry_tip.get(j).get("industry_tip_id"));
                            }
                        } else {
                            users_industry_tip = new ArrayList<>();
                            object.put("users_industry_tip",users_industry_tip);
                        }
                        //按专业开通
                        if(null == map.get("profession")) {
                            for (int k=0; k<strTips.size(); k++) {
                                DBObject tipNew = tip_content().findOne(new BasicDBObject("parent_tip_id",0).append("tip_name",strTips.get(k)).append("product",0),new BasicDBObject("_id",1));
                                List<DBObject> childTip = tip_content().find(new BasicDBObject("parent_tip_id",tipNew.get("_id")).append("product",0),new BasicDBObject("_id",1)).toArray();
                                for(int n=0; n<childTip.size(); n++) {
                                    DBObject child = childTip.get(n);
                                    if(null != child && !tipOldID.contains(child.get("_id"))) {
                                        DBObject idMap = new BasicDBObject();
                                        idMap.put("industry_tip_id",child.get("_id"));
                                        users_industry_tip.add(idMap);
                                    }
                                }
                            }
                        }
                        //按科目开通
                        else {
                            DBObject tipNew = tip_content().findOne(new BasicDBObject("parent_tip_id",0).append("tip_name",map.get("profession")).append("product",0),new BasicDBObject("_id",1));
                            for (int k=0; k<strTips.size(); k++) {
                                DBObject childTip = tip_content().findOne(new BasicDBObject("parent_tip_id",tipNew.get("_id")).append("tip_name",strTips.get(k)).append("product",0),new BasicDBObject("_id",1));
                                if(null != childTip && !tipOldID.contains(childTip.get("_id"))) {
                                    DBObject idMap = new BasicDBObject();
                                    idMap.put("industry_tip_id",childTip.get("_id"));
                                    users_industry_tip.add(idMap);
                                }
                            }
                        }
                        break;
                    }
                }
            }
        } else {
            user_industry = new ArrayList<>();
        }

        if(!has1111) {
            List<DBObject> listTip = new ArrayList<>();
            DBObject mapTip = new BasicDBObject();
            //按专业开通
            if(null == map.get("profession")) {
                for (int i = 0; i < strTips.size(); i++) {
                    DBObject tipNew = tip_content().findOne(new BasicDBObject("parent_tip_id",0).append("tip_name", strTips.get(i)).append("product", 0), new BasicDBObject("_id", 1));
                    List<DBObject> childTip = tip_content().find(new BasicDBObject("parent_tip_id", tipNew.get("_id")).append("product", 0), new BasicDBObject("_id", 1)).toArray();
                    for (int n = 0; n < childTip.size(); n++) {
                        DBObject child = childTip.get(n);
                        if (null != child) {
                            DBObject idMap = new BasicDBObject();
                            idMap.put("industry_tip_id", child.get("_id"));
                            listTip.add(idMap);
                        }
                    }
                }
            }
            //按科目开通
            else {
                DBObject tipNew = tip_content().findOne(new BasicDBObject("parent_tip_id",0).append("tip_name",map.get("profession")).append("product",0),new BasicDBObject("_id",1));
                for (int i=0; i<strTips.size(); i++) {
                    DBObject childTip = tip_content().findOne(new BasicDBObject("parent_tip_id",tipNew.get("_id")).append("tip_name",strTips.get(i)).append("product",0),new BasicDBObject("_id",1));
                    if(null != childTip) {
                        DBObject idMap = new BasicDBObject();
                        idMap.put("industry_tip_id",childTip.get("_id"));
                        listTip.add(idMap);
                    }
                }
            }
            mapTip.put("industry_id",1111);
            mapTip.put("users_industry_tip",listTip);

            user_industry.add(mapTip);
        }
        users().update(new BasicDBObject("tuid", qqUser.get("tuid")),
                new BasicDBObject("$set",new BasicDBObject("user_industry",user_industry).append("business_id", Arrays.asList("kuaiji")).append("priv1",1).append("priv",2)));

        return false;
    }

}
