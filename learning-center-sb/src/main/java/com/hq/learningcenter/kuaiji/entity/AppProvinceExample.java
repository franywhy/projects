package com.hq.learningcenter.kuaiji.entity;

import java.util.ArrayList;
import java.util.List;

public class AppProvinceExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table bd_app_province
     *
     * @mbggenerated Mon Aug 14 09:39:43 CST 2017
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table bd_app_province
     *
     * @mbggenerated Mon Aug 14 09:39:43 CST 2017
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table bd_app_province
     *
     * @mbggenerated Mon Aug 14 09:39:43 CST 2017
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bd_app_province
     *
     * @mbggenerated Mon Aug 14 09:39:43 CST 2017
     */
    public AppProvinceExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bd_app_province
     *
     * @mbggenerated Mon Aug 14 09:39:43 CST 2017
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bd_app_province
     *
     * @mbggenerated Mon Aug 14 09:39:43 CST 2017
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bd_app_province
     *
     * @mbggenerated Mon Aug 14 09:39:43 CST 2017
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bd_app_province
     *
     * @mbggenerated Mon Aug 14 09:39:43 CST 2017
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bd_app_province
     *
     * @mbggenerated Mon Aug 14 09:39:43 CST 2017
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bd_app_province
     *
     * @mbggenerated Mon Aug 14 09:39:43 CST 2017
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bd_app_province
     *
     * @mbggenerated Mon Aug 14 09:39:43 CST 2017
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bd_app_province
     *
     * @mbggenerated Mon Aug 14 09:39:43 CST 2017
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bd_app_province
     *
     * @mbggenerated Mon Aug 14 09:39:43 CST 2017
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bd_app_province
     *
     * @mbggenerated Mon Aug 14 09:39:43 CST 2017
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table bd_app_province
     *
     * @mbggenerated Mon Aug 14 09:39:43 CST 2017
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andNcProvinceCodeIsNull() {
            addCriterion("nc_province_code is null");
            return (Criteria) this;
        }

        public Criteria andNcProvinceCodeIsNotNull() {
            addCriterion("nc_province_code is not null");
            return (Criteria) this;
        }

        public Criteria andNcProvinceCodeEqualTo(String value) {
            addCriterion("nc_province_code =", value, "ncProvinceCode");
            return (Criteria) this;
        }

        public Criteria andNcProvinceCodeNotEqualTo(String value) {
            addCriterion("nc_province_code <>", value, "ncProvinceCode");
            return (Criteria) this;
        }

        public Criteria andNcProvinceCodeGreaterThan(String value) {
            addCriterion("nc_province_code >", value, "ncProvinceCode");
            return (Criteria) this;
        }

        public Criteria andNcProvinceCodeGreaterThanOrEqualTo(String value) {
            addCriterion("nc_province_code >=", value, "ncProvinceCode");
            return (Criteria) this;
        }

        public Criteria andNcProvinceCodeLessThan(String value) {
            addCriterion("nc_province_code <", value, "ncProvinceCode");
            return (Criteria) this;
        }

        public Criteria andNcProvinceCodeLessThanOrEqualTo(String value) {
            addCriterion("nc_province_code <=", value, "ncProvinceCode");
            return (Criteria) this;
        }

        public Criteria andNcProvinceCodeLike(String value) {
            addCriterion("nc_province_code like", value, "ncProvinceCode");
            return (Criteria) this;
        }

        public Criteria andNcProvinceCodeNotLike(String value) {
            addCriterion("nc_province_code not like", value, "ncProvinceCode");
            return (Criteria) this;
        }

        public Criteria andNcProvinceCodeIn(List<String> values) {
            addCriterion("nc_province_code in", values, "ncProvinceCode");
            return (Criteria) this;
        }

        public Criteria andNcProvinceCodeNotIn(List<String> values) {
            addCriterion("nc_province_code not in", values, "ncProvinceCode");
            return (Criteria) this;
        }

        public Criteria andNcProvinceCodeBetween(String value1, String value2) {
            addCriterion("nc_province_code between", value1, value2, "ncProvinceCode");
            return (Criteria) this;
        }

        public Criteria andNcProvinceCodeNotBetween(String value1, String value2) {
            addCriterion("nc_province_code not between", value1, value2, "ncProvinceCode");
            return (Criteria) this;
        }

        public Criteria andKjlProvinceCodeIsNull() {
            addCriterion("kjl_province_code is null");
            return (Criteria) this;
        }

        public Criteria andKjlProvinceCodeIsNotNull() {
            addCriterion("kjl_province_code is not null");
            return (Criteria) this;
        }

        public Criteria andKjlProvinceCodeEqualTo(String value) {
            addCriterion("kjl_province_code =", value, "kjlProvinceCode");
            return (Criteria) this;
        }

        public Criteria andKjlProvinceCodeNotEqualTo(String value) {
            addCriterion("kjl_province_code <>", value, "kjlProvinceCode");
            return (Criteria) this;
        }

        public Criteria andKjlProvinceCodeGreaterThan(String value) {
            addCriterion("kjl_province_code >", value, "kjlProvinceCode");
            return (Criteria) this;
        }

        public Criteria andKjlProvinceCodeGreaterThanOrEqualTo(String value) {
            addCriterion("kjl_province_code >=", value, "kjlProvinceCode");
            return (Criteria) this;
        }

        public Criteria andKjlProvinceCodeLessThan(String value) {
            addCriterion("kjl_province_code <", value, "kjlProvinceCode");
            return (Criteria) this;
        }

        public Criteria andKjlProvinceCodeLessThanOrEqualTo(String value) {
            addCriterion("kjl_province_code <=", value, "kjlProvinceCode");
            return (Criteria) this;
        }

        public Criteria andKjlProvinceCodeLike(String value) {
            addCriterion("kjl_province_code like", value, "kjlProvinceCode");
            return (Criteria) this;
        }

        public Criteria andKjlProvinceCodeNotLike(String value) {
            addCriterion("kjl_province_code not like", value, "kjlProvinceCode");
            return (Criteria) this;
        }

        public Criteria andKjlProvinceCodeIn(List<String> values) {
            addCriterion("kjl_province_code in", values, "kjlProvinceCode");
            return (Criteria) this;
        }

        public Criteria andKjlProvinceCodeNotIn(List<String> values) {
            addCriterion("kjl_province_code not in", values, "kjlProvinceCode");
            return (Criteria) this;
        }

        public Criteria andKjlProvinceCodeBetween(String value1, String value2) {
            addCriterion("kjl_province_code between", value1, value2, "kjlProvinceCode");
            return (Criteria) this;
        }

        public Criteria andKjlProvinceCodeNotBetween(String value1, String value2) {
            addCriterion("kjl_province_code not between", value1, value2, "kjlProvinceCode");
            return (Criteria) this;
        }

        public Criteria andProvinceNameIsNull() {
            addCriterion("province_name is null");
            return (Criteria) this;
        }

        public Criteria andProvinceNameIsNotNull() {
            addCriterion("province_name is not null");
            return (Criteria) this;
        }

        public Criteria andProvinceNameEqualTo(String value) {
            addCriterion("province_name =", value, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameNotEqualTo(String value) {
            addCriterion("province_name <>", value, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameGreaterThan(String value) {
            addCriterion("province_name >", value, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameGreaterThanOrEqualTo(String value) {
            addCriterion("province_name >=", value, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameLessThan(String value) {
            addCriterion("province_name <", value, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameLessThanOrEqualTo(String value) {
            addCriterion("province_name <=", value, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameLike(String value) {
            addCriterion("province_name like", value, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameNotLike(String value) {
            addCriterion("province_name not like", value, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameIn(List<String> values) {
            addCriterion("province_name in", values, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameNotIn(List<String> values) {
            addCriterion("province_name not in", values, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameBetween(String value1, String value2) {
            addCriterion("province_name between", value1, value2, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameNotBetween(String value1, String value2) {
            addCriterion("province_name not between", value1, value2, "provinceName");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table bd_app_province
     *
     * @mbggenerated do_not_delete_during_merge Mon Aug 14 09:39:43 CST 2017
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table bd_app_province
     *
     * @mbggenerated Mon Aug 14 09:39:43 CST 2017
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}