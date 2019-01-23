package com.hq.bi.offline.task.service;

import com.github.pagehelper.PageHelper;
import com.hq.bi.offline.task.factory.MybatisSessionFactory;
import com.hq.bi.offline.task.mapper.LiveEvaluateMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

/**
 * Description:直播评价管理
 * Autor: hzr
 * Date: 2018-12-11-9:03
 */
@Slf4j
public class LiveEvaluateService {
    public static  void main(String[] args) {
        long start = System.currentTimeMillis();

        SqlSessionFactory sqlSessionFactory = MybatisSessionFactory.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        LiveEvaluateMapper liveEvaluateMapper = sqlSession.getMapper(LiveEvaluateMapper.class);

        //为了防止JVM消耗过大，分次查询，每次查询插入五十万条
        boolean isPageHelp = true;
        int pageNum = 1;
        int pageSize = 500000;
        int keyToken = 0;
        while (isPageHelp) {
            PageHelper.startPage(pageNum, pageSize);
            List<Map> list = liveEvaluateMapper.getLiveEvaluateList();
            if (list != null && list.size() > 0) {
                //第一次遍历时删除报表中旧数据
                if (pageNum == 1) {
                    int deleteCount = liveEvaluateMapper.deleteLiveEvaluate();
                    log.info("删除直播评价报表数据:" + deleteCount + "条");
                }
                //为了防止一次插入数据JVM消耗过大，分次插入，每次插入一万条
                int listSize = list.size();
                int toIndex = 10000;
                for (int i = 0; i < listSize; i += 10000) {
                    if (i + 10000 > listSize) {
                        toIndex = listSize - i;
                    }
                    List newList = list.subList(i, i + toIndex);
                    keyToken++;
                    liveEvaluateMapper.saveLiveEvaluate(newList);
                    log.info("第" + keyToken + "次" + "插入直播评价报表数据:" + newList.size() + "条");
                }
            } else {
                break;
            }
            //避免再进行最后一次查询消耗性能
            if (list.size() < pageSize) {
                break;
            } else {
                pageNum++;
            }
        }
        sqlSession.commit();
        log.info("运行时间=" + (System.currentTimeMillis() - start));
    }
}
