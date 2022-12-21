package com.zlq.mall.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.zlq.mall.common.constant.ProductConstant;
import com.zlq.mall.common.utils.OperationResult;
import com.zlq.mall.product.vo.AttrVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zlq.mall.product.service.AttrService;
import com.zlq.mall.common.utils.R;



/**
 * 商品属性
 *
 * @author zhangliqun
 * @email yuzexi0727@gmail.com
 * @date 2022-11-19 15:29:27
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @RequestMapping("/base/list/{catelogId}")
    public R queryBasePage(@RequestParam Map<String, Object> params, @PathVariable(value = "catelogId") Long catelogId){
        OperationResult result = attrService.queryBaseOrSalePage(params,catelogId, ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        if (!result.isSuccess()){
            return R.error(result.getMessage());
        }
        return R.ok().put("page", result.get("page"));
    }

    @RequestMapping("/sale/list/{catelogId}")
    public R querySalePage(@RequestParam Map<String, Object> params, @PathVariable(value = "catelogId") Long catelogId){
        OperationResult result = attrService.queryBaseOrSalePage(params,catelogId,ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());
        if (!result.isSuccess()){
            return R.error(result.getMessage());
        }
        return R.ok().put("page", result.get("page"));
    }



    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R getAttrInfo(@PathVariable("attrId") Long attrId){
        OperationResult result = attrService.getAttrInfo(attrId);
        if (!result.isSuccess()){
            return R.error(result.getMessage());
        }
        return R.ok().put("attr", result.get("attr"));
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R insertAttr(@RequestBody AttrVo vo){
        OperationResult result = attrService.insertAttr(vo);
        if (!result.isSuccess()){
            return R.error(result.getMessage());
        }
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:attr:update")
    public R updateAttr(@RequestBody AttrVo vo){
        OperationResult result = attrService.updateAttr(vo);
        if (!result.isSuccess()){
            return R.error(result.getMessage());
        }
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }


}
