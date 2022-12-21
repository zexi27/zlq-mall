package com.zlq.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zlq.mall.common.utils.OperationResult;
import com.zlq.mall.common.utils.R;
import com.zlq.mall.product.constant.BrandRelationConstant;
import com.zlq.mall.product.constant.CategoryConstant;
import com.zlq.mall.product.constant.UpdateCategoryBrandEnum;
import com.zlq.mall.product.dao.BrandDao;
import com.zlq.mall.product.dao.CategoryDao;
import com.zlq.mall.product.entity.BrandEntity;
import com.zlq.mall.product.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlq.mall.common.utils.PageUtils;
import com.zlq.mall.common.utils.Query;

import com.zlq.mall.product.dao.CategoryBrandRelationDao;
import com.zlq.mall.product.entity.CategoryBrandRelationEntity;
import com.zlq.mall.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    private BrandDao brandDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private CategoryBrandRelationDao categoryBrandRelationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OperationResult addCategoryBrandRelationEntity(Long brandId, Long categoryId) {
        BrandEntity brandEntity = brandDao.selectById(brandId);
        if (Objects.isNull(brandEntity)) {
            return OperationResult.error(BrandRelationConstant.CATEGORY_NOT_EXIST);
        }
        CategoryEntity categoryEntity = categoryDao.selectById(categoryId);
        if (Objects.isNull(categoryEntity)) {
            return OperationResult.error(CategoryConstant.CATEGORY_NOT_EXIST);
        }
        // 查询品牌分类关联表中是否有重复的分类
        List<CategoryBrandRelationEntity> categoryBrandRelationEntityList = categoryBrandRelationDao.selectList(new LambdaQueryWrapper<CategoryBrandRelationEntity>()
                .eq(CategoryBrandRelationEntity::getCatelogId, categoryId)
                .eq(CategoryBrandRelationEntity::getBrandId, brandId));
        if (!categoryBrandRelationEntityList.isEmpty()) {
            return OperationResult.error(BrandRelationConstant.BRAND_RELATION_EXIST);
        }
        CategoryBrandRelationEntity entity = CategoryBrandRelationEntity
                .builder()
                .brandId(brandId)
                .catelogId(categoryId)
                .catelogName(categoryEntity.getName())
                .brandName(brandEntity.getName())
                .build();
        if (categoryBrandRelationDao.insert(entity) != 1) {
            return OperationResult.error();
        }
        return OperationResult.ok();
    }

    @Override
    public OperationResult updateCategoryBrandRelation(Integer updateStyle, Long updateId, String updateName) {
        List<CategoryBrandRelationEntity> categoryBrandRelationList = null;
        if (updateStyle == UpdateCategoryBrandEnum.UPDATE_CATEGORY.getCode()) {
            categoryBrandRelationList = categoryBrandRelationDao.selectList(new LambdaQueryWrapper<CategoryBrandRelationEntity>()
                    .eq(CategoryBrandRelationEntity::getCatelogId, updateId));
            if (!categoryBrandRelationList.isEmpty()) {
                for (CategoryBrandRelationEntity entity : categoryBrandRelationList) {
                    entity.setCatelogName(updateName);
                }

            }
        } else {
            categoryBrandRelationList = categoryBrandRelationDao.selectList(new LambdaQueryWrapper<CategoryBrandRelationEntity>()
                    .eq(CategoryBrandRelationEntity::getBrandId, updateId));
            if (!categoryBrandRelationList.isEmpty()) {
                for (CategoryBrandRelationEntity entity : categoryBrandRelationList) {
                    entity.setBrandName(updateName);
                }
            }
        }
        if (!this.updateBatchById(categoryBrandRelationList, categoryBrandRelationList.size())) {
            return OperationResult.error(BrandRelationConstant.UPDATE_BRAND_RELATION_FAIL);
        }
        return OperationResult.ok();
    }

    @Override
    public OperationResult listBrand(Long catId) {
        List<CategoryBrandRelationEntity> list = categoryBrandRelationDao.selectList(new LambdaQueryWrapper<CategoryBrandRelationEntity>()
                .eq(CategoryBrandRelationEntity::getCatelogId, catId));
        return OperationResult.ok().put("data",list);
    }


}