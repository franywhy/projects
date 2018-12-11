package com.school.controller;

import com.school.web.model.WrappedResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by DL on 2018/12/1.
 */
@RestController
@RequestMapping("/redis")
@Api(description = "清除redis缓存")
public class RemoveRedisCacheController extends AbstractBaseController{

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("#{application['remove.checkKey']}")
    private String checkKey;

    @Resource
    private StringRedisTemplate mainRedis;

    @ApiOperation(value = "清除redis缓存接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "接口校验值", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "redisKey", value = "redis key值", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/removeCache",method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> remove(HttpServletRequest request, HttpServletResponse response){
        String key = ServletRequestUtils.getStringParameter(request, "key", "");
        String redisKey = ServletRequestUtils.getStringParameter(request, "redisKey", "");
        if (StringUtils.isBlank(key) || !checkKey.equals(key)){
            logger.info("清除缓存失败,校验值checkKey={},参数key={}",checkKey,key);
            return this.fail("接口校验失败,请重试",null);
        }
        if (StringUtils.isBlank(redisKey)){
            logger.info("清除缓存失败,参数redisKey={}",redisKey);
            return this.fail("redisKey不能为空,请重试",null);
        }
        try {
            List<String> keys = new ArrayList<>();
            ScanOptions scanOptions = ScanOptions.scanOptions().match(redisKey + "*").build();
            Cursor<byte[]> scan = mainRedis.getConnectionFactory().getConnection().scan(scanOptions);
            while (scan.hasNext()){
                keys.add(new String(scan.next(),"UTF-8"));
            }
            mainRedis.delete(keys);
            return this.success(redisKey+"清除缓存,执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            return this.fail("服务器错误",e.toString());
        }
    }
}
