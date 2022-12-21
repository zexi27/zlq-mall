package com.zlq.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlq.mall.common.utils.OperationResult;
import com.zlq.mall.common.utils.PageUtils;
import com.zlq.mall.product.entity.AttrEntity;
import com.zlq.mall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author zhangliqun
 * @email yuzexi0727@gmail.com
 * @date 2022-11-19 15:29:27
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);



    OperationResult listAttrByGroupId(Long attrGroupId);

    OperationResult listNoAttrGroupRelation(Long attrGroupId, Map<String, Object> params);

    OperationResult insertAttr(AttrVo vo);

    OperationResult queryBaseOrSalePage(Map<String, Object> params, Long catelogId, int typeCode);

    OperationResult getAttrInfo(Long attrId);

    OperationResult updateAttr(AttrVo vo);

    /**
     * 获取属性组中的关联属性列表
     * @param attrGroupId
     * @return
     */
    List<AttrEntity> getRelationAttr(Long attrGroupId);
}

