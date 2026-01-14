package cn.lazyking.power.service;

import cn.lazyking.power.domain.Category;
import cn.lazyking.power.ex.exception.BusinessException;
import cn.lazyking.power.mapper.CategoryMapper;
import cn.lazyking.power.service.impl.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@CacheConfig(cacheNames = "cn.lazyking.power.service.CategoryServiceImpl")
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService{

    private final CategoryMapper categoryMapper;

    @Override
    @Cacheable(key = "'prod:category'")
    public List<Category> getAllCategoryList() {
        return categoryMapper.selectList(
                new LambdaQueryWrapper<Category>()
                    .orderByDesc(Category::getSeq)
        );
    }

    @Override
    @Cacheable(key = "'prod:rootCategory'")
    public List<Category> getRootCategoryList() {
        return categoryMapper.selectList(
                new LambdaQueryWrapper<Category>()
                        .eq(Category::getParentId, 0)
                        .eq(Category::getStatus, 1)
                        .orderByDesc(Category::getSeq)
        );
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "'prod:category'"),
            @CacheEvict(key = "'prod:rootCategory'")
    })
    public boolean saveCategory(Category category) {
        category.setCreateTime(new Date());
        category.setUpdateTime(new Date());
        return save(category);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "'prod:category'"),
            @CacheEvict(key = "'prod:rootCategory'")
    })
    public boolean modifyCategory(Category category) {
        // 获取商品 id
        Long categoryId = category.getCategoryId();
        // 查询商品类目信息
        Category oldCategory = categoryMapper.selectById(categoryId);
        // 获取类目父 id
        Long oldParentId = oldCategory.getParentId();
        // 查询当前类目的子类目数量
        Long count = categoryMapper.selectCount(
                new LambdaQueryWrapper<Category>()
                        .eq(Category::getParentId, categoryId)
        );

        if (0 == oldParentId && 0 != category.getParentId()) {
            // 判断类目是否包含子类目，如果子类目数量为0则可以修改
            if(0 != count) {
                throw new BusinessException("类目录包含子目录，修改失败");
            }
        }

        if(0 != oldParentId && category.getParentId() != null) {
            category.setParentId(0L);
        }

        category.setUpdateTime(new Date());
        return this.updateById(category);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "'prod:category'"),
            @CacheEvict(key = "'prod:rootCategory'")
    })
    public boolean removeCategoryById(Long categoryId) {
        // 查询子类目的数量
        Long count = categoryMapper.selectCount(
                new LambdaQueryWrapper<Category>()
                        .eq(Category::getParentId, categoryId)
        );
        if(count > 0) {
            throw new BusinessException("类目下有子类目，请先删除子类目");
        }
        return removeById(categoryId);
    }

}
