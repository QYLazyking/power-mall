package cn.lazyking.power.controller;

import cn.lazyking.power.domain.SysUser;
import cn.lazyking.power.model.Result;
import cn.lazyking.power.service.impl.SysUserService;
import cn.lazyking.power.utils.AuthUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
