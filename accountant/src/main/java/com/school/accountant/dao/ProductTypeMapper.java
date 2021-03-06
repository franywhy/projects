package com.school.accountant.dao;

import com.school.accountant.entity.ProductType;
import com.school.accountant.entity.ProductTypeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ProductTypeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mall_product_type
     *
     * @mbggenerated Tue Aug 01 15:06:53 CST 2017
     */
    int countByExample(ProductTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mall_product_type
     *
     * @mbggenerated Tue Aug 01 15:06:53 CST 2017
     */
    int deleteByExample(ProductTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mall_product_type
     *
     * @mbggenerated Tue Aug 01 15:06:53 CST 2017
     */
    int deleteByPrimaryKey(Integer typeid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mall_product_type
     *
     * @mbggenerated Tue Aug 01 15:06:53 CST 2017
     */
    int insert(ProductType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mall_product_type
     *
     * @mbggenerated Tue Aug 01 15:06:53 CST 2017
     */
    int insertSelective(ProductType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mall_product_type
     *
     * @mbggenerated Tue Aug 01 15:06:53 CST 2017
     */
    List<ProductType> selectByExampleWithRowbounds(ProductTypeExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mall_product_type
     *
     * @mbggenerated Tue Aug 01 15:06:53 CST 2017
     */
    List<ProductType> selectByExample(ProductTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mall_product_type
     *
     * @mbggenerated Tue Aug 01 15:06:53 CST 2017
     */
    ProductType selectByPrimaryKey(Integer typeid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mall_product_type
     *
     * @mbggenerated Tue Aug 01 15:06:53 CST 2017
     */
    int updateByExampleSelective(@Param("record") ProductType record, @Param("example") ProductTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mall_product_type
     *
     * @mbggenerated Tue Aug 01 15:06:53 CST 2017
     */
    int updateByExample(@Param("record") ProductType record, @Param("example") ProductTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mall_product_type
     *
     * @mbggenerated Tue Aug 01 15:06:53 CST 2017
     */
    int updateByPrimaryKeySelective(ProductType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mall_product_type
     *
     * @mbggenerated Tue Aug 01 15:06:53 CST 2017
     */
    int updateByPrimaryKey(ProductType record);
}