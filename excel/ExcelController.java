package com.izhubo.excel;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param file
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "importFile", method = RequestMethod.POST)
    public Object importFile(MultipartFile file, int headerNum, int sheetIndex) {
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
                        } else if(1 == j) {
                            tip.put("name",val.toString());
                        } else {
                            list.add(val.toString());
                        }
                    }
                }
                tip.put("tipStrList",list);
                if(updateUserTip(tip)) {
                    data.add(tip);
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

    private boolean updateUserTip(Map map) {

        DBObject qqUser = qQUser().findOne(new BasicDBObject("username",map.get("mobile")),new BasicDBObject("tuid",1));
        if(null == qqUser) return true;

        DBObject user = users().findOne(new BasicDBObject("tuid", qqUser.get("tuid")), new BasicDBObject("tip_contents",1));
        if(null == user) return true;

        List tipOldID = new ArrayList();
        List<DBObject> tipList = new ArrayList<>();
        List<DBObject> list = (List<DBObject>) user.get("tip_contents");
        if(null != list) {
            for(int i=0; i<list.size(); i++) {
                DBObject object = list.get(i);
                DBObject tipOld = tip_content().findOne(new BasicDBObject("_id",object.get("_id")),new BasicDBObject("tip_name",1));
                object.put("tip_name",tipOld.get("tip_name"));
                object.put("refresh_time",System.currentTimeMillis());
                tipOldID.add(object.get("_id"));
            }
            tipList = list;
        }

        Map fieldMap = new HashMap();
        fieldMap.put("_id",1);
        fieldMap.put("tip_name",1);

        List<String> strTips = (List<String>) map.get("tipStrList");
        if(null != strTips) {
            for (int i=0; i<strTips.size(); i++) {
                DBObject tipNew = tip_content().findOne(new BasicDBObject("tip_name",strTips.get(i)),new BasicDBObject(fieldMap));
                if(null != tipNew && !tipOldID.contains(tipNew.get("_id"))) {
                    tipNew.put("refresh_time",System.currentTimeMillis());
                    tipList.add(tipNew);
                }
            }
        }

        users().update(new BasicDBObject("tuid", qqUser.get("tuid")),new BasicDBObject("$set",new BasicDBObject("tip_contents",tipList)));
        return false;
    }

}
