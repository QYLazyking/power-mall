package cn.lazyking.power.strategy;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * 登录策略接口
 */
public interface LoginStrategy {

    UserDetails doLogin(String name);

}
