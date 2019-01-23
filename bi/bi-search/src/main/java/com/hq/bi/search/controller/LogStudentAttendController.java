package com.hq.bi.search.controller;

import com.hq.bi.search.entity.LogStudentAttendEntity;
import com.hq.bi.search.service.LogStudentAttendService;
import com.hq.bi.search.util.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * @author hq
 */
@RestController
@RequestMapping("/logStudentAttend")
@Slf4j
@Api(tags = "报表查询-LogStudentAttendController")
public class LogStudentAttendController {

    @Autowired
    private LogStudentAttendService logStudentAttendService;

    /**
     * 获取学员考勤报表接口
     * @param map
     * @return
     */
    @PostMapping(value = "/getStudentAttendList",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R getStudentAttendList(@RequestBody Map<String, Object> map){
        List<LogStudentAttendEntity> list = logStudentAttendService.getStudentAttendList(map);
        return new R<>(list);
    }

}
