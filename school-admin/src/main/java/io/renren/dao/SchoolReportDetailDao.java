package io.renren.dao;

import io.renren.entity.SchoolReportDetailEntity;
import io.renren.pojo.ClassPlanLivesDetailPOJO;
import io.renren.pojo.SchoolReportUserMessagePOJO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-03-17 16:09:03
 */
@Mapper
public interface SchoolReportDetailDao extends BaseDao<SchoolReportDetailEntity> {

    List<SchoolReportUserMessagePOJO> queryUserMessage();

    List<String> classPlanIdByOrder(Long orderId);

    List<ClassPlanLivesDetailPOJO> getClassPlanLivesDetail(@Param("orderId") Long orderId,@Param("userId") Long userId, @Param("startDate") String startDateStr,  @Param("endDate")String endDateStr);
}
