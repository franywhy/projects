package com.hqjy.msg.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DataSortAndFilterUtils {

    /**
     * 排序
     * @param list
     * @param comparator
     * @return
     */
    public static List sort(List list , Comparator comparator){
            List result = new ArrayList();

            try{

                if ( null!=comparator) {
                    result = (List) list.stream().sorted(comparator).collect(Collectors.toList());
                }else{
                    result = list;
                }


            }catch (Exception e){
                e.printStackTrace();
            }



        return result;
    }

    /**
     * 过滤
     * @param list
     * @param predicate
     * @return
     */
    public static List filter(List list , Predicate predicate){
        List result = new ArrayList();
        try{
            if ( null!=predicate) {
                result =  (List) list.stream().filter(predicate).collect(Collectors.toList());
            }else{
                result = list;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 分页
     * @param list
     * @param pageSize
     * @param pageNum
     * @return
     */
    public static PageUtils page(List list ,int pageSize,int pageNum){
        PageUtils pageUtils = new PageUtils();
        try{
            pageUtils =  new PageUtils(pageNum,pageSize,list);
        }catch (Exception e){
            e.printStackTrace();
        }
        return pageUtils;
    }
}
