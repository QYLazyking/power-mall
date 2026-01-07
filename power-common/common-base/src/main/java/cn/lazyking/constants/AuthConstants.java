package cn.lazyking.constants;

/**
 * 认证相关常量
 */
public class AuthConstants {
    /**
     * HTTP 请求头 Authorization 字段名
     */
    public static final String AUTHORIZATION = "Authorization";
    
    /**
     * Bearer token 前缀
     */
    public static final String BEARER = "Bearer ";
    
    /**
     * 认证 token 在缓存中的前缀
     */
    public static final String AUTH_TOKEN_PREFIX = "auth:token:";

    /**
     * 登录 URL
     */
    public static final String LOGIN_URL = "/doLogin";

    /**
     * 登出 URL
     */
    public static final String LOGOUT_URL = "/doLogout";

    /**
     * 登录类型
     */
    public static final String LOGIN_TYPE = "loginType";

    /**
     * 登录类型值：系统用户
     */
    public static final String SYS_USER_LOGIN = "sysUserLogin";

    /**
     * 登录类型值：商城用户
     */
    public static final String MEMBER_LOGIN = "memberLogin";

    /**
     * TOKEN 有效市场
     */
    public static final long TOKEN_TIME = 14400L;
}
