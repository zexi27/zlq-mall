package com.zlq.mall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zlq.mall.common.utils.OperationResult;
import com.zlq.mall.product.constant.BrandRelationConstant;
import org.apache.http.HttpStatus;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zlq.mall.product.entity.CategoryBrandRelationEntity;
import com.zlq.mall.product.service.CategoryBrandRelationService;
import com.zlq.mall.common.utils.PageUtils;
import com.zlq.mall.common.utils.R;


/**
 * 品牌分类关联
 *
 * @author zhangliqun
 * @email yuzexi0727@gmail.com
 * @date 2022-11-19 15:29:27
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }

    @RequestMapping("/catelog/list")
    public R listBrandRelation(Long brandId) {
        List<CategoryBrandRelationEntity> categoryBrandRelationList = categoryBrandRelationService.list(new LambdaQueryWrapper<CategoryBrandRelationEntity>()
                .eq(CategoryBrandRelationEntity::getBrandId, brandId));
        return R.ok().put("data",categoryBrandRelationList);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("product:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id) {
        CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        if (brandId == null){
            return R.error(BrandRelationConstant.CATEGORY_ID_NOT_EXIST);
        }
        Long categoryId = categoryBrandRelation.getCatelogId();
        if (categoryId == null){
            return R.error(BrandRelationConstant.BRAND_ID_NOT_EXIST);
        }
        OperationResult response = categoryBrandRelationService.addCategoryBrandRelationEntity(brandId, categoryId);
        if (!response.isSuccess()){
            return R.error(response.getMessage());
        }

        return R.ok();
    }

    @RequestMapping("/brands/list")
    public R listBrand(@RequestParam(value = "catId",defaultValue = "0") Long catId){
        OperationResult response = categoryBrandRelationService.listBrand(catId);
        if (!response.isSuccess()){
            return R.error(response.getMessage());
        }

        return R.ok().put("data",response.get("data"));
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids) {
        categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
