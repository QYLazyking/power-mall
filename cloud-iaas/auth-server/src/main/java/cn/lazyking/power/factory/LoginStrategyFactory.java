package cn.lazyking.power.factory;

import cn.lazyking.power.strategy.LoginStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 登录策略工厂
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginStrategyFactory {
    private final Map<String, LoginStrategy> strategyMap;

    public LoginStrategy getInstance(String loginType) {
        log.info("strategyMap.size()：{}", strategyMap.size());
        log.info("loginType：{}", loginType);
        return strategyMap.get(loginType);
    }
}
