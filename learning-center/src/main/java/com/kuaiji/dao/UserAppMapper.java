package com.kuaiji.dao;

import com.kuaiji.entity.UserApp;
import com.kuaiji.entity.UserAppExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface UserAppMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_app
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    int countByExample(UserAppExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_app
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    int deleteByExample(UserAppExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_app
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    int deleteByPrimaryKey(Integer userappid);

    int insertBatch(List<UserApp> list);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_app
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    int insert(UserApp record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_app
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    int insertSelective(UserApp record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_app
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    List<UserApp> selectByExampleWithRowbounds(UserAppExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_app
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    List<UserApp> selectByExample(UserAppExample example);

    UserApp selectByExampleFetchOne(UserAppExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_app
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    UserApp selectByPrimaryKey(Integer userappid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_app
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    int updateByExampleSelective(@Param("record") UserApp record, @Param("example") UserAppExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_app
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    int updateByExample(@Param("record") UserApp record, @Param("example") UserAppExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_app
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    int updateByPrimaryKeySelective(UserApp record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_app
     *
     * @mbggenerated Mon Aug 28 09:16:43 CST 2017
     */
    int updateByPrimaryKey(UserApp record);
}