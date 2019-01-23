package com.hq.bi.search.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author chenzeke
 * @date 2018/12/10 16:38
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "用户信息")
public class User {
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", name = "id")
    private Integer id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", name = "username")
    private String username;

    /**
     * 用户密码
     */
    @ApiModelProperty(value = "用户密码", name = "password")
    private String password;

}
