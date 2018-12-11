package io.renren.service.manage;

import io.renren.utils.PageUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/12/7 0007.
 */
public interface ActivityJoinInfoService {

    PageUtils findPage(HttpServletRequest request);

    void updateStatusById(int[] ids, int status);
}
