package cn.lazyking.power.controller;

import cn.lazyking.power.constants.BusinessStatus;
import cn.lazyking.power.domain.Category;
import cn.lazyking.power.model.Result;
import cn.lazyking.power.service.impl.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "商品类目接口管理")
@RestController
@RequestMapping("/prod/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @ApiOperation("查询系统所有商品类目")
    @GetMapping("table")
    public Result<List<Category>> loadAllCategoryList() {
        List<Category> list = categoryService.getAllCategoryList();
        return Result.ok(list);
    }

    @ApiOperation("查询商品一级类目")
    @GetMapping("listCategory")
    @PreAuthorize("hasAuthority('prod:category:page')")
    public Result<List<Category>> loadRootCategoryList() {
        List<Category> list = categoryService.getRootCategoryList();
        return Result.ok(list);
    }

    @ApiOperation("新增商品类目")
    @PostMapping
    @PreAuthorize("hasAuthority('prod:category:save')")
    public Result<String> saveCategory(@RequestBody Category category) {
        boolean result = categoryService.saveCategory(category);
        return result ? Result.ok("新增成功") : Result.fail(BusinessStatus.OPERATION_FAIL);
    }

    @ApiOperation("查询商品类目详情")
    @GetMapping("info/{categoryId}")
    @PreAuthorize("hasAuthority('prod:category:info')")
    public Result<Category> loadCategoryInfo(@PathVariable Long categoryId) {
        Category category = categoryService.getById(categoryId);
        return Result.ok(category);
    }

    @ApiOperation("修改商品类目")
    @PutMapping
    @PreAuthorize("hasAuthority('prod:category:update')")
    public Result<String> modifyCategory(@RequestBody Category category) {
        boolean flag = categoryService.modifyCategory(category);
        return flag ? Result.ok("修改成功") : Result.fail(BusinessStatus.OPERATION_FAIL);
    }

    @ApiOperation("删除商品类目")
    @DeleteMapping("{categoryId}")
    @PreAuthorize("hasAuthority('prod:category:delete')")
    public Result<String> removeCategory(@PathVariable Long categoryId) {
        boolean flag = categoryService.removeCategoryById(categoryId);
        return flag ? Result.ok("删除成功") : Result.fail(BusinessStatus.OPERATION_FAIL);
    }


}
