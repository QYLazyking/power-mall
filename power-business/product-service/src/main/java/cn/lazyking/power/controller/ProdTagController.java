package cn.lazyking.power.controller;

import cn.lazyking.power.constants.BusinessStatus;
import cn.lazyking.power.domain.ProdTag;
import cn.lazyking.power.model.Result;
import cn.lazyking.power.service.impl.ProdTagService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api(tags = "商品分组标签接口管理")
@RestController
@RequestMapping("/prod/prodTag")
@RequiredArgsConstructor
public class ProdTagController {

    private final ProdTagService prodTagService;

    @ApiOperation("多条件分页查询商品分组标签")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('prod:prodTag:page')")
    public Result<Page<ProdTag>> loadProdTagPage(
            @RequestParam Long current,
            @RequestParam Long size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer status
            ) {

        Page<ProdTag> page = new Page<>(current, size);

        page = prodTagService.page(
                page,
                new LambdaQueryWrapper<ProdTag>()
                        .eq(status != null, ProdTag::getStatus, status)
                        .like(StringUtils.hasText(title), ProdTag::getTitle, title)
                        .orderByDesc(ProdTag::getSeq)
        );

        return Result.ok(page);
    }

    @ApiOperation("新增商品分组标签")
    @PostMapping
    @PreAuthorize("hasAuthority('prod:prodTag:save')")
    public Result<String> saveProdTag(@RequestBody ProdTag prodTag) {
        boolean flag = prodTagService.saveProdTag(prodTag);
        return flag ? Result.ok(null) : Result.fail(BusinessStatus.OPERATION_FAIL);
    }

    @ApiOperation("查询商品分组标签详情")
    @GetMapping("info/{prodTagId}")
    @PreAuthorize("hasAuthority('prod:prodTag:info')")
    public Result<ProdTag> loadProdTagDetailInfo(@PathVariable Long prodTagId) {
        ProdTag prodTag = prodTagService.getProdTagDetailById(prodTagId);
        return Result.ok(prodTag);
    }

    @ApiOperation("修改商品分组标签信息")
    @PutMapping
    @PreAuthorize("hasAuthority('prod:prodTag:update')")
    public Result<String> modifyProdTag(@RequestBody ProdTag prodTag) {
        prodTag.setUpdateTime(new Date());
        boolean flag = prodTagService.updateById(prodTag);
        return flag ? Result.ok(null) : Result.fail(BusinessStatus.OPERATION_FAIL);
    }

    @ApiOperation("删除商品分组标签")
    @DeleteMapping("{tagId}")
    @PreAuthorize("hasAuthority('prod:prodTag:delete')")
    public Result<String> removeProdTag(@PathVariable Long tagId) {
        boolean flag = prodTagService.removeById(tagId);
        return flag ? Result.ok(null) : Result.fail(BusinessStatus.OPERATION_FAIL);
    }

}
