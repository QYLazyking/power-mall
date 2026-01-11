package cn.lazyking.power.service.impl;

import cn.lazyking.power.domain.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysMenuService extends IService<SysMenu>{

    List<SysMenu> getUserMenuListByUserId(Long userId);

    List<SysMenu> getAllSysMenuList();

}
