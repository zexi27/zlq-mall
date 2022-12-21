package com.zlq.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlq.mall.common.utils.OperationResult;
import com.zlq.mall.common.utils.PageUtils;
import com.zlq.mall.common.utils.R;
import com.zlq.mall.product.entity.CategoryBrandRelationEntity;

import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author zhangliqun
 * @email yuzexi0727@gmail.com
 * @date 2022-11-19 15:29:27
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    OperationResult addCategoryBrandRelationEntity(Long brandId, Long categoryId);

    OperationResult updateCategoryBrandRelation(Integer updateStyle, Long updateId, String updateName);

    OperationResult listBrand(Long catId);
}

