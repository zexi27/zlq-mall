package com.zlq.mall.product.service.impl;

import com.zlq.mall.common.utils.OperationResult;
import com.zlq.mall.product.constant.AttrConstant;
import com.zlq.mall.product.vo.AttrGroupRelationVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlq.mall.common.utils.PageUtils;
import com.zlq.mall.common.utils.Query;

import com.zlq.mall.product.dao.AttrAttrgroupRelationDao;
import com.zlq.mall.product.entity.AttrAttrgroupRelationEntity;
import com.zlq.mall.product.service.AttrAttrgroupRelationService;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OperationResult saveRelation(List<AttrGroupRelationVO> vos) {
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntityList = vos.stream().map(vo -> {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(vo, attrAttrgroupRelationEntity);
            return attrAttrgroupRelationEntity;
        }).collect(Collectors.toList());
        if (this.saveBatch(attrAttrgroupRelationEntityList)){
            return OperationResult.error(AttrConstant.SAVE_RELATION_FALL);
        }
        return OperationResult.ok();
    }


}