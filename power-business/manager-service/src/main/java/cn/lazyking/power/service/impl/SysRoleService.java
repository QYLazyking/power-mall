package cn.lazyking.power.service.impl;

import cn.lazyking.power.domain.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysRoleService extends IService<SysRole>{


    List<SysRole> getSysRoleList();

}
