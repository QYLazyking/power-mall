package cn.lazyking.power.service.impl;

import cn.lazyking.power.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysUserService extends IService<SysUser>{


    Integer addSysUser(SysUser sysUser);

    SysUser getSysUserByUserId(Long userId);

    int modifySysUser(SysUser sysUser);

    int removeSysUserByUserIds(List<Long> userIds);
}
