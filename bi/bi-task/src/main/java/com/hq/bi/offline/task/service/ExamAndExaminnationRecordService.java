package com.hq.bi.offline.task.service;

import com.github.pagehelper.PageHelper;
import com.hq.bi.offline.task.factory.MybatisSessionFactory;
import com.hq.bi.offline.task.mapper.TeachExamQualityMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

/**
 * Description:模拟考试和国考记录
 * Autor: wl
 * Date: 2019-12-30-15:13
 */
@Slf4j
public class ExamAndExaminnationRecordService {


    public static void main(String[] args){
        //插入题库考试用户关联表数据
        insertTkExamRecordUserList();
        //插入蓝鲸自适应课程模考结果表,
        insertBwCourseTkExamRecordUserList();
        //插入国考成绩情况表
        insertBwCourseExaminationRecordList();

    }

    /**
     * 插入题库考试用户关联表数据
     *
     */
    private static void insertTkExamRecordUserList(){
        try{
            long startlong=System.currentTimeMillis();
            SqlSessionFactory sqlSessionFactory = MybatisSessionFactory.getSqlSessionFactory();
            SqlSession sqlSession = sqlSessionFactory.openSession();
            TeachExamQualityMapper teachExamQualityMapper = sqlSession.getMapper(TeachExamQualityMapper.class);
            boolean isTrue=true;
            int page=1;
            int pageSize=500000;
            int keyToken = 0;
            //分批插入，每次插入10000条
            while (isTrue) {
                PageHelper.startPage(page,pageSize);
                List<Map> tkExamRecordUserList = teachExamQualityMapper.getTkExamRecordUserList();
                if(tkExamRecordUserList != null && tkExamRecordUserList.size()>0){
                    //第一次插入前删除报表中旧数据
                    if(page==1){
                        int deleteCount = teachExamQualityMapper.deleteAllTkExamRecordUserEntity();
                        log.info("删除题库考试用户关联表数据"+ deleteCount +"条");
                    }
                    //为了防止10万数据JVM消耗过大，分次插入，每次插入1000条
                    int listSize=tkExamRecordUserList.size();
                    int toIndex=10000;
                    for (int i = 0; i < tkExamRecordUserList.size(); i += 10000) {
                        if (i + 10000 > listSize) {
                            toIndex = listSize - i;
                        }
                        List newList = tkExamRecordUserList.subList(i, i + toIndex);
                        keyToken++;
                        teachExamQualityMapper.saveTkExamRecordUserList(newList);
                        log.info("第" + keyToken + "次" + " 插入题库考试用户关联表数据" + newList.size() + "条");
                    }
                }else {
                    break;
                }
                //最后一页查完直接提交，不用重新查一次是否为空
                if(tkExamRecordUserList.size()< pageSize){
                    break;
                }else {
                    page++;
                }
            }
            sqlSession.commit();
            long endlong = System.currentTimeMillis() - startlong;
            log.info("插入 题库考试用户关联表数据 耗时>>>>>"+endlong/1000 +"秒");
        }catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    /**
     * 插入蓝鲸课程模考结果表
     *
     */
    private static void insertBwCourseTkExamRecordUserList(){

        try{
            long startlong=System.currentTimeMillis();
            SqlSessionFactory sqlSessionFactory = MybatisSessionFactory.getSqlSessionFactory();
            SqlSession sqlSession = sqlSessionFactory.openSession();
            TeachExamQualityMapper teachExamQualityMapper = sqlSession.getMapper(TeachExamQualityMapper.class);
            boolean isTrue=true;
            int page=1;
            int pageSize=50000;
            int keyToken = 0;
            //分批插入，每次插入10000条
            while (isTrue) {
                PageHelper.startPage(page,pageSize);
                List<Map> bwCourseTkExamRecordUserList = teachExamQualityMapper.getBwCourseTkExamRecordUserList();
                if(bwCourseTkExamRecordUserList != null && bwCourseTkExamRecordUserList.size()>0){
                    //第一次插入前删除报表中旧数据
                    if(page==1){
                        int deleteCount = teachExamQualityMapper.deleteBwCourseAllTkExamRecordUserEntity();
                        log.info("删除蓝鲸课程模考结果数据"+ deleteCount +"条");
                    }
                    //为了防止10万数据JVM消耗过大，分次插入，每次插入1000条
                    int listSize=bwCourseTkExamRecordUserList.size();
                    int toIndex=5000;
                    for (int i = 0; i < bwCourseTkExamRecordUserList.size(); i += 5000) {
                        if (i + 5000 > listSize) {
                            toIndex = listSize - i;
                        }
                        List newList = bwCourseTkExamRecordUserList.subList(i, i + toIndex);
                        keyToken++;
                        teachExamQualityMapper.saveBwCourseTkExamRecordUserList(newList);
                        log.info("第" + keyToken + "次" + " 插入蓝鲸课程模考结果数据" + newList.size() + "条");
                    }
                }else {
                    break;
                }
                //最后一页查完直接提交，不用重新查一次是否为空
                if(bwCourseTkExamRecordUserList.size()< pageSize){
                    break;
                }else {
                    page++;
                }
            }
            sqlSession.commit();
            long endlong = System.currentTimeMillis() - startlong;
            log.info("插入 蓝鲸课程模考结果数据 耗时>>>>>"+endlong/1000 +"秒");
        }catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    /**
     * 插入蓝鲸课程国考结果表
     *
     */
    private static void insertBwCourseExaminationRecordList(){

        try{
            long startlong=System.currentTimeMillis();
            SqlSessionFactory sqlSessionFactory = MybatisSessionFactory.getSqlSessionFactory();
            SqlSession sqlSession = sqlSessionFactory.openSession();
            TeachExamQualityMapper teachExamQualityMapper = sqlSession.getMapper(TeachExamQualityMapper.class);
            boolean isTrue=true;
            int page=1;
            int pageSize=50000;
            int keyToken = 0;
            //分批插入，每次插入10000条
            while (isTrue) {
                PageHelper.startPage(page,pageSize);
                List<Map> bwCourseExaminationRecordList = teachExamQualityMapper.getBwCourseExaminationRecordList();
                if(bwCourseExaminationRecordList != null && bwCourseExaminationRecordList.size()>0){
                    //第一次插入前删除报表中旧数据
                    if(page==1){
                        int deleteCount = teachExamQualityMapper.deleteBwCourseExaminationRecordEntity();
                        log.info("删除蓝鲸课程国考结果明细数据"+ deleteCount +"条");
                    }
                    //为了防止10万数据JVM消耗过大，分次插入，每次插入1000条
                    int listSize=bwCourseExaminationRecordList.size();
                    int toIndex=5000;
                    for (int i = 0; i < bwCourseExaminationRecordList.size(); i += 5000) {
                        if (i + 5000 > listSize) {
                            toIndex = listSize - i;
                        }
                        List newList = bwCourseExaminationRecordList.subList(i, i + toIndex);
                        keyToken++;
                        teachExamQualityMapper.saveBwCourseExaminationRecordList(newList);
                        log.info("第" + keyToken + "次" + " 插入蓝鲸课程国考结果明细数据" + newList.size() + "条");
                    }
                }else {
                    break;
                }
                //最后一页查完直接提交，不用重新查一次是否为空
                if(bwCourseExaminationRecordList.size()< pageSize){
                    break;
                }else {
                    page++;
                }
            }
            sqlSession.commit();
            long endlong = System.currentTimeMillis() - startlong;
            log.info("插入 蓝鲸课程国考结果明细数据 耗时>>>>>"+endlong/1000 +"秒");
        }catch (Exception e) {
            log.info(e.getMessage());
        }
    }

}
