package com.zlq.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.zlq.mall.common.valid.ListValue;
import com.zlq.mall.common.valid.groups.AddGroupsInterface;
import com.zlq.mall.common.valid.groups.UpdateGroupsInterface;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 *
 * @author zhangliqun
 * @email yuzexi0727@gmail.com
 * @date 2022-11-19 15:29:27
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 品牌id
     */
    @TableId
    @Null(message = "品牌id必须为空", groups = AddGroupsInterface.class)
    @NotNull(message = "品牌id不能为空", groups = UpdateGroupsInterface.class)
    private Long brandId;
    /**
     * 品牌名
     */
    @NotBlank(message = "品牌名称不能为空", groups = AddGroupsInterface.class)
    private String name;
    /**
     * 品牌logo地址
     */
    @NotBlank(message = "logo不能为空", groups = AddGroupsInterface.class)
    @URL(message = "logo必须是一个合法URL地址", groups = {AddGroupsInterface.class, UpdateGroupsInterface.class})
    private String logo;
    /**
     * 介绍
     */
    private String descript;
    /**
     * 显示状态[0-不显示；1-显示]
     */
    @ListValue(message = "显示状态必须输0或1", val = {0, 1},groups = {AddGroupsInterface.class,UpdateGroupsInterface.class})
	@NotNull(message = "显示状态不能为空",groups = AddGroupsInterface.class)
    private Integer showStatus;
    /**
     * 检索首字母
     */
    @NotBlank(message = "检索首字母不能为空", groups = AddGroupsInterface.class)
    @Pattern(regexp = "/^[a-zA-Z]$/", message = "检索首字母必须是单个的字母")
    private String firstLetter;
    /**
     * 排序
     */
    @NotNull(message = "排序不能为null", groups = AddGroupsInterface.class)
    @Min(value = 0, message = "排序不能小于0", groups = {AddGroupsInterface.class, UpdateGroupsInterface.class})
    private Integer sort;

}
