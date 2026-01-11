package cn.lazyking.power.service;

import cn.lazyking.power.domain.SysRole;
import cn.lazyking.power.mapper.SysRoleMapper;
import cn.lazyking.power.service.impl.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "cn.lazyking.power.service.SysRoleServiceImpl")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMapper sysRoleMapper;

    @Override
    @Cacheable(key = "'getSysRoleList'")
    public List<SysRole> getSysRoleList() {
        return sysRoleMapper.selectList(
                new LambdaQueryWrapper<SysRole>()
                        .orderByDesc(SysRole::getCreateTime)
        );
    }
}
