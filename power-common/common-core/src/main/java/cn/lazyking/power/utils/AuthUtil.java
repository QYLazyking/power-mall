package cn.lazyking.power.utils;

import cn.lazyking.power.model.SecurityUser;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {

    /**
     * 获取当前认证用户
     * @return 当前认证用户
     */
    public static SecurityUser getCurrentUser() {
        return (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 获取当前认证用户标识
     * @return 当前认证用户标识
     */
    public static long getCurrentUserId() {
        return getCurrentUser().getUserId();
    }

}
