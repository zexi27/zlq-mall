package com.zlq.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlq.mall.common.utils.PageUtils;
import com.zlq.mall.ware.entity.WareSkuEntity;

import java.util.Map;

/**
 * εεεΊε­
 *
 * @author zhangliqun
 * @email yuzexi0727@gmail.com
 * @date 2022-11-19 15:46:04
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

