package cn.lazyking.power.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.lazyking.power.domain.SysUser;
import cn.lazyking.power.domain.SysUserRole;
import cn.lazyking.power.mapper.SysUserMapper;
import cn.lazyking.power.service.impl.SysRoleService;
import cn.lazyking.power.service.impl.SysUserRoleService;
import cn.lazyking.power.service.impl.SysUserService;
import cn.lazyking.power.utils.AuthUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService{

    private final SysUserMapper sysUserMapper;
    private final SysRoleService sysRoleService;
    private final SysUserRoleService sysUserRoleService;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 新增管理员
     * @param sysUser 系统用户
     * @return 添加结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addSysUser(SysUser sysUser) {
        // 获取当前登录用户的 id
        long currentUserId = AuthUtil.getCurrentUserId();
        // 设置值
        sysUser.setCreateUserId(currentUserId);
        sysUser.setCreateTime(new Date());
        sysUser.setStatus(1);
        sysUser.setShopId(1L);

        // 密码加密
        String password = sysUser.getPassword();
        if(StringUtils.hasText(password)) {
            sysUser.setPassword(passwordEncoder.encode(password));
        }

        // 保存管理员信息
        int count = sysUserMapper.insert(sysUser);

        // 保存角色信息
        if(count > 0) {
            // 角色 id 集合
            List<Long> roleIdList = sysUser.getRoleIdList();
            if(CollectionUtil.isNotEmpty(roleIdList)) {
                // 创建 SysUserRole 对象集合
                List<SysUserRole> sysUserRoleList = roleIdList.stream().map(roleId -> {
                    SysUserRole sysUserRole = new SysUserRole();
                    sysUserRole.setUserId(sysUser.getUserId());
                    sysUserRole.setRoleId(roleId);
                    return sysUserRole;
                }).toList();
                // 批量保存
                sysUserRoleService.saveBatch(sysUserRoleList);
            }
        }
        return count;
    }

    @Override
    public SysUser getSysUserByUserId(Long userId) {
        // 查询管理员详情
        SysUser sysUser = this.getById(userId);
        // 查询该管理员拥有的角色列表
        List<SysUserRole> sysUserRoleList = sysUserRoleService.lambdaQuery()
                .eq(SysUserRole::getUserId, userId)
                .list();
        if(CollectionUtil.isNotEmpty(sysUserRoleList)) {
            // 角色 id 集合
            List<Long> roleIdList = sysUserRoleList.stream().map(SysUserRole::getRoleId).toList();
            sysUser.setRoleIdList(roleIdList);
        }
        return sysUser;
    }
}
