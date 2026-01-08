package cn.lazyking.power.constants;

public class ResourceConstants {
    public static final String[] RESOURCE_ALLOW_URLS = {
            // swagger 相关路径
            "/v2/api-docs",
            "/v3/api-docs",
            "/swagger-resources/configuration/ui",  //用来获取支持的动作
            "/swagger-resources",                   //用来获取api-docs的URI
            "/swagger-resources/configuration/security",//安全选项
            "/webjars/**",
            "/swagger-ui/**",
            "/druid/**",
            "/actuator/**"

    };
}
