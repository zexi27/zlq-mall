package com.zlq.mall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.zlq.mall.common.utils.OperationResult;
import com.zlq.mall.product.constant.CategoryConstant;
import com.zlq.mall.product.dto.CategoryTreeDTO;
import org.apache.http.HttpStatus;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zlq.mall.product.entity.CategoryEntity;
import com.zlq.mall.product.service.CategoryService;
import com.zlq.mall.common.utils.PageUtils;
import com.zlq.mall.common.utils.R;


/**
 * 商品三级分类
 *
 * @author zhangliqun
 * @email yuzexi0727@gmail.com
 * @date 2022-11-19 15:29:27
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = categoryService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 列出商品的树状结构
     *
     * @return
     */
    @RequestMapping("/listTree")
    public R listTree() {
        List<CategoryTreeDTO> categoryTreeList = categoryService.listTree();

        return R.ok().put("data", categoryTreeList);
    }


    /**
     * 逻辑删除商品类型
     *
     * @return
     */
    @PostMapping("/delete")
    public R deleteCategory(@RequestBody List<Long> ids) {
        if (Objects.isNull(ids) || ids.size() == 0) {
            return R.error(CategoryConstant.CATEGORY_ID_NOT_EXIST);
        }
        R res = categoryService.deleteCategory(ids);
        if (res.getCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR || (Integer) res.get("data") < ids.size()) {
            return R.error((String) res.get("msg"));
        }
        return R.ok();
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    @RequiresPermissions("product:category:info")
    public R info(@PathVariable("catId") Long catId) {
        CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:category:save")
    public R save(@RequestBody CategoryEntity category) {
        categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category) {
        if (Objects.isNull(category)){
            return R.error();
        }
        OperationResult result = categoryService.updateCategory(category);
        if (!result.isSuccess()){
            return R.error(result.getMessage());
        }
        return R.ok();
    }


    /**
     * 批量修改
     */
    @RequestMapping("/updateBatch")
    //@RequiresPermissions("product:category:update")
    public R updateBatch(@RequestBody CategoryEntity[] category){
        categoryService.updateBatchById(Arrays.asList(category));
        return R.ok();
    }

}
