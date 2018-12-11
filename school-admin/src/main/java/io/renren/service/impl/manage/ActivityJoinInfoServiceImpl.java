package io.renren.service.impl.manage;

import io.renren.common.doc.ParamKey;
import io.renren.dao.manage.ActivityJoinInfoMapper;
import io.renren.entity.manage.ActivityJoinInfo;
import io.renren.entity.manage.ActivityJoinInfoExample;
import io.renren.service.manage.ActivityJoinInfoService;
import io.renren.utils.PageUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/12/7 0007.
 */
@Service
public class ActivityJoinInfoServiceImpl implements ActivityJoinInfoService {

    @Autowired
    private ActivityJoinInfoMapper activityJoinInfoMapper;

    @Override
    public PageUtils findPage(HttpServletRequest request) {
        String mobile = ServletRequestUtils.getStringParameter(request,"mobile","");
        int status = ServletRequestUtils.getIntParameter(request,"status",2);
        String startTime = ServletRequestUtils.getStringParameter(request,"startTime","");
        String endTime = ServletRequestUtils.getStringParameter(request,"endTime","");
        String activityCode = ServletRequestUtils.getStringParameter(request,"activityCode","");
        int currPage = ServletRequestUtils.getIntParameter(request,  ParamKey.In.PAGE, 1);
        int pageSize = ServletRequestUtils.getIntParameter(request,  ParamKey.In.LIMIT, ParamKey.In.DEFAULT_MAX_LIMIT);

        StringBuilder sb = new StringBuilder();
        sb.append("create_time DESC limit ");
        sb.append((currPage-1)*pageSize);
        sb.append(",");
        sb.append(pageSize);

        ActivityJoinInfoExample example = new ActivityJoinInfoExample();
        ActivityJoinInfoExample.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(mobile)) {
            criteria.andMobileEqualTo(mobile);
        }
        if(0 == status || 1 == status) {
            criteria.andStatusEqualTo(status);
        }
        if(StringUtils.isNotBlank(activityCode)) {
            criteria.andActivityCodeEqualTo(activityCode);
        }
        if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                criteria.andCreateTimeBetween(sdf.parse(startTime),sdf.parse(endTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        example.setOrderByClause(sb.toString());

        //查询列表数据
        List<ActivityJoinInfo> ajiList = activityJoinInfoMapper.selectByExample(example);
        int total = activityJoinInfoMapper.countByExample(example);
        PageUtils pageUtil = new PageUtils(ajiList,total,pageSize,currPage);
        return pageUtil;
    }

    @Override
    public void updateStatusById(int[] ids, int status) {
        activityJoinInfoMapper.updateStatusById(ids, status, new Date());
    }
}
