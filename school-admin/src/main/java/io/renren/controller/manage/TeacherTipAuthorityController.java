package io.renren.controller.manage;

import com.alibaba.fastjson.JSONObject;
import io.renren.common.doc.ParamKey;
import io.renren.common.doc.SysLog;
import io.renren.controller.AbstractController;
import io.renren.entity.manage.TeacherTipAuthority;
import io.renren.utils.HttpUtils;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.http.HttpClientUtil4_3;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/7 0007.
 * @author Evan
 */
@Controller
@RequestMapping("/teacherTipAuthority")
public class TeacherTipAuthorityController extends AbstractController {

    @Value("#{application['kuaida.api']}")
    private String KUAIDA_API;

    /**
     * 列表
     */
    @ResponseBody
    @RequestMapping("/list")
    @RequiresPermissions("teacherTipAuthority:list")
    public R list(HttpServletRequest request) throws Exception {
        String mobile = ServletRequestUtils.getStringParameter(request,"mobile","").trim();
        int answerPermission = ServletRequestUtils.getIntParameter(request,"answerPermission",-1);
        int currPage = ServletRequestUtils.getIntParameter(request,  ParamKey.In.PAGE, 1);
        int pageSize = ServletRequestUtils.getIntParameter(request,  ParamKey.In.LIMIT, ParamKey.In.DEFAULT_MAX_LIMIT);

        Map<String,String> params = new HashMap<>();
        params.put("mobile",mobile);
        params.put("answerPermission",answerPermission+"");
        params.put("currPage",currPage+"");
        params.put("pageSize",pageSize+"");

        String result = HttpClientUtil4_3.post(KUAIDA_API+"/api/teacherTipAuthority/getTeacherList",params,null);
        logger.info("url："+KUAIDA_API+"/api/teacherTipAuthority/getTeacherList");
        logger.info("params："+params.toString());
        logger.info("result："+result);
        int total = 0;
        List<TeacherTipAuthority> list = null;
        if(StringUtils.isNotBlank(result)) {
            JSONObject object = JSONObject.parseObject(result);
            if(null != object && 1 == object.getIntValue("code")) {
                JSONObject data = object.getJSONObject("data");
                total = data.getIntValue("total");
                list = JSONObject.parseArray(data.getString("list"),TeacherTipAuthority.class);
            }
        }
        PageUtils pageUtil = new PageUtils(list,total,pageSize,currPage);
        return R.ok().put(pageUtil);
    }


    /**
     * 修改教师标签权限
     */
    @SysLog("修改教师标签权限")
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("teacherTipAuthority:update")
    public R update(@RequestBody String teacherTipAuthority) throws IOException{

        long userId = getUserId();
        String result = HttpClientUtil4_3.postStr(KUAIDA_API+"/api/teacherTipAuthority/updateUserTip?userId="+userId,teacherTipAuthority,null);
        logger.info("url："+KUAIDA_API+"/api/teacherTipAuthority/updateUserTip");
        logger.info("params："+teacherTipAuthority);
        logger.info("result："+result);

        return R.ok(result);
    }
}
