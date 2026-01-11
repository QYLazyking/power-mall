package cn.lazyking.power.controller;

import cn.lazyking.power.constants.BusinessStatus;
import cn.lazyking.power.domain.SysRole;
import cn.lazyking.power.model.Result;
import cn.lazyking.power.service.impl.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("多条件分页查询系统角色列表")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('sys:role:page')")
    public Result<Page<SysRole>> loadSysRolePage(
            @RequestParam Long current,
            @RequestParam Long size,
            @RequestParam(required = false) String roleName
    ) {
        Page<SysRole> page = new Page<>(current, size);
        page = sysRoleService.page(
                page,
                new LambdaQueryWrapper<SysRole>()
                        .like(StringUtils.hasText(roleName), SysRole::getRoleName, roleName)
                        .orderByDesc(SysRole::getCreateTime)
        );
        return Result.ok(page);
    }

    @ApiOperation("")
    @PostMapping
    @PreAuthorize("hasAuthority('sys:role:save')")
    public Result<Object> saveSysRole(@RequestBody SysRole sysRole) {
        boolean save = sysRoleService.addSysRole(sysRole);
        return save ? Result.ok(null) : Result.fail(BusinessStatus.OPERATION_FAIL);
    }

    @ApiOperation("查询角色详情(包含权限ID集合)")
    @GetMapping("info/{roleId}")
    @PreAuthorize("hasAuthority('sys:role:info')")
    public Result<SysRole> loadSysRoleDetailInfo(@PathVariable Long roleId) {
        SysRole sysRole = sysRoleService.getSysRoleDetailById(roleId);
        return Result.ok(sysRole);
    }

    @ApiOperation("修改角色信息")
    @PutMapping
    @PreAuthorize("hasAuthority('sys:role:update')")
    public Result<String> modifySysRole(@RequestBody SysRole sysRole) {
        boolean flag = sysRoleService.modifySysRole(sysRole);
        return flag ? Result.ok(null) : Result.fail(BusinessStatus.OPERATION_FAIL);
    }

    @ApiOperation("批量删除角色")
    @DeleteMapping
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public Result<String> removeSysRoles(@RequestBody List<Long> roleIds) {
        boolean flag = sysRoleService.removeSysRolesByIds(roleIds);
        return flag ? Result.ok(null) : Result.fail(BusinessStatus.OPERATION_FAIL);
    }







}
