package com.zlq.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zlq.mall.common.utils.*;
import com.zlq.mall.product.constant.BrandRelationConstant;
import com.zlq.mall.product.constant.CategoryConstant;
import com.zlq.mall.common.constant.PageConstant;
import com.zlq.mall.product.constant.UpdateCategoryBrandEnum;
import com.zlq.mall.product.dao.CategoryBrandRelationDao;
import com.zlq.mall.product.dto.CategoryTreeDTO;
import com.zlq.mall.product.entity.CategoryBrandRelationEntity;
import com.zlq.mall.product.service.CategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zlq.mall.product.dao.CategoryDao;
import com.zlq.mall.product.entity.CategoryEntity;
import com.zlq.mall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CategoryBrandRelationDao categoryBrandRelationDao;

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Object sizeObj = params.get("size");
        Object currentPageObj = params.get("currentPage");
        int size = Objects.isNull(sizeObj) ? PageConstant.DEFAULT_PAGE_SIZE : Integer.valueOf((String) sizeObj);
        int currentPage = Objects.isNull(currentPageObj) ? PageConstant.DEFAULT_PAGE_NUM : Integer.valueOf((String) currentPageObj);
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );
        return new PageUtils(page.getRecords(), page.getRecords().size(), size, currentPage);
    }

    @Override
    public List<CategoryTreeDTO> listTree() {
        List<CategoryTreeDTO> rootList = new ArrayList<>();
        List<CategoryEntity> categoryEntityList = baseMapper.selectList(null);
        if (categoryEntityList.isEmpty()) {
            return rootList;
        }
        rootList = categoryEntityList.stream().filter(categoryEntity ->
                        categoryEntity.getParentCid() != null && categoryEntity.getParentCid() == 0 && categoryEntity.getShowStatus() == 1)
                .map(categoryEntity -> {
                    CategoryTreeDTO categoryTreeDTO = BeanCopyUtils.copyObject(categoryEntity, CategoryTreeDTO.class);
                    categoryTreeDTO.setChildren(getChildren(categoryTreeDTO.getCatId(), categoryEntityList));
                    return categoryTreeDTO;
                }).sorted((o1, o2) -> (o1.getSort() == null ? 0 : o1.getSort()) - (o2.getSort() == null ? 0 : o2.getSort()))
                .collect(Collectors.toList());

        return rootList;
    }

    @Override
    public OperationResult deleteCategory(List<Long> ids) {
        // 检查要删除的分类中是否有在使用
        for (Long id : ids) {
            if (Objects.isNull(categoryDao.selectById(id))) {
                return OperationResult.error(CategoryConstant.CATEGORY_NOT_EXIST);
            }
            List<CategoryBrandRelationEntity> categoryBrandRelationList = categoryBrandRelationDao.selectList(new LambdaQueryWrapper<CategoryBrandRelationEntity>()
                    .eq(CategoryBrandRelationEntity::getCatelogId, id));
            if (!categoryBrandRelationList.isEmpty()) {
                return OperationResult.error(CategoryConstant.CATEGORY_IN_USED);
            }
        }
        // 检查要删除的分类中是否有父分类

        return OperationResult.ok().put("data", baseMapper.deleteBatchIds(ids));
    }

    @Override
    @Transactional
    public OperationResult updateCategory(CategoryEntity category) {
        if (categoryDao.updateById(category) != 1) {
            return OperationResult.error("品牌修改失败");
        }
        String brandName = category.getName();
        // 如果修改了品牌名称需要去category_brand表里面修改冗余字段
        if (StringUtils.isNotEmpty(brandName)) {
            Long brandId = category.getCatId();
            OperationResult result = categoryBrandRelationService.updateCategoryBrandRelation(UpdateCategoryBrandEnum.UPDATE_CATEGORY.getCode(), brandId, brandName);
            if (!result.isSuccess()) {
                return OperationResult.error(BrandRelationConstant.UPDATE_BRAND_RELATION_FAIL);
            }
        }
        return OperationResult.ok();
    }

    /**
     * 根据categoryId从下至上获取整个的category路径
     *
     * @param categoryId
     * @param cateLogPathList
     */
    private void addParentCategoryId(Long categoryId, List<Long> cateLogPathList) {
        CategoryEntity categoryEntity = categoryDao.selectById(categoryId);
        cateLogPathList.add(categoryId);
        if (categoryEntity.getParentCid() == 0) return;
        addParentCategoryId(categoryEntity.getParentCid(), cateLogPathList);

    }


    @Override
    public Long[] getAttrCatelogPath(Long catelogId) {
        List<Long> cateLogPathList = new ArrayList<>();
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        if (!Objects.isNull(categoryEntity)) {
            addParentCategoryId(categoryEntity.getCatId(), cateLogPathList);
        }
        Long[] catelogPath = null;
        if (!cateLogPathList.isEmpty()) {
            Collections.reverse(cateLogPathList);
            catelogPath = cateLogPathList.stream().toArray(Long[]::new);
        }
        return catelogPath;
    }


    private List<CategoryTreeDTO> getChildren(Long parentCatId, List<CategoryEntity> categoryEntityList) {
        List<CategoryTreeDTO> childrenList = new ArrayList<>();
        if (parentCatId == null) {
            return childrenList;
        }
        childrenList = categoryEntityList.stream().
                filter(categoryEntity -> categoryEntity.getParentCid().equals(parentCatId))
                .map(categoryEntity -> {
                    CategoryTreeDTO categoryTreeDTO = BeanCopyUtils.copyObject(categoryEntity, CategoryTreeDTO.class);
                    // 递归获取子树
                    categoryTreeDTO.setChildren(getChildren(categoryTreeDTO.getCatId(), categoryEntityList));
                    return categoryTreeDTO;
                }).sorted((o1, o2) -> (o1.getSort() == null ? 0 : o1.getSort()) - (o2.getSort() == null ? 0 : o2.getSort()))
                .collect(Collectors.toList());

        return childrenList;
    }


}