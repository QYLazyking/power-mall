package cn.lazyking.power.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.lazyking.power.constants.BusinessStatus;
import cn.lazyking.power.domain.Prod;
import cn.lazyking.power.model.Result;
import cn.lazyking.power.service.impl.ProdService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "商品接口管理")
@RestController
@RequestMapping("/prod/prod")
@RequiredArgsConstructor
public class ProdController {

    private final ProdService prodService;

    @ApiOperation("多条件分页查询商品列表")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('prod:prod:page')")
    public Result<Page<Prod>> loadProdPage(Page<Prod> page, Prod prod) {
        page = prodService.page(page,new LambdaQueryWrapper<Prod>()
                .eq(ObjectUtil.isNotNull(prod.getStatus()),Prod::getStatus,prod.getStatus())
                .like(StringUtils.hasText(prod.getProdName()),Prod::getProdName,prod.getProdName())
                .orderByDesc(Prod::getCreateTime)
        );
        return Result.ok(page);
    }

    @ApiOperation("新增商品")
    @PostMapping
    @PreAuthorize("hasAuthority('prod:prod:save')")
    public Result<Object> saveProd(@RequestBody Prod prod) {
        boolean flag = prodService.addProd(prod);
        return flag ? Result.ok(null) : Result.fail(BusinessStatus.OPERATION_FAIL);
    }

    @ApiOperation("根据标识查询商品详情")
    @GetMapping("info/{prodId}")
    @PreAuthorize("hasAuthority('prod:prod:info')")
    public Result<Prod> loadProdDetail(@PathVariable Long prodId) {
        Prod prodDetailVo = prodService.getProdDetailById(prodId);
        return Result.ok(prodDetailVo);
    }

    @ApiOperation("修改商品信息")
    @PutMapping
    @PreAuthorize("hasAuthority('prod:prod:update')")
    public Result<String> modifyProd(@RequestBody Prod prod) {
        boolean flag = prodService.modifyProd(prod);
        return flag ? Result.ok(null) : Result.fail(BusinessStatus.OPERATION_FAIL);
    }

    @ApiOperation("批量删除商品")
    @DeleteMapping("{prodIds}")
    @PreAuthorize("hasAuthority('prod:prod:delete')")
    public Result<String> removeProds(@PathVariable List<Long> prodIds) {
        boolean flag = prodService.removeProdsByIds(prodIds);
        return flag ? Result.ok(null) : Result.fail(BusinessStatus.OPERATION_FAIL);
    }


}