package com.hq.bi.search.util;

import lombok.Getter;
import org.apache.commons.collections.MapUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * description: 分页参数
 * @author: hutao
 * @date 2018/12/13 18:12
 */

@Getter
public class PageQuery extends HashMap<String, Object> {
    //当前页
    private static final String PAGENUM = "pageNum";
    //每页的数量
    private static final String PAGESIZE = "pageSize";
    //排序字段
    private static final String SIDX = "sidx";
    //排序方式
    private static final String SORT = "sort";

    private int pageNum;
    private int pageSize;
    private String pageOrder;

    private PageQuery() {
    }

    public static PageQuery build(Map<String, Object> pageParam, Map<String, Object> queryParam) {
        return init(pageParam, queryParam);
    }

    public static PageQuery build(Map<String, Object> pageParam) {
        return init(pageParam, null);
    }

    public static PageQuery buildQuery(Map<String, Object> queryParam) {
        return init(queryParam);
    }

    private static PageQuery init(Map<String, Object> pageParam, Map<String, Object> queryParam) {
        PageQuery pageQuery = new PageQuery();
        pageQuery.putAll(pageParam);
        pageQuery.pageNum = MapUtils.getInteger(pageParam, PAGENUM, 1);
        pageQuery.pageSize = MapUtils.getInteger(pageParam, PAGESIZE, 10);
        pageQuery.put(PAGENUM, pageQuery.pageNum);
        pageQuery.put(PAGESIZE, pageQuery.pageSize);

        String sidx = StringUtils.underscoreName(MapUtils.getString(pageParam, SIDX, ""));
        if (StringUtils.isNotBlank(sidx)) {
            String sort = (MapUtils.getString(pageParam, SORT, "ASC")).toUpperCase().contains("ASC") ? "ASC" : "DESC";
            pageQuery.pageOrder = sidx + " " + sort;
            pageQuery.put(SIDX, sidx);
            pageQuery.put(SORT, sort);
        }

        if (queryParam != null) {
            pageQuery.putAll(queryParam);
            return pageQuery;
        }

        // 简单查询条件
        String queryValue = MapUtils.getString(pageParam, "queryValue");
        if (StringUtils.isNotBlank(queryValue)) {
            pageQuery.put(MapUtils.getString(pageParam, "queryKey"), queryValue);
        }

        // 管理树点击的查询条件
        String treeValue = MapUtils.getString(pageParam, "treeValue");
        if (StringUtils.isNotBlank(treeValue)) {
            pageQuery.put(MapUtils.getString(pageParam, "treeKey"), treeValue);
        }

        return pageQuery;
    }

    private static PageQuery init(Map<String, Object> queryParam) {
        PageQuery pageQuery = new PageQuery();
        if (queryParam != null) {
            pageQuery.putAll(queryParam);
            return pageQuery;
        }
        return pageQuery;
    }


}
