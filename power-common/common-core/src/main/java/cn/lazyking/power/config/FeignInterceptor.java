package cn.lazyking.power.config;

import cn.lazyking.constants.AuthConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Feign 拦截器
 */
@Component
public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 在这里添加你需要的请求头或其他拦截逻辑
        // 例如：传递认证信息、请求头等
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String authorization = request.getHeader(AuthConstants.AUTHORIZATION);
            requestTemplate.header(AuthConstants.AUTHORIZATION, authorization);
            return;
        }
        requestTemplate.header(AuthConstants.AUTHORIZATION, AuthConstants.BEARER + "31bd44ec-e302-4273-b794-d69cfffb28b2");
    }
}
