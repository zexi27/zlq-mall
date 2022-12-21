package com.zlq.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlq.mall.common.utils.OperationResult;
import com.zlq.mall.common.utils.PageUtils;
import com.zlq.mall.product.entity.AttrGroupEntity;
import com.zlq.mall.product.vo.AttrGroupRelationVO;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author zhangliqun
 * @email yuzexi0727@gmail.com
 * @date 2022-11-19 15:29:27
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params,Long catelogId);

    OperationResult deleteAttrGroupRelation(AttrGroupRelationVO[] vos);


    OperationResult getAttrGroupWithAttr(Long catelogId);

}

