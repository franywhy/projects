package com.hqjy.msg.util;

import java.util.*;

/**
 * Created by Administrator on 2018/1/5 0005.
 */
public class ListUtils  {
    public static String DEFUALT_STR_SPLIT = ",";

    /**
     * 判断list是否为空。若为空，则返回false；不为空，则反回true
     * @param list
     * @return
     */
    public static boolean listIsExists(List list){
        if(null==list || list.isEmpty()) return false;
        return true;

    }

    public static List listsToArray(List list ,int idx ){
        int size = list.size() ;
        List lists = new ArrayList();
        if (size<=idx) {
            lists.add(list);
            return lists;
        }
        int a = (size/idx);
        for (int i = 0; i <= a; i++) {
            int start = i*idx;
            int end = (i+1)*idx;
            if (end>size) {
                end = size;
            }
            lists.add(list.subList(start,end));

        }
        return lists;
    }

    /**
     *
     * @param list
     * @param size
     * @return
     */
    public static List linkedHashSetsToStrings(List<LinkedHashSet> list, int size ){
        List result = new ArrayList();
        LinkedHashSet set = null;
        for (int i = 0; i < list.size(); i++) {
            set = (LinkedHashSet) list.get(i);
            Iterator iterator = set.iterator();
            while (iterator.hasNext()){

                result.add((String) iterator.next());
            }

        }
        if(size>0) {
            int listSize = result.size();
            int startSize = listSize-size>0 ? listSize-size : 0 ;
            return result.subList(startSize,listSize);
        }
        return result;
    }
    public static String listToSqlIn(List<String> list){
        StringBuffer sb  = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            sb.append("'"+str.replaceAll("'","")+"',");
        }
        return sb.toString().substring(0,sb.length()-1);
    }

    public static List strToList(String str){
        return  strToListBySplit(str,DEFUALT_STR_SPLIT);


    }

    public static List strToListBySplit(String str ,String split){
        String[] strs = str.split(split);
        List list = new ArrayList();
        if (null!= strs && strs.length>0) {
            for (int i = 0; i < strs.length; i++) {
                list.add(strs[i]);
            }
        }
        return list;

    }

    public static String listTostr(List<String> list){
        StringBuffer sb  = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            sb.append(""+str.replaceAll("'","")+",");
        }
        return sb.toString().substring(0,sb.length()-1);
    }

    public static List twoListInto(List<String> list1,List<String> list2){
        StringBuffer sb  = new StringBuffer();
        Set set = new HashSet();
        if (ListUtils.listIsExists(list1)) {
            for (int i = 0; i < list1.size(); i++) {
                set.add(list1.get(i));
            }
        }
        if (ListUtils.listIsExists(list2)) {
            for (int i = 0; i < list2.size(); i++) {
                set.add(list2.get(i));
            }
        }

        Iterator iterator = set.iterator();
        List result = new ArrayList();
        while (iterator.hasNext()){
            result.add(iterator.next());
        }

        return result;
    }


    public static List pagingList(List list, int page, int pageSize) {
        int size = list.size();
        int pageCount=size/pageSize;
        int fromIndex = pageSize * (page - 1);
        int toIndex = fromIndex + pageSize;
        if (toIndex >= size) {
            toIndex = size;
        }
        if(page>pageCount+1){
            fromIndex=0;
            toIndex=0;
        }

        return list.subList(fromIndex, toIndex);
    }

    public static void getUnreadMsgIds(List msgIds) {
        // 辅助list
        List assistantList = new ArrayList();
        // 遍历msgIds
        for (int i = 0; i < msgIds.size(); i++){
            // 如果该消息id以"readed_"开头
            if (msgIds.get(i).toString().startsWith("readed_")) {
                String str = msgIds.get(i).toString();
                // 删除这个id
                msgIds.remove(str);
                // 并且回退id
                i--;

                // 把"readed_"部分截取掉(已读的消息id会在用户id下保存着消息id以及readed_ + msgId)
                String newstr = str.substring(7);
                // 把截取掉readed 的部分加入到辅助类
                assistantList.add(newstr);
            }
        }

        // 求差集
        msgIds.removeAll(assistantList);
    }
}
