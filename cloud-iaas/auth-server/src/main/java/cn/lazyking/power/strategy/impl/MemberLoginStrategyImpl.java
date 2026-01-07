package cn.lazyking.power.strategy.impl;

import cn.lazyking.constants.AuthConstants;
import cn.lazyking.power.strategy.LoginStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * 商城用户登录策略实现类
 */
@Service(AuthConstants.MEMBER_LOGIN)
public class MemberLoginStrategyImpl implements LoginStrategy {

    @Override
    public UserDetails doLogin(String name) {
        return null;
    }

}
