package com.hq.learningapi.dao;

import com.hq.learningapi.entity.AppGuideEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Glenn on 2017/5/2 0002.
 */

@Repository
public interface AppGuideDao {


    List<AppGuideEntity> queryList(@Param("areaIdList") List<Long> areaIdList,
                                   @Param("professionIdList") List<Long> professionIdList,
                                   @Param("levelIdList") List<Long> levelIdList,
                                   @Param("schoolId") String schoolId,
                                   @Param("status") Integer status);

    List<AppGuideEntity> queryList0(@Param("professionId") Long professionId,
                                    @Param("areaId") Long areaId,
                                    @Param("levelId") Long levelId,
                                    @Param("schoolId") String schoolId,
                                    @Param("status") Integer status);
}
