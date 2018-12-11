package com.school.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Glenn on 2017/7/20 0020.
 */
@Repository
public interface LogWatchDao {

    List<String> getBusinessIdList(@Param("userId") Long userId,
                                   @Param("list") List<String> list,
                                   @Param("attend30") Integer attend30);


    Integer getFlag(@Param("userId") Integer userId,
                    @Param("classPlanLiveId") String classPlanLiveId,
                    @Param("attend30") Integer attend30);

}
