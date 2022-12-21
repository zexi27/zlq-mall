package com.zlq.mall.product.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @ProjectName:zlq-mall
 * @Package:com.zlq.mall.product.dto
 * @ClassName: AttrResultDTO
 * @description:
 * @author: LiQun
 * @CreateDate:2022/12/19 22:42
 */
@Data
public class AttrResultDTO {

    /**
     * 属性id
     */
    @TableId
    private Long attrId;
    /**
     * 属性名
     */
    private String attrName;
    /**
     * 是否需要检索[0-不需要，1-需要]
     */
    private Integer searchType;
    /**
     * 属性图标
     */
    private String icon;
    /**
     * 可选值列表[用逗号分隔]
     */
    private String valueSelect;
    /**
     * 属性类型[0-销售属性，1-基本属性，2-既是销售属性又是基本属性]
     */
    private Integer attrType;
    /**
     * 启用状态[0 - 禁用，1 - 启用]
     */
    private Long enable;
    /**
     * 所属分类Id
     */
    private Long catelogId;
    /**
     * 快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整
     */
    private Integer showDesc;

    /**
     * 所属分类名
     */
    private String catelogName;

    /**
     * 所属分组名
     */
    private String groupName;


    private Long[] catelogPath;

    private Long attrGroupId;

}
