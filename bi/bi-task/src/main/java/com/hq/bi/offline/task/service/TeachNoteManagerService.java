package com.hq.bi.offline.task.service;

import com.github.pagehelper.PageHelper;
import com.hq.bi.offline.task.entity.PKIndexEvaluationEntity;
import com.hq.bi.offline.task.factory.MybatisSessionFactory;
import com.hq.bi.offline.task.mapper.PKIndexMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * Description:学习笔记管理
 * @author: hutao
 * Date: 2018-12-11-9:03
 */
@Slf4j
public class TeachNoteManagerService {
    public static  void main(String[] args){
        long start = System.currentTimeMillis();
        SqlSessionFactory sqlSessionFactory = MybatisSessionFactory.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        PKIndexMapper pkIndexMapper = sqlSession.getMapper(PKIndexMapper.class);
        //为了防止JVM消耗过大，分次查询，每次查询插入五十万条
        boolean isPageHelp = true;
        int pageNum = 1;
        int pageSize = 500000;
        int keyToken = 0;
        while (isPageHelp) {
            PageHelper.startPage(pageNum, pageSize, false, false,null);
            List<PKIndexEvaluationEntity> getTeachNoteList = pkIndexMapper.getTeachNoteList();
            if (getTeachNoteList != null && getTeachNoteList.size() > 0) {
                //第一次遍历时删除报表中旧数据
                if (pageNum == 1) {
                    int deleteCount = pkIndexMapper.deleteTeachNoteList();
                    log.info("删除学习笔记报表数据:"+ deleteCount +"条");
                }
                //为了防止一次插入数据JVM消耗过大，分次插入，每次插入一万条
                int listSize=getTeachNoteList.size();
                int toIndex=10000;
                for(int i = 0;i<listSize;i+=10000){
                    if(i+10000>listSize){
                        toIndex=listSize-i;
                    }
                    List newList = getTeachNoteList.subList(i,i+toIndex);
                    keyToken++;
                    pkIndexMapper.saveTeachNoteList(newList);
                    log.info("第"+keyToken+"次"+"插入学习笔记报表数据:"+ newList.size() +"条");
                }
            } else {
                break;
            }
            //避免再进行最后一次查询消耗性能
            if(getTeachNoteList.size()< pageSize){
                break;
            }else {
                pageNum++;
            }
        }
        sqlSession.commit();
        log.info("运行时间=" + (System.currentTimeMillis() - start));
    }
}
