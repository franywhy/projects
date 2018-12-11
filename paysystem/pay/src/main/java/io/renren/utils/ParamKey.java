package io.renren.utils;

public interface ParamKey {
	interface In {
		
		Integer DEFAULT_LIMIT = 10;
		Integer DEFAULT_MAX_LIMIT = 1000;
		Integer PAGE_MAX_LIMIT = 5000;
		String PAGE = "page";
		
		String LIMIT = "limit";
		
		String OFFSET = "offset";
		
		String SCHOOLID = "schoolId";
	}

	interface Out {

		String code = "code";

		String data = "data";

		Integer SUCCESS = 0;

	}
}
