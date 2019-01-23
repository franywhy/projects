package com.hq.bi.offline.task.service;

import com.github.pagehelper.PageHelper;
import com.hq.bi.offline.task.factory.MybatisSessionFactory;
import com.hq.bi.offline.task.mapper.ClassExamQualityMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

/**
 * Description:班级考试质量拼比
 * Autor: wl
 * Date: 2018-12-17
 */
@Slf4j
public class ClassExamQualityService {
    public static  void main(String[] args){

        try{
            long startlong=System.currentTimeMillis();
            SqlSessionFactory sqlSessionFactory = MybatisSessionFactory.getSqlSessionFactory();
            SqlSession sqlSession = sqlSessionFactory.openSession();
            ClassExamQualityMapper classExamQualityMapper = sqlSession.getMapper(ClassExamQualityMapper.class);
            boolean isTrue=true;
            int page=1;
            int pageSize=500000;
            int keyToken = 0;
            //分批插入，每次插入10000条
            while (isTrue) {
                PageHelper.startPage(page,pageSize,false,false,null);
                List<Map> classExamQualityList = classExamQualityMapper.getClassExamQualityList();
                if(classExamQualityList != null && classExamQualityList.size()>0){
                    //第一次插入前删除报表中旧数据
                    if(page==1){
                        int deleteCount = classExamQualityMapper.deleteAllClassExamQualityEntity();
                        log.info("删除班级考试质量明细数据:"+ deleteCount +"条");
                    }
                    //为了防止10万数据JVM消耗过大，分次插入，每次插入1000条
                    int listSize=classExamQualityList.size();
                    int toIndex=10000;
                    for(int i = 0;i<classExamQualityList.size();i+=10000){
                        if(i+10000>listSize){
                            toIndex=listSize-i;
                        }
                        List newList = classExamQualityList.subList(i,i+toIndex);
                        keyToken++;
                        classExamQualityMapper.saveClassExamQuality(newList);
                        log.info("第"+keyToken+"次"+"插入班级考试质量明细数据:"+ newList.size() +"条");
                    }
                }else {
                    break;
                }
                //最后一页查完直接提交，不用重新查一次是否为空
                if(classExamQualityList.size()< pageSize){
                    break;
                }else {
                    page++;
                }
            }
            sqlSession.commit();
            long endlong = System.currentTimeMillis() - startlong;
            log.info("插入 班级考试质量明细数据 耗时>>>>>"+endlong/1000 +"秒");
        }catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}
