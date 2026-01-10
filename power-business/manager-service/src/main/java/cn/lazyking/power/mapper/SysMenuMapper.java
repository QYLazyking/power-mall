package cn.lazyking.power.mapper;

import cn.lazyking.power.domain.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Set;

public interface SysMenuMapper extends BaseMapper<SysMenu> {
    Set<SysMenu> queryMenuListByUserId(Long userId);
}