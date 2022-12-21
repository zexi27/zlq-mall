package com.zlq.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlq.mall.common.utils.OperationResult;
import com.zlq.mall.common.utils.PageUtils;
import com.zlq.mall.common.utils.R;
import com.zlq.mall.product.dto.CategoryTreeDTO;
import com.zlq.mall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author zhangliqun
 * @email yuzexi0727@gmail.com
 * @date 2022-11-19 15:29:27
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryTreeDTO> listTree();

    OperationResult deleteCategory(List<Long> ids);


    OperationResult updateCategory(CategoryEntity category);


    /**
     * 获取该分类id的路径
     * @param catelogId
     * @return
     */
    Long[] getAttrCatelogPath(Long catelogId);
}

