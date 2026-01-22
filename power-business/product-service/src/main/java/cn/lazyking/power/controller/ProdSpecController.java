package cn.lazyking.power.controller;

import cn.lazyking.power.constants.BusinessStatus;
import cn.lazyking.power.domain.ProdProp;
import cn.lazyking.power.domain.ProdPropValue;
import cn.lazyking.power.model.Result;
import cn.lazyking.power.service.impl.ProdPropService;
import cn.lazyking.power.service.impl.ProdPropValueService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "商品规格接口管理")
@RequestMapping("prod/spec")
@RequiredArgsConstructor
public class ProdSpecController {

    private final ProdPropService prodPropService;
    private final ProdPropValueService prodPropValueService;

    @ApiOperation("多条件分页查询商品规格")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('prod:spec:page')")
    public Result<Page<ProdProp>> loadProdSpecPage(
            @RequestParam Long current,
            @RequestParam Long size,
            @RequestParam(required = false) String propName
    ) {
        Page<ProdProp> page = prodPropService.getProdSpecPage(current, size, propName);
        return Result.ok(page);
    }

    @ApiOperation("新增商品规格")
    @PostMapping
    @PreAuthorize("hasAuthority('prod:spec:save')")
    public Result<String> saveProdSpec(@RequestBody ProdProp prodProp) {
        Boolean flag = prodPropService.addProdProp(prodProp);
        return flag ? Result.ok(null) : Result.fail(BusinessStatus.OPERATION_FAIL);
    }

    @ApiOperation("修改商品规格")
    @PutMapping
    @PreAuthorize("hasAuthority('prod:spec:update')")
    public Result<Object> modifyProdSpec(@RequestBody ProdProp prodProp) {
        boolean flag = prodPropService.modifyProdProp(prodProp);
        return flag ? Result.ok(null) : Result.fail(BusinessStatus.OPERATION_FAIL);
    }

    @ApiOperation("删除商品规格")
    @DeleteMapping("{propId}")
    @PreAuthorize("hasAuthority('prod:spec:delete')")
    public Result<String> removeProdSpec(@PathVariable Long propId) {
        boolean flag = prodPropService.removeProdProp(propId);
        return flag ? Result.ok(null) : Result.fail(BusinessStatus.OPERATION_FAIL);
    }

    @ApiOperation("查询商品属性列表")
    @GetMapping("list")
    @PreAuthorize("hasAuthority('prod:spec:page')")
    public Result<List<ProdProp>> loadProdPropList() {
        List<ProdProp> list = prodPropService.list(new LambdaQueryWrapper<ProdProp>()
                .orderByDesc(ProdProp::getPropId)
        );
        return Result.ok(list);
    }

    @ApiOperation("根据商品属性标识查询属性值集合")
    @GetMapping("listSpecValue/{propId}")
    @PreAuthorize("hasAuthority('prod:spec:page')")
    public Result<List<ProdPropValue>> loadProdPropValues(@PathVariable Long propId) {
        List<ProdPropValue> list = prodPropValueService.list(new LambdaQueryWrapper<ProdPropValue>()
                .eq(ProdPropValue::getPropId, propId)
        );
        return Result.ok(list);
    }


}
