package com.hq.learningcenter.school.pojo;

import java.io.Serializable;

/**
 * 学习中心左侧菜单
 * 
 * @class LcMenuPOJO.java
 * @Description:
 * @author shihongjie
 * @dete 2017年9月1日
 */
public class LcMenuPOJO implements Serializable{

	private String name;
	private String url;
	private String pic;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

    @Override
    public String toString() {
        return "{" +
                "name:'" + name + '\'' +
                ", url:'" + url + '\'' +
                ", pic:'" + pic + '\'' +
                '}';
    }
}
