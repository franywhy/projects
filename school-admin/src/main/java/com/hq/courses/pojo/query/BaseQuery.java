package com.hq.courses.pojo.query;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import io.renren.common.doc.ParamKey;

/**  
 * 类说明   
 *  
 * @author shihongjie
 * @email  shihongjie@hengqijy.com
 * @date 2017年11月8日
 */
public abstract class BaseQuery {
	
	protected static final String DEFAULT_LIMIT_STRING = "10";
	protected static final String DEFAULT_PAGE_STRING = "1";
	
	/** 分页参数 */
	private Integer offset;
	private Integer limit;
	private String sidx;
	private String order;
	private Integer _page;
	
//	/**
//	 * 初始化参数
//	 * @param params
//	 * @return
//	 */
//	public BaseQuery initPage(Map<String , Object> params) {
//		//查询列表数据
//		limit= Integer.valueOf((String)params.getOrDefault(ParamKey.In.LIMIT, DEFAULT_LIMIT_STRING) , 1000);
//		_page = Integer.valueOf((String)params.getOrDefault(ParamKey.In.PAGE, DEFAULT_PAGE_STRING) , 1);
//		sidx = (String) params.getOrDefault(ParamKey.In.SIDX, null);
//		order = (String) params.getOrDefault(ParamKey.In.ORDER, null);
//		offset = (_page - 1) * limit;
//		return this;
//	}
	/**
	 * 初始化参数
	 * @param params
	 * @return
	 */
	public BaseQuery initPage(HttpServletRequest request) {
		//查询列表数据
		Integer page = ServletRequestUtils.getIntParameter(request,  ParamKey.In.PAGE, 1);
		limit = ServletRequestUtils.getIntParameter(request,  ParamKey.In.LIMIT, ParamKey.In.DEFAULT_MAX_LIMIT);
		_page = ServletRequestUtils.getIntParameter(request,  ParamKey.In.PAGE, 1);
		try {
			sidx = ServletRequestUtils.getStringParameter(request , ParamKey.In.SIDX);
		} catch (ServletRequestBindingException e) {
			e.printStackTrace();
		}
		try {
			order = ServletRequestUtils.getStringParameter(request , ParamKey.In.ORDER);
		} catch (ServletRequestBindingException e) {
			e.printStackTrace();
		}
		offset = (page - 1) * limit;
		return this;
	}
	
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public String getSidx() {
		return sidx;
	}
	public void setSidx(String sidx) {
		this.sidx = sidx;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public Integer get_page() {
		return _page;
	}
	public void set_page(Integer _page) {
		this._page = _page;
	}
	
	
}
