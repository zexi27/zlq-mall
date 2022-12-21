package com.zlq.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zlq.mall.common.utils.OperationResult;
import com.zlq.mall.common.utils.R;
import com.zlq.mall.product.constant.BrandRelationConstant;
import com.zlq.mall.product.constant.UpdateCategoryBrandEnum;
import com.zlq.mall.product.dao.CategoryBrandRelationDao;
import com.zlq.mall.product.entity.CategoryBrandRelationEntity;
import com.zlq.mall.product.service.CategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
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

import com.zlq.mall.product.dao.BrandDao;
import com.zlq.mall.product.entity.BrandEntity;
import com.zlq.mall.product.service.BrandService;
import org.springframework.transaction.annotation.Transactional;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Autowired
    private BrandDao brandDao;
    @Autowired
    private CategoryBrandRelationDao categoryBrandRelationDao;
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        IPage<BrandEntity> page = null;
        if (key == null || key == "") {
            page = this.page(
                    new Query<BrandEntity>().getPage(params),
                    new QueryWrapper<BrandEntity>()
            );
        } else {
            page = this.page(
                    new Query<BrandEntity>().getPage(params),
                    new QueryWrapper<BrandEntity>().eq("name", key)
            );

        }

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public OperationResult updateBrand(BrandEntity brand) {
        if (brandDao.updateById(brand) != 1){
            return OperationResult.error("品牌修改失败");
        }
        String brandName = brand.getName();
        if (StringUtils.isNotEmpty(brandName)){
            Long brandId = brand.getBrandId();
            OperationResult result = categoryBrandRelationService.updateCategoryBrandRelation(UpdateCategoryBrandEnum.UPDATE_BRAND.getCode(), brandId,brandName);
            if (!result.isSuccess()){
                return OperationResult.error(BrandRelationConstant.UPDATE_BRAND_RELATION_FAIL);
            }
        }
        return OperationResult.ok();
    }

}