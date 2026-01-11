package cn.lazyking.power.service;

import cn.lazyking.power.domain.SysMenu;
import cn.lazyking.power.mapper.SysMenuMapper;
import cn.lazyking.power.service.impl.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@CacheConfig(cacheNames = "cn.lazyking.power.service.SysMenuServiceImpl")
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    private final SysMenuMapper sysMenuMapper;

    @Cacheable(key = "#userId")
    @Override
    public List<SysMenu> getUserMenuListByUserId(Long userId) {
        Set<SysMenu> menus = sysMenuMapper.queryMenuListByUserId(userId);
        return transformTree(menus, 0);
    }

    private List<SysMenu> transformTree(Set<SysMenu> menus, long pid) {
        List<SysMenu> roots = menus.stream()
                .filter(menu -> menu.getParentId() == pid)
                .toList();

        roots.forEach(root -> root.setList(transformTree(menus, root.getMenuId())));
        return roots;
    }
}
