package cn.lazyking.power.controller;

import cn.lazyking.power.domain.SysMenu;
import cn.lazyking.power.model.Result;
import cn.lazyking.power.model.SecurityUser;
import cn.lazyking.power.service.impl.SysMenuService;
import cn.lazyking.power.utils.AuthUtil;
import cn.lazyking.power.vo.MenuAndAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 系统权限控制层
 */
@RestController
@RequestMapping("sys/menu")
@Api(tags = "系统权限接口管理")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService sysMenuService;

    @GetMapping("nav")
    @ApiOperation("查询用户的菜单权限和操作权限")
    public Result<MenuAndAuth> loadUserMenuAndAuth() {
        // 获取当前登录用户
        SecurityUser currentUser = AuthUtil.getCurrentUser();
        // 根据用户标识查询操作权限集合
        Set<String> perms = currentUser.getPerms();
        // 根据用户标识查询菜单权限集合
        List<SysMenu> menus = sysMenuService.getUserMenuListByUserId(currentUser.getUserId());
        // 构建 MenuAndAuth 对象
        MenuAndAuth menuAndAuth = new MenuAndAuth(perms, menus);
        return Result.ok(menuAndAuth);
    }

}
