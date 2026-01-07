package cn.lazyking.power.impl;

import cn.lazyking.constants.AuthConstants;
import cn.lazyking.power.factory.LoginStrategyFactory;
import cn.lazyking.power.strategy.LoginStrategy;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 用户详情服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final LoginStrategyFactory loginStrategyFactory;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 获取请求对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 从请求头中获取登录类型
        String loginType = request.getHeader(AuthConstants.LOGIN_TYPE);
        // 获取对应的登录策略实例
        LoginStrategy instance = loginStrategyFactory.getInstance(loginType);
        // 调用登录策略实例的 doLogin 方法
        return instance.doLogin(username);
    }

}
