package io.renren.pojo;

import java.io.Serializable;
import java.util.List;



/**
 * 部门管理
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-25 14:21:58
 */
public class BaseTreePOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//部门id
	private Long id;
	private List<?> list;
	//部门名称
	private String name;
	private Object o;

	/**
	 * ztree属性
	 */
	private Boolean open;
	
	private Integer orderNum;
	
	//上级部门ID，一级部门为0
	private long parentId;
	
	private String productName;
	
	private String type
	;
	
	private String url;
	

	public BaseTreePOJO(Long id, long parentId, String name, Boolean open, List<?> list, Object o , String type) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.open = open;
		this.list = list;
		this.o = o;
		this.type = type;
	}

	public BaseTreePOJO(Long id, long parentId, String name, Boolean open, List<?> list, Object o, String url,
			Integer orderNum, String productName , String type) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.open = open;
		this.list = list;
		this.o = o;
		this.url = url;
		this.orderNum = orderNum;
		this.productName = productName;
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public List<?> getList() {
		return list;
	}

	public String getName() {
		return name;
	}

	public Object getO() {
		return o;
	}

	public Boolean getOpen() {
		return open;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public long getParentId() {
		return parentId;
	}

	public String getProductName() {
		return productName;
	}

	public String getType() {
		return type;
	}

	public String getUrl() {
		return url;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setO(Object o) {
		this.o = o;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
