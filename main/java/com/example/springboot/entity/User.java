package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 *
 * </p>
 *
 * @author
 * 
 */
@Getter
@Setter
@TableName("sys_user")
@ApiModel(value = "User_Object", description = "")
@ToString
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("Username")
    private String username;

    @ApiModelProperty("Password")
    private String password;

    @ApiModelProperty("Name")
    private String nickname;

    @ApiModelProperty("Email")
    private String email;

    @ApiModelProperty("Phone")
    private String phone;

    @ApiModelProperty("Address")
    private String address;

    @ApiModelProperty("CreateTime")
    private Date createTime;

    @ApiModelProperty("Avatar")
    private String avatarUrl;

    @ApiModelProperty("Role")
    private String role;

    private String education;

    private String experiences;



}
