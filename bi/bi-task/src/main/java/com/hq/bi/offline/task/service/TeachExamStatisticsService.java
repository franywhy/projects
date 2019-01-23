package com.hq.bi.offline.task.service;


import com.hq.bi.offline.task.entity.TeachExamStatisticsEntity;
import com.hq.bi.offline.task.factory.MybatisSessionFactory;
import com.hq.bi.offline.task.mapper.TeachExamStatisticsMapper;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tobias
 */


public class TeachExamStatisticsService {

  private static TeachExamStatisticsMapper teachExamStatisticsMapper;
  private static final int PAGE_SIZE = 10000;
  private Logger log = LoggerFactory.getLogger(getClass());

  static {
    SqlSessionFactory sqlSessionFactory = MybatisSessionFactory.getSqlSessionFactory();
    SqlSession sqlSession = sqlSessionFactory.openSession(true);
    teachExamStatisticsMapper = sqlSession.getMapper(TeachExamStatisticsMapper.class);
  }

  public static void main(String[] args) {
    TeachExamStatisticsService teachExamStatisticsService = new TeachExamStatisticsService();
    teachExamStatisticsService.updateData();
  }

  /**
   * 定时每四个小时更新考试统计数据
   */
  private void updateData() {

    Integer total = teachExamStatisticsMapper.getTotal();

    int pageNum;
    if (total % PAGE_SIZE == 0) {
      pageNum = total / PAGE_SIZE;
    } else {
      pageNum = total / PAGE_SIZE + 1;
    }

    if (total > 0) {
      teachExamStatisticsMapper.delete();
      log.info("清除考试统计报表数据");

      for (int n = 1; n <= pageNum; n++) {
        List<TeachExamStatisticsEntity> examStatistics = teachExamStatisticsMapper
            .getExamStatistics((n - 1) * PAGE_SIZE, PAGE_SIZE);
        teachExamStatisticsMapper.insert(examStatistics);
        log.info("第" + n + "次" + "插入考试统计报表数据:" + examStatistics.size() + "条");
        examStatistics.clear();
      }
    }

  }
}


