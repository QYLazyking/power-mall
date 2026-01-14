package cn.lazyking.power.service.impl;

import cn.lazyking.power.domain.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CategoryService extends IService<Category>{


    List<Category> getAllCategoryList();

    List<Category> getRootCategoryList();

    boolean saveCategory(Category category);

    boolean modifyCategory(Category category);

    boolean removeCategoryById(Long categoryId);
}
