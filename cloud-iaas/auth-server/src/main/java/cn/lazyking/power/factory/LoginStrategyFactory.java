package cn.lazyking.power.factory;

import cn.lazyking.power.strategy.LoginStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 登录策略工厂
 */
@Component
@RequiredArgsConstructor
public class LoginStrategyFactory {
    private final Map<String, LoginStrategy> strategyMap;

    public LoginStrategy getInstance(String loginType) {
        return strategyMap.get(loginType);
    }
}
