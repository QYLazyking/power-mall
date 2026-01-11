package cn.lazyking.power.controller;

import cn.lazyking.power.domain.SysRole;
import cn.lazyking.power.model.Result;
import cn.lazyking.power.service.impl.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统角色控制层
 */
@RestController
@RequestMapping("sys/role")
@Api(tags = "系统角色接口管理")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService sysRoleService;

    @ApiOperation("查询系统角色列表")
    @GetMapping("list")
    public Result<List<SysRole>> loadSysRoleList() {
        List<SysRole> roles = sysRoleService.getSysRoleList();

        return Result.ok(roles);
    }



}
