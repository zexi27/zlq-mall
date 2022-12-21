package com.zlq.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zlq.mall.common.constant.ProductConstant;
import com.zlq.mall.common.utils.OperationResult;
import com.zlq.mall.product.constant.AttrConstant;
import com.zlq.mall.product.dao.AttrAttrgroupRelationDao;
import com.zlq.mall.product.dao.AttrGroupDao;
import com.zlq.mall.product.dao.CategoryDao;
import com.zlq.mall.product.dto.AttrResultDTO;
import com.zlq.mall.product.entity.AttrAttrgroupRelationEntity;
import com.zlq.mall.product.entity.AttrGroupEntity;
import com.zlq.mall.product.entity.CategoryEntity;
import com.zlq.mall.product.service.CategoryService;
import com.zlq.mall.product.vo.AttrVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlq.mall.common.utils.PageUtils;
import com.zlq.mall.common.utils.Query;

import com.zlq.mall.product.dao.AttrDao;
import com.zlq.mall.product.entity.AttrEntity;
import com.zlq.mall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    private AttrDao attrDao;
    @Autowired
    private AttrGroupDao attrGroupDao;
    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OperationResult listAttrByGroupId(Long attrGroupId) {
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntityList = attrAttrgroupRelationDao.selectList(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                .eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupId));
        List<AttrEntity> attrEntityList = attrAttrgroupRelationEntityList.stream()
                .map(attrAttrgroupRelationEntity -> attrDao.selectById(attrAttrgroupRelationEntity.getAttrId()))
                .collect(Collectors.toList());
        return OperationResult.ok().put("data", attrEntityList);
    }


    @Override
    public OperationResult listNoAttrGroupRelation(Long attrGroupId, Map<String, Object> params) {
        String key = (String) params.get("key");
        // 获取属性分组
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
        if (Objects.isNull(attrGroupEntity)) {
            return OperationResult.error(AttrConstant.ATTR_GROUP_NOT_EXIST);
        }

        // attr_Group中的关联categoryId
        Long relationCategoryId = attrGroupEntity.getCatelogId();
        // 获取该属性分组下的对应categoryId的所有分组
        List<AttrGroupEntity> attrGroupEntityList = attrGroupDao.selectList(new LambdaQueryWrapper<AttrGroupEntity>()
                .eq(AttrGroupEntity::getCatelogId, relationCategoryId));
        List<Long> attrGroupIdList = attrGroupEntityList.stream()
                .map(entity -> entity.getAttrGroupId())
                .collect(Collectors.toList());
        // 获取属性关联组信息
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntityList = attrAttrgroupRelationDao
                .selectList(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                        .in(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupIdList));
        List<AttrEntity> attrEntityList = new ArrayList<>();
        LambdaQueryWrapper<AttrEntity> wrapper = null;

        // 如果attrAttrgroupRelationEntityList为空直接返回

        if (!attrAttrgroupRelationEntityList.isEmpty()) {
            wrapper = new LambdaQueryWrapper<>();
            List<Long> attrIdList = attrAttrgroupRelationEntityList.stream()
                    .map(attrAttrgroupRelationEntity -> attrAttrgroupRelationEntity.getAttrId())
                    .collect(Collectors.toList());
            wrapper.eq(AttrEntity::getCatelogId, relationCategoryId)
                    .eq(AttrEntity::getAttrType, ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());

            if (!attrIdList.isEmpty()) {
                wrapper.notIn(AttrEntity::getAttrId, attrIdList);

            }
            if (StringUtils.isNotBlank(key)) {
                wrapper.and(w -> {
                    w.eq(AttrEntity::getAttrId, key).or().like(AttrEntity::getAttrName, key);
                });
            }
        }


        // 查询对应的相关信息
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        return OperationResult.ok().put("data", pageUtils);
    }

    @Override
    public OperationResult insertAttr(AttrVo vo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(vo, attrEntity);
        if (!this.save(attrEntity)) {
            return OperationResult.error(AttrConstant.SAVE_ATTR_FALL);
        }
        if (vo.getAttrGroupId() != null) {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationEntity.setAttrGroupId(vo.getAttrGroupId());
            if (attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity) != 1) {
                return OperationResult.error(AttrConstant.SAVE_RELATION_FALL);
            }
        }
        return OperationResult.ok();
    }

    @Override
    public OperationResult queryBaseOrSalePage(Map<String, Object> params, Long catelogId, int typeCode) {
        String key = (String) params.get("key");
        LambdaQueryWrapper<AttrEntity> wrapper = new LambdaQueryWrapper<>();
        if (typeCode == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            wrapper.eq(AttrEntity::getAttrType, ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        } else {
            wrapper.eq(AttrEntity::getAttrType, ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());
        }

        if (catelogId != 0) {
            wrapper.eq(AttrEntity::getCatelogId, catelogId);
        }
        if (StringUtils.isNotBlank(key)) {
            wrapper.and(w -> {
                w.eq(AttrEntity::getCatelogId, key).or().like(AttrEntity::getAttrName, key);
            });
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);

        List<AttrEntity> attrEntityList = page.getRecords();
        List<AttrResultDTO> attrResultDTOList = attrEntityList.stream().map(attrEntity -> {
            AttrResultDTO attrResultDTO = new AttrResultDTO();
            BeanUtils.copyProperties(attrEntity, attrResultDTO);
            if (attrEntity.getCatelogId() != null) {
                CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
                if (!Objects.isNull(categoryEntity)) {
                    attrResultDTO.setCatelogName(categoryEntity.getName());
                }
            }
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq(AttrAttrgroupRelationEntity::getAttrId, attrEntity.getAttrId()));
            if (!Objects.isNull(attrAttrgroupRelationEntity)) {
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrAttrgroupRelationEntity.getAttrGroupId());
                attrResultDTO.setAttrGroupId(attrGroupEntity.getAttrGroupId());
                attrResultDTO.setGroupName(attrGroupEntity.getAttrGroupName());
            }
            return attrResultDTO;
        }).collect(Collectors.toList());
        pageUtils.setList(attrResultDTOList);
        return OperationResult.ok().put("page", pageUtils);
    }

    @Override
    public OperationResult getAttrInfo(Long attrId) {
        AttrEntity attrEntity = attrDao.selectById(attrId);
        if (Objects.isNull(attrEntity)) {
            return OperationResult.error(AttrConstant.ATTR_NOT_FOUND);
        }
        AttrResultDTO attrResultDTO = new AttrResultDTO();
        BeanUtils.copyProperties(attrEntity, attrResultDTO);
        if (attrEntity.getCatelogId() != null) {
            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (!Objects.isNull(categoryEntity)) {
                attrResultDTO.setCatelogName(categoryEntity.getName());
            }
        }
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                .eq(AttrAttrgroupRelationEntity::getAttrId, attrEntity.getAttrId()));
        if (!Objects.isNull(attrAttrgroupRelationEntity)) {
            AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrAttrgroupRelationEntity.getAttrGroupId());
            attrResultDTO.setAttrGroupId(attrGroupEntity.getAttrGroupId());
            attrResultDTO.setGroupName(attrGroupEntity.getAttrGroupName());
        }
        Long[] catelogPath = categoryService.getAttrCatelogPath(attrEntity.getCatelogId());
        attrResultDTO.setCatelogPath(catelogPath);
        return OperationResult.ok().put("attr", attrResultDTO);
    }

    @Override
    @Transactional
    public OperationResult updateAttr(AttrVo vo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(vo, attrEntity);
        if (!this.updateById(attrEntity)) {
            return OperationResult.error(AttrConstant.UPDATE_ATTR_FAIL);
        }
        // 修改关联属性
        if (vo.getAttrGroupId() != null) {
            // 查询关联数据是否存在
            Integer attrRelationCount = attrAttrgroupRelationDao.selectCount(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq(AttrAttrgroupRelationEntity::getAttrId, attrEntity.getAttrId()));
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrId(attrEntity.getAttrId());
            relationEntity.setAttrGroupId(vo.getAttrGroupId());
            // 如果存在关联数据直接修改
            if (attrRelationCount > 0) {
                int update = attrAttrgroupRelationDao.update(relationEntity, new LambdaUpdateWrapper<AttrAttrgroupRelationEntity>()
                        .set(AttrAttrgroupRelationEntity::getAttrGroupId, vo.getAttrGroupId())
                        .eq(AttrAttrgroupRelationEntity::getAttrId, attrEntity.getAttrId()));
                if (update != 1) {
                    return OperationResult.error(AttrConstant.UPDATE_ATTR_RELATION_FALL);
                }
            } else { // 不存在关联数据直接新增
                int insert = attrAttrgroupRelationDao.insert(relationEntity);
                if (insert != 1) {
                    return OperationResult.error(AttrConstant.INSERT_ATTR_RELATION_FALL);
                }
            }
        } else {
            // 如果未选择分组，将原来存在的分组关联信息删除
            attrAttrgroupRelationDao.delete(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq(AttrAttrgroupRelationEntity::getAttrId, attrEntity.getAttrId()));

        }
        return OperationResult.ok();
    }

    @Override
    public List<AttrEntity> getRelationAttr(Long attrGroupId) {
        return attrDao.getRelationAttr(attrGroupId);
    }

}