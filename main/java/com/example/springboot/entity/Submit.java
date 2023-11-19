package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import cn.hutool.core.annotation.Alias;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 
 */
@Getter
@Setter
@ApiModel(value = "Submit Object", description = "")
public class Submit implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @Alias("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("Position Name")
    @Alias("Position Name")
    private String name;

    @ApiModelProperty("Education")
    @Alias("Education")
    private String education;

    @ApiModelProperty("Experience")
    @Alias("Experience")
    private String experiences;

    @ApiModelProperty("AU Experience")
    @Alias("AU Experience")
    private String isWork;

    @ApiModelProperty("Recommendation Letter")
    @Alias("Recommendation Letter")
    private String isRecommend;

    @ApiModelProperty("CV")
    @Alias("CV")
    private String file;

    @ApiModelProperty("Score")
    @Alias("Score")
    private Double scores;

    @ApiModelProperty("Successful Candidate")
    @Alias("Successful Candidate")
    private String state;

    @ApiModelProperty("Interview Schedule")
    @Alias("Interview Schedule")
    private String time;

    @ApiModelProperty("Contact Information")
    @Alias("Contact Information")
    private String phone;

    @ApiModelProperty("Subject")
    @Alias("Subject")
    private String department;

    private String user;

}
