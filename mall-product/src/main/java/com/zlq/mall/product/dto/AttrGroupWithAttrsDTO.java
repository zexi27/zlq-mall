package com.zlq.mall.product.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.zlq.mall.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @ProjectName:zlq-mall
 * @Package:com.zlq.mall.product.dto
 * @ClassName: AttrGroupWithAttrsDTO
 * @description:
 * @author: LiQun
 * @CreateDate:2022/12/21 15:16
 */
@Data
public class AttrGroupWithAttrsDTO {
    /**
     * 分组id
     */
    @TableId
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    // 关联对应的属性信息
    private List<AttrEntity> attrs;
}
