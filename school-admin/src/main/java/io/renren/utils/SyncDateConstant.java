package io.renren.utils;

public enum SyncDateConstant {
	course_classplan("course_classplan"),
	course_userplan_class_detail("course_userplan_class_detail"),
	course_userplan("course_userplan"),
	course_userplan_xlxw("course_userplan_xlxw"),
    user_oldClass_log("user_oldClass_log"),
	nc_course_classplan("nc_course_classplan")
	;
	private String tableName;
	private SyncDateConstant(String tableName) {
		this.tableName = tableName;
	}
	public String getTableName() {
		return tableName;
	}
}
