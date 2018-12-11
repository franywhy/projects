package com.hq.courses.service;
@FunctionalInterface
public interface ImportExcelService<T> {
	/**
	 * 保存excel信息
	 * @param dataItem
	 * @param line
	 * @return
	 */
	public T execute(String[] dataItem,int line);
}
