package io.renren.common.doc;

public interface ParamKey {
	interface In {

		Integer DEFAULT_LIMIT = 10;
		Integer DEFAULT_MAX_LIMIT = 1000;
		Integer PAGE_MAX_LIMIT = 5000;
		String PAGE = "page";
		String SIDX = "sidx";
		String ORDER = "order";

		String LIMIT = "limit";

		String OFFSET = "offset";

		String SCHOOLID = "schoolId";
	}

	interface Out {

		String code = "code";

		String data = "data";

		Integer SUCCESS = 0;

	}

	/**
	 * 数据库事务名称
	 */
	interface Transactional{
		String hq_courses = "transactionManagerCourses";
		String hq_adaptive = "transactionManagerAdaptive";
	}
}
