package com.zlq.mall.order.dao;

import com.zlq.mall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author zhangliqun
 * @email yuzexi0727@gmail.com
 * @date 2022-11-19 15:27:39
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
