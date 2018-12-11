package com.izhubo.web.mobile


import org.json.JSONArray;
import javax.servlet.http.HttpServletRequest
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.izhubo.rest.AppProperties
import com.izhubo.rest.common.util.JSONUtil
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import com.izhubo.rest.anno.RestWithSession;
import com.izhubo.rest.common.util.http.HttpClientUtil4_3;
import com.izhubo.web.BaseController
import org.springframework.beans.factory.annotation.Value
import com.wordnik.swagger.annotations.ApiOperation
@RestWithSession
class CourseListController extends BaseController {

    private static Logger logger = LoggerFactory
            .getLogger(CourseListController.class);

    @Value("#{application['hqonlineh5.domain']}")
    private String hqonlineh5_domain ="http://wap.hqjy.com/";

    @Value("#{application['us.domain']}")
    private String us_domain ="http://passport.hqjy.com/";


    private String courselist_action="lib/coursetype/course_type.ashx";

    /**
     * 课程列表接口
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "course_list/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "课程列表接口", httpMethod = "GET",  notes = "恒企会计3.0课程列表接口")
    def course_list(HttpServletRequest request){

        String token = request["access_token"];
        try {
            hqonlineh5_domain = hqonlineh5_domain.trim();
            String reqResult = HttpClientUtil4_3.get(hqonlineh5_domain+courselist_action,null);
            Map _jsonMap = JSONUtil.jsonToMap(reqResult);
            String picdomain =  _jsonMap.get("imgdomain");
            _jsonMap.get("course_list").each { def item ->
                String picurl = item["PIC"];
                String courseurl =hqonlineh5_domain+ item["TYPEID"].toString()+".html";
                item["PIC"] = picdomain+picurl;
                item["course_url"] =  us_domain +"/redirect_to_other?access_token="+token+"&rd_url="+courseurl;
            }
            getResultOK( _jsonMap.get("course_list"));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            getResultOK();
        }





    }







}


