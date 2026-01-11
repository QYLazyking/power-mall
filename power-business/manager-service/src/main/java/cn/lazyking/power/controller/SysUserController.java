package cn.lazyking.power.controller;

import cn.lazyking.power.constants.BusinessStatus;
import cn.lazyking.power.domain.SysUser;
import cn.lazyking.power.model.Result;
import cn.lazyking.power.service.impl.SysUserService;
import cn.lazyking.power.utils.AuthUtil;
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
 * 系统管理员控制层
 */
@RestController
@RequestMapping("sys/user")
@Api(tags = "系统管理员接口管理")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService sysUserService;

    @ApiOperation("查询用户信息")
    @GetMapping("info")
    public Result<SysUser> loadSysUserInfo() {
        // 获取当前登录用户 id
        long currentUserId = AuthUtil.getCurrentUserId();
        // 根据用户 id 查询用户信息
        SysUser sysUser = sysUserService.getById(currentUserId);
        // 移除密码
        sysUser.setPassword(null);
        return Result.ok(sysUser);
    }

    @ApiOperation("多条件分页查询系统管理员列表")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('sys:user:page')")
    public Result<Page<SysUser>> loadSysUserPage(
            @RequestParam Long current,
            @RequestParam Long size,
            @RequestParam(required = false) String username
    ) {
        // 多条件分页查询系统管理员
        // 构建 page 对象
        Page<SysUser> page = new Page<>(current, size);
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        // 查询条件，当username 非空时，添加查询条件
        queryWrapper.like(
                StringUtils.hasText(username),
                SysUser::getUsername,
                username
        ).orderByDesc(SysUser::getCreateTime);
        page = sysUserService.page(page, queryWrapper);
        return Result.ok(page);
    }

    @ApiOperation("新增管理员")
    @PostMapping
    @PreAuthorize("hasAuthority('sys:user:save')")
    public Result<Object> saveSysUser(@RequestBody SysUser sysUser) {
        Integer count = sysUserService.addSysUser(sysUser);
        return count > 0 ? Result.ok(null) : Result.fail(BusinessStatus.OPERATION_FAIL);
    }

    @ApiOperation("查询管理员详情")
    @GetMapping("info/{userId}")
    @PreAuthorize("hasAuthority('sys:user:info')")
    public Result<SysUser> loadSysUserDetail(@PathVariable Long userId) {
        SysUser sysUser = sysUserService.getSysUserByUserId(userId);
        return Result.ok(sysUser);
    }

    @ApiOperation("修改管理员信息")
    @PutMapping
    @PreAuthorize("hasAuthority('sys:user:update')")
    public Result<Object> modifySysUser(@RequestBody SysUser sysUser) {
        int count = sysUserService.modifySysUser(sysUser);
        return count > 0 ? Result.ok(null) : Result.fail(BusinessStatus.OPERATION_FAIL);
    }

    @ApiOperation("删除管理员")
    @DeleteMapping("{userIds}")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public Result<Object> deleteSysUser(@PathVariable List<Long> userIds) {
        int count = sysUserService.removeSysUserByUserIds(userIds);
        return count == userIds.size() ? Result.ok(null) : Result.fail(BusinessStatus.OPERATION_FAIL);
    }

}
