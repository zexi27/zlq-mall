package com.zlq.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlq.mall.common.utils.PageUtils;
import com.zlq.mall.product.entity.SpuCommentEntity;

import java.util.Map;

/**
 * 商品评价
 *
 * @author zhangliqun
 * @email yuzexi0727@gmail.com
 * @date 2022-12-21 23:15:42
 */
public interface SpuCommentService extends IService<SpuCommentEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

