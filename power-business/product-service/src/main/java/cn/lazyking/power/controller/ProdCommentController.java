package cn.lazyking.power.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.lazyking.power.constants.BusinessStatus;
import cn.lazyking.power.domain.ProdComm;
import cn.lazyking.power.model.Result;
import cn.lazyking.power.service.impl.ProdCommService;
import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "商品评论管理")
@RequestMapping("prod/prodComm")
@RequiredArgsConstructor
public class ProdCommentController {

    private final ProdCommService prodCommService;

    @ApiOperation("多条件分页查询评论列表")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('prod:prodComm:page')")
    public Result<Page<ProdComm>> loadProdCommPage(Page<ProdComm> page, ProdComm prodComm) {
        page = prodCommService.page(page,new LambdaQueryWrapper<ProdComm>()
                .eq(ObjectUtil.isNotNull(prodComm.getStatus()), ProdComm::getStatus,prodComm.getStatus())
                .like(StringUtils.hasText(prodComm.getProdName()),ProdComm::getProdName,prodComm.getProdName())
                .orderByDesc(ProdComm::getCreateTime)
        );
        return Result.ok(page);
    }

    @ApiOperation("根据标识查询评论详情")
    @GetMapping("{commId}")
    @PreAuthorize("hasAuthority('prod:prodComm:info')")
    public Result<ProdComm> loadProdCommInfo(@PathVariable Long commId) {
        ProdComm prodComm = prodCommService.getById(commId);
        return Result.ok(prodComm);
    }

    @ApiOperation("审核或回复商品评论")
    @PutMapping
    @PreAuthorize("hasAuthority('prod:prodComm:update')")
    public Result<String> modifyProdComm(@RequestBody ProdComm prodComm) {
        boolean flag = prodCommService.examineOrReplyProdComm(prodComm);
        return flag ? Result.ok(null) : Result.fail(BusinessStatus.OPERATION_FAIL);
    }




}
