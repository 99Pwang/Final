package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@ApiModel(value = "Jobs_Object", description = "")
public class Jobs implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @Alias("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("Position_Name")
    @Alias("Position_Name")
    private String name;

    @ApiModelProperty("Expected_Number")
    @Alias("Expected_Number")
    private Integer nums;

    @ApiModelProperty("Position_Description")
    @Alias("Position_Description")
    private String content;

    @ApiModelProperty("Cover")
    @Alias("Cover")
    private String img;

    @ApiModelProperty("Publish_Time")
    @Alias("Publish_Time")
    private String time;

    @ApiModelProperty("Subject")
    @Alias("Subject")
    private String department;

    @TableField(exist = false)
    private Long submitNums;

}
