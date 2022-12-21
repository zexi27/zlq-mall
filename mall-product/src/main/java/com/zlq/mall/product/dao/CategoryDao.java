package com.zlq.mall.product.dao;

import com.zlq.mall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author zhangliqun
 * @email yuzexi0727@gmail.com
 * @date 2022-11-19 15:29:27
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
