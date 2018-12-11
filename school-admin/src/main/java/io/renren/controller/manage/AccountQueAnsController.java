package io.renren.controller.manage;

import com.alibaba.fastjson.JSONObject;
import io.renren.common.doc.ParamKey;
import io.renren.controller.AbstractController;
import io.renren.entity.manage.AccountQueAns;
import io.renren.service.SysUserLaberService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.http.HttpClientUtil4_3;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/7 0007.
 * @author Evan
 */
@Controller
@RequestMapping("/accountQueAns")
public class AccountQueAnsController extends AbstractController {

    @Value("#{application['kuaida.api']}")
    private String KUAIDA_API;

    @Autowired
    private SysUserLaberService sysUserLaberService;

    /**
     * 列表
     */
    @ResponseBody
    @RequestMapping("/list")
    @RequiresPermissions("accountQueAns:list")
    public R list(HttpServletRequest request) throws Exception {
        String startTimeText = ServletRequestUtils.getStringParameter(request,"startTime","");
        String endTimeText = ServletRequestUtils.getStringParameter(request,"endTime","");
        String mobile = ServletRequestUtils.getStringParameter(request,"mobile","").trim();
        String realName = ServletRequestUtils.getStringParameter(request,"realName","").trim();
        Long teachId = ServletRequestUtils.getLongParameter(request,"teachId",-1);
        String childTipIds = ServletRequestUtils.getStringParameter(request,"childTipIds","");
        int currPage = ServletRequestUtils.getIntParameter(request,  ParamKey.In.PAGE, 1);
        int pageSize = ServletRequestUtils.getIntParameter(request,  ParamKey.In.LIMIT, ParamKey.In.DEFAULT_MAX_LIMIT);

        String hasParentTipIds = sysUserLaberService.queryLaberByUserId(this.getUserId());

        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Long startTime = sdf.parse(startTimeText).getTime();
        Long endTime = sdf.parse(endTimeText).getTime();

        Map<String,String> params = new HashMap<>();
        params.put("startTime",startTime+"");
        params.put("endTime",endTime+"");
        params.put("mobile",mobile);
        params.put("realName",realName);
        params.put("teachId",teachId+"");
        params.put("childTipIds",childTipIds);
        params.put("hasParentTipIds",hasParentTipIds);
        params.put("currPage",currPage+"");
        params.put("pageSize",pageSize+"");

        String result = HttpClientUtil4_3.sendHttpPost(KUAIDA_API+"/api/accountQueAns/teacherTopic",params);
        logger.info("url："+KUAIDA_API+"/api/accountQueAns/teacherTopic");
        logger.info("params："+params.toString());
        logger.info("result："+result);
        int total = 0;
        List<AccountQueAns> list = null;
        if(StringUtils.isNotBlank(result)) {
            JSONObject object = JSONObject.parseObject(result);
            if(null != object && 1 == object.getIntValue("code")) {
                JSONObject data = object.getJSONObject("data");
                total = data.getIntValue("total");
                list = JSONObject.parseArray(data.getString("list"),AccountQueAns.class);
            }
            JSONObject.parseObject(result, AccountQueAns.class);
        }
        PageUtils pageUtil = new PageUtils(list,total,pageSize,currPage);
        return R.ok().put(pageUtil);
    }

}
