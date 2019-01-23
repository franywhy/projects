package com.hq.bi.search.controller;

import com.hq.bi.search.entity.User;
import com.hq.bi.search.mapper.UserMapper;
import com.hq.bi.search.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author chenzeke
 * @date 2018/12/13 11:47
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户信息管理-UserController")
public class UserController {

    @Resource
    private UserMapper userMapper;

    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据ID查询用户信息")
    @ApiImplicitParam(name = "id", value = "用户ID", example = "1")
    public R info(@PathVariable Integer id) {
        User user = userMapper.findUserById(id);
        return new R<>(user);
    }

    @PostMapping(value = "/add",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "添加用户信息")
    public R add(@RequestBody User user) {
        int result = userMapper.addUser(user);
        return new R<>(result);
    }


}
