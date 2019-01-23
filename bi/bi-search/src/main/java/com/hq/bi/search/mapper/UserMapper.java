package com.hq.bi.search.mapper;


import com.hq.bi.search.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author chenzeke
 * @date 2018/12/10 16:43
 */
@Mapper
public interface UserMapper {

    /**
     * 根据id查询数据
     *
     * @param id 用户ID
     * @return 返回用户信息实体
     */
    User findUserById(int id);

    /**
     * 查询所有数据
     *
     * @return 返回用户信息列表
     */
    List<User> findAllUser();

    /**
     * 添加User
     *
     * @param user 用户实体
     * @return 返回int
     */
    int addUser(User user);

    /**
     * 根据id删除数据
     *
     * @param id 用户ID
     * @return 返回int
     */
    int deleteUserById(int id);

    /**
     * 修改User
     *
     * @param user 用户实体
     * @return 返回int
     */
    int updateUser(User user);
}
