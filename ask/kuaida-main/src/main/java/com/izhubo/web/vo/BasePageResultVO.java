package com.izhubo.web.vo;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class BasePageResultVO<T> extends BaseResultVO<T> {
	

	@ApiModelProperty(value = "总共多少页")
	protected int all_page;

	@ApiModelProperty(value = "总数")
	private int count;
	
	@ApiModelProperty(value = "当前返回第page页")
	private int page;
	
	@ApiModelProperty(value = "每页size条数据")
	private int size;

	public int getAll_page() {
		return all_page;
	}

	public void setAll_page(int all_page) {
		this.all_page = all_page;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
	
	

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "BasePageResultVO [all_page=" + all_page + ", count=" + count + ", page=" + page + ", size=" + size
				+ "]";
	}



	
	
	
}
