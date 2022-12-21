package com.zlq.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zlq.mall.common.constant.PageConstant;
import com.zlq.mall.common.constant.ProductConstant;
import com.zlq.mall.common.utils.OperationResult;
import com.zlq.mall.product.constant.AttrConstant;
import com.zlq.mall.product.dao.AttrAttrgroupRelationDao;
import com.zlq.mall.product.dao.AttrDao;
import com.zlq.mall.product.dao.CategoryDao;
import com.zlq.mall.product.dto.AttrGroupWithAttrsDTO;
import com.zlq.mall.product.entity.AttrAttrgroupRelationEntity;
import com.zlq.mall.product.entity.AttrEntity;
import com.zlq.mall.product.entity.CategoryEntity;
import com.zlq.mall.product.service.AttrService;
import com.zlq.mall.product.service.CategoryService;
import com.zlq.mall.product.vo.AttrGroupRelationVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlq.mall.common.utils.PageUtils;
import com.zlq.mall.common.utils.Query;

import com.zlq.mall.product.dao.AttrGroupDao;
import com.zlq.mall.product.entity.AttrGroupEntity;
import com.zlq.mall.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    @Autowired
    private AttrGroupDao attrGroupDao;
    @Autowired
    private AttrDao attrDao;
    @Autowired
    private AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        String attrGroupName = (String) params.get("key");
        LambdaQueryWrapper<AttrGroupEntity> wrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(attrGroupName)) {
            wrapper.like(AttrGroupEntity::getAttrGroupName, attrGroupName);

        }
        if (catelogId != 0) {
            wrapper.eq(AttrGroupEntity::getCatelogId, catelogId);
        }
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }


    @Override
    public OperationResult deleteAttrGroupRelation(AttrGroupRelationVO[] vos) {
        for (AttrGroupRelationVO vo : vos) {
            if (attrAttrgroupRelationDao.deleteById(vo.getAttrId()) != 1) {
                return OperationResult.error(AttrConstant.DELETE_ATTR_GROUP_RELATION_FALL);
            }
        }
        return OperationResult.ok();
    }

    @Override
    public OperationResult getAttrGroupWithAttr(Long catelogId) {
        List<AttrGroupEntity> attrGroupEntityList = attrGroupDao.selectList(new LambdaQueryWrapper<AttrGroupEntity>()
                .eq(AttrGroupEntity::getCatelogId, catelogId));
        List<AttrGroupWithAttrsDTO> list = new ArrayList<>();
        if (!attrGroupEntityList.isEmpty()) {
            list = attrGroupEntityList.stream().map(attrGroupEntity -> {
                AttrGroupWithAttrsDTO attrGroupWithAttrsDTO = new AttrGroupWithAttrsDTO();
                BeanUtils.copyProperties(attrGroupEntity, attrGroupWithAttrsDTO);
                // 获取属性组中的规格参数
                List<AttrEntity> attrEntityList = attrService.getRelationAttr(attrGroupEntity.getAttrGroupId());
                attrGroupWithAttrsDTO.setAttrs(attrEntityList);
                return attrGroupWithAttrsDTO;
            }).collect(Collectors.toList());
        }
        return OperationResult.ok().put("data", list);
    }


//    public static void main(String[] args) {
//        List<Long> cateLogPathList = new ArrayList<>();
//        cateLogPathList.add(387L);
//        cateLogPathList.add(47L);
//        cateLogPathList.add(4L);
//        Long[] catelogPath = null;
//        Collections.reverse(cateLogPathList);
//        catelogPath = cateLogPathList.stream().toArray(Long[]::new);
//
//        System.out.println(Arrays.toString(catelogPath));
//    }

}