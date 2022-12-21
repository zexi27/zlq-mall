package com.zlq.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlq.mall.common.utils.PageUtils;
import com.zlq.mall.product.entity.SkuSaleAttrValueEntity;

import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author zhangliqun
 * @email yuzexi0727@gmail.com
 * @date 2022-12-21 23:15:42
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

