package cn.lazyking.power.strategy.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.lazyking.constants.AuthConstants;
import cn.lazyking.model.SecurityUser;
import cn.lazyking.power.domain.LoginSysUser;
import cn.lazyking.power.mapper.LoginSysUserMapper;
import cn.lazyking.power.strategy.LoginStrategy;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 系统用户登录策略实现类
 */
@Service(AuthConstants.SYS_USER_LOGIN)
@RequiredArgsConstructor
public class SysUserLoginStrategyImpl implements LoginStrategy {

    private final LoginSysUserMapper loginSysUserMapper;

    @Override
    public UserDetails doLogin(String name) {
        LoginSysUser loginSysUser = loginSysUserMapper.selectOne(
                new LambdaQueryWrapper<LoginSysUser>()
                        .eq(LoginSysUser::getUsername, name)
        );
        if(ObjectUtil.isNotNull(loginSysUser)) {
            // 根据用户表示查询用户的权限集合
            Set<String> perms = loginSysUserMapper.selectPermsByUserId(loginSysUser.getUserId());
            // 创建安全用户对象
            SecurityUser securityUser = new SecurityUser();
            securityUser.setUserId(loginSysUser.getUserId());
            securityUser.setUsername(loginSysUser.getUsername()); // 设置用户名
            securityUser.setPassword(loginSysUser.getPassword());
            securityUser.setStatus(loginSysUser.getStatus());
            securityUser.setShopId(loginSysUser.getShopId());
            securityUser.setLoginType(AuthConstants.SYS_USER_LOGIN);

            if(!perms.isEmpty()) {
                securityUser.setPerms(perms);
            }

            return securityUser;
        }

        return null;
    }
}
