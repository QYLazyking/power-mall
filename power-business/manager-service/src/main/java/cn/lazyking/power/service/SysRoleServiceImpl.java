package cn.lazyking.power.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.lazyking.power.domain.SysRole;
import cn.lazyking.power.domain.SysRoleMenu;
import cn.lazyking.power.mapper.SysRoleMapper;
import cn.lazyking.power.service.impl.SysRoleMenuService;
import cn.lazyking.power.service.impl.SysRoleService;
import cn.lazyking.power.utils.AuthUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "cn.lazyking.power.service.SysRoleServiceImpl")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMapper sysRoleMapper;
    private final SysRoleMenuService sysRoleMenuService;

    @Override
    @Cacheable(key = "'getSysRoleList'")
    public List<SysRole> getSysRoleList() {
        return sysRoleMapper.selectList(
                new LambdaQueryWrapper<SysRole>()
                        .orderByDesc(SysRole::getCreateTime)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addSysRole(SysRole sysRole) {
        // 当前登录用户 id
        long currentUserId = AuthUtil.getCurrentUserId();
        // 补充角色信息
        sysRole.setCreateUserId(currentUserId);
        sysRole.setCreateTime(new Date());
        // 保存角色
        int count = sysRoleMapper.insert(sysRole);
        if(count <= 0) {
            return false;
        }
        // 角色 id
        Long roleId = sysRole.getRoleId();
        // 获取角色权限 id 列表
        List<Long> menuIdList = sysRole.getMenuIdList();
        if(CollectionUtil.isNotEmpty(menuIdList)) {
            List<SysRoleMenu> sysRoleMenuList = menuIdList.stream().map(menuId -> {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(roleId);
                sysRoleMenu.setMenuId(menuId);
                return sysRoleMenu;
            }).toList();
            // 批量保存
            sysRoleMenuService.saveBatch(sysRoleMenuList);
        }
        return true;
    }

    @Override
    public SysRole getSysRoleDetailById(Long roleId) {
        // 获取角色信息
        SysRole sysRole = sysRoleMapper.selectById(roleId);
        // 获取角色权限信息
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.list(
                new LambdaQueryWrapper<SysRoleMenu>()
                        .eq(SysRoleMenu::getRoleId, roleId)
        );
        if(CollectionUtil.isNotEmpty(sysRoleMenuList)) {
            // 获取权限 id 集合
            List<Long> menuIdList = sysRoleMenuList
                    .stream()
                    .map(SysRoleMenu::getMenuId)
                    .toList();
            sysRole.setMenuIdList(menuIdList);
        }
        return sysRole;
    }

    @Override
    public boolean modifySysRole(SysRole sysRole) {
        // 获取角色 id
        Long roleId = sysRole.getRoleId();
        // 删除角色原有的权限
        sysRoleMenuService.remove(
                new LambdaQueryWrapper<SysRoleMenu>()
                        .eq(SysRoleMenu::getRoleId, roleId)
        );
        // 获取新的权限 id 集合
        List<Long> menuIdList = sysRole.getMenuIdList();
        if(CollectionUtil.isNotEmpty(menuIdList)) {
            List<SysRoleMenu> sysRoleMenuList = menuIdList
                    .stream()
                    .map(menuId -> {
                        SysRoleMenu sysRoleMenu = new SysRoleMenu();
                        sysRoleMenu.setRoleId(roleId);
                        sysRoleMenu.setMenuId(menuId);
                        return sysRoleMenu;
                    }).toList();
            sysRoleMenuService.saveBatch(sysRoleMenuList);
        }
        // 修改角色信息
        return this.updateById(sysRole);
    }

    @Override
    public boolean removeSysRolesByIds(List<Long> roleIds) {
        // 删除用户
        sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>()
                .in(SysRoleMenu::getRoleId, roleIds)
        );
        return this.removeByIds(roleIds);
    }
}
