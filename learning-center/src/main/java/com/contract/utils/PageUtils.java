package com.contract.utils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 分页工具类
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月4日 下午12:59:00
 */
public class PageUtils implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE = "page";
	private static final String PAGESIZE = "pageSize";
	private static final String LIMIT = "limit";
	private static final String OFFSET = "offset";

	//总记录数
	private int totalCount;
	//每页记录数
	//固定每页记录数
	private static int pageSize = 10;

	//总页数
	private int totalPage;
	//当前页数
	private int currPage;
	//列表数据
	private List<?> list;
	
	/**
	 * 分页
	 * @param list        列表数据
	 * @param totalCount  总记录数
	 */
	public PageUtils(List<?> list, int totalCount, Integer page) {
		this.list = list;
		this.totalCount = totalCount;
		//this.pageSize = pageSize;
		this.currPage = page;
		this.totalPage = (int)Math.ceil((double)totalCount/pageSize);
	}

	public static Map<String,Object> pageMap(Map<String,Object> map,Integer page){
		Integer currPage = page;
		Integer currPageSize = pageSize;
		map.put(OFFSET,(currPage - 1) * currPageSize);
		map.put(LIMIT,currPageSize);
		return map;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	/*public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}*/

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}
	
}
