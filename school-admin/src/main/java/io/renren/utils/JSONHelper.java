package io.renren.utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
public class JSONHelper {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
     /**
      * 将JSONArray对象转换成Map-List集合
      * @param jsonArr
      * @return
      */
     public static Object JsonToList(JSONArray jsonArr){
         List<Object> jsonObjList = new ArrayList<Object> ();
         for(Object obj : jsonArr){
             if(obj instanceof JSONArray){
                 jsonObjList.add(JsonToList((JSONArray) obj));
             } else if(obj instanceof JSONObject){
                 jsonObjList.add(JsonToMap((JSONObject) obj));
             }else{
                 jsonObjList.add(obj);
             }
         }
         return jsonObjList;
     }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
    /**
     *  将JSONObjec对象转换成Map-List集合
     * @param json
     * @return
     */
     public static Map<String, Object> JsonToMap(JSONObject json){
         Map<String,Object> columnValMap = new HashMap<String,Object>();
         Set<Object> jsonKeys = json.keySet();
         for (Object key : jsonKeys) {
             Object JsonValObj = json.get(key);
             if(JsonValObj instanceof JSONArray){
                columnValMap.put((String)key,  JsonToList((JSONArray) JsonValObj));
             }else if(key instanceof JSONObject){
                columnValMap.put((String)key,  JsonToMap((JSONObject) JsonValObj));
             }else{
                 columnValMap.put((String)key,JsonValObj);
             }
        }
         return columnValMap;
     }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
     /**
      * 将json对象转换为map集合，通过此方法获取存放map集合键的list集合
      * @param obj
      * @return
      */
     public static List<Object> mapKeys(Object obj){
         List<Object> keysList = new ArrayList<Object>();
         Map<String,Object> columnValMap = new HashMap<String,Object>();
         String columnStr = "column";
         if(obj instanceof JSONArray){
             List<Map<String, Object>> jsonObjList = new ArrayList<Map<String, Object>> ();
             jsonObjList = (List<Map<String, Object>>) JsonToList((JSONArray) obj);
             columnValMap =(Map<String, Object>) (jsonObjList.get(0));
             //以上三句可优化为如下一句
             //columnValMap =(Map<String, Object>)(((List<Object>)JsonToList((JSONArray) obj)).get(0));
         }else if(obj instanceof JSONObject){
             columnValMap =JsonToMap((JSONObject) obj);
         }else{
             keysList.add(obj);
         }
         for(int i=0;i<columnValMap.keySet().size();i++){
             keysList.add(columnStr+(i+1));
         }
         System.out.println(keysList.size());
         return keysList;
     }
}