package io.renren.controller.manage;

import com.alibaba.fastjson.JSONObject;
import io.renren.controller.AbstractController;
import io.renren.entity.manage.BunusQueAns;
import io.renren.entity.manage.TipQueAns;
import io.renren.service.SysUserLaberService;
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
@RequestMapping("/bunusQueAns")
public class BunusQueAnsController extends AbstractController {

    @Value("#{application['kuaida.api']}")
    private String KUAIDA_API;

    @Autowired
    private SysUserLaberService sysUserLaberService;

    /**
     * 列表
     */
    @ResponseBody
    @RequestMapping("/list")
    @RequiresPermissions("bunusQueAns:list")
    public R list(HttpServletRequest request) throws Exception {
        String startTimeText = ServletRequestUtils.getStringParameter(request,"startTime","");
        String endTimeText = ServletRequestUtils.getStringParameter(request,"endTime","");
        String childTipIds = ServletRequestUtils.getStringParameter(request,"childTipIds","");

        String hasParentTipIds = sysUserLaberService.queryLaberByUserId(this.getUserId());

        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Long startTime = sdf.parse(startTimeText).getTime();
        Long endTime = sdf.parse(endTimeText).getTime();

        Map<String,String> params = new HashMap<>();
        params.put("startTime",startTime+"");
        params.put("endTime",endTime+"");
        params.put("childTipIds",childTipIds);
        params.put("hasParentTipIds",hasParentTipIds);

        String result = HttpClientUtil4_3.post(KUAIDA_API+"/api/accountQueAns/bunusTopic",params,null);
        logger.info("url："+KUAIDA_API+"/api/accountQueAns/bunusTopic");
        logger.info("params："+params.toString());
        logger.info("result："+result);
        List<BunusQueAns> list = null;
        if(StringUtils.isNotBlank(result)) {
            JSONObject object = JSONObject.parseObject(result);
            if(null != object && 1 == object.getIntValue("code")) {
                list = JSONObject.parseArray(object.getString("data"), BunusQueAns.class);
            }
            JSONObject.parseObject(result, TipQueAns.class);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("list",list);
        return R.ok().put(map);
    }

}
