package com.ray.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "查询标签dto")
public class TagListDto {
    private Long id;

    //标签名
    @ApiModelProperty(notes = "标签名")
    private String name;

    private Long createBy;

    private Date createTime;

    private Long updateBy;

    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    @ApiModelProperty(notes = "删除标志")
    private Integer delFlag;
    //备注
    @ApiModelProperty(notes = "备注")
    private String remark;

}
