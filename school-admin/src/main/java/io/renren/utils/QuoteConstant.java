package io.renren.utils;

public enum QuoteConstant {
    course_material_course_id("course_material" , "course_id" , false),
    course_book_course_id("course_book" , "course_id" , true),
    course_classplan_course_id("course_classplan" , "course_id" , true),
    course_exam_schedule_course_id("course_exam_schedule" , "course_id" , true),
    course_record_detail_course_id("course_record_detail" , "course_id" , false),
    course_textbook_course_id("course_textbook" , "course_id" , false),
    course_userplan_detail_course_id("course_userplan_detail" , "course_id" , true),
    mall_goods_detail_course_id("mall_goods_details" , "course_id" , true),
    course_userplan_class_type_id("course_userplan" , "class_type_id" , true),
    course_userplan_profession_id("course_userplan" , "profession_id" , true),
    mall_order_commodity_id("mall_order","commodity_id",true),
    mall_order_class_id("mall_order","class_id",true),
    mall_class_profession_id("mall_class","profession_id",true),
    course_userplan_class_id("course_userplan","class_id",true),
    mall_order_user_id("mall_order","user_id",true),
    course_userplan_user_id("course_userplan","user_id",true),
    course_material_detail_material_type_id("course_material_detail","material_type_id",false),
    course_userplan_class_detail_userplan_detail_id("course_userplan_class_detail","userplan_detail_id",true),
    course_userplan_class_detail_classplan_id("course_userplan_class_detail","classplan_id",true),
    contract_template_id("mall_goods_info","contract_template_id",true)
    ;
    //字段名
    private String columnName;
    //表名
    private String tableName;
    //检验是否软删除
    private boolean dbs;

    private QuoteConstant(String tableName , String columnName , boolean dbs) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.dbs = dbs;
    }
    public String getColumnName() {
        return columnName;
    }
    public String getTableName() {
        return tableName;
    }
    public boolean isDbs() {
        return dbs;
    }


}
