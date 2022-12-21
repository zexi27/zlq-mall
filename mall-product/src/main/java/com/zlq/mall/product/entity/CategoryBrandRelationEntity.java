package com.zlq.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * 品牌分类关联
 *
 * @author zhangliqun
 * @email yuzexi0727@gmail.com
 * @date 2022-11-19 15:29:27
 */
@Data
@TableName("pms_category_brand_relation")
@Builder
public class CategoryBrandRelationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Long id;
	/**
	 * 品牌id
	 */
	private Long brandId;
	/**
	 * 分类id
	 */
	private Long catelogId;
	/**
	 *
	 */
	private String brandName;
	/**
	 *
	 */
	private String catelogName;

}
