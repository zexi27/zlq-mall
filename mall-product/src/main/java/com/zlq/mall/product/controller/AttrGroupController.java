package com.zlq.mall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.zlq.mall.common.utils.OperationResult;
import com.zlq.mall.product.service.AttrAttrgroupRelationService;
import com.zlq.mall.product.service.AttrService;
import com.zlq.mall.product.service.CategoryService;
import com.zlq.mall.product.vo.AttrGroupRelationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zlq.mall.product.entity.AttrGroupEntity;
import com.zlq.mall.product.service.AttrGroupService;
import com.zlq.mall.common.utils.PageUtils;
import com.zlq.mall.common.utils.R;


/**
 * 属性分组
 *
 * @author zhangliqun
 * @email yuzexi0727@gmail.com
 * @date 2022-11-19 15:29:27
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId) {
        PageUtils page = attrGroupService.queryPage(params, catelogId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        if (!Objects.isNull(attrGroup)) {
            Long[] catelogPath = categoryService.getAttrCatelogPath(attrGroup.getCatelogId());
            attrGroup.setCatelogPath(catelogPath);
        }
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

    @RequestMapping("/{attrGroupId}/attr/relation")
    public R listAttrGroupRelation(@PathVariable(value = "attrGroupId") Long attrGroupId) {
        OperationResult result = attrService.listAttrByGroupId(attrGroupId);
        if (!result.isSuccess()) {
            return R.error(result.getMessage());
        }
        return R.ok().put("data", result.get("data"));
    }

    @RequestMapping("/attr/relation")
    public R saveRelation(@RequestBody List<AttrGroupRelationVO> vos) {
        if (!vos.isEmpty()){
            OperationResult result = attrAttrgroupRelationService.saveRelation(vos);
            if (!result.isSuccess()){
                return R.error(result.getMessage());
            }
        }
        return R.ok();
    }

    @PostMapping("/attr/relation/delete")
    public R deleteAttrGroupRelation(@RequestBody AttrGroupRelationVO[] vos) {
        OperationResult result = attrGroupService.deleteAttrGroupRelation(vos);
        if (!result.isSuccess()) {
            return R.error(result.getMessage());
        }
        return R.ok().put("data", result.get("data"));
    }

    @GetMapping("/{attrGroupId}/noattr/relation")
    public R listNoAttrGroupRelation(@PathVariable(value = "attrGroupId") Long attrGroupId,
                                     @RequestParam Map<String,Object> param) {
        OperationResult result = attrService.listNoAttrGroupRelation(attrGroupId,param);
        if (!result.isSuccess()){
            return R.error(result.getMessage());
        }
        PageUtils pageUtils = (PageUtils) result.get("data");
        return R.ok().put("page", pageUtils);
    }

    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttr(@PathVariable(value = "catelogId")Long catelogId){
        OperationResult result = attrGroupService.getAttrGroupWithAttr(catelogId);
        if (!result.isSuccess()){
            return R.error(result.getMessage());
        }
        return R.ok().put("data", result.get("data"));
    }

}
