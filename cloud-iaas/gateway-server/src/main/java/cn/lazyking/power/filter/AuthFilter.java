package cn.lazyking.power.filter;


import cn.lazyking.constants.AuthConstants;
import cn.lazyking.constants.BusinessStatus;
import cn.lazyking.constants.HttpConstants;
import cn.lazyking.model.Result;
import cn.lazyking.power.config.AuthPathConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter implements GlobalFilter, Ordered {

    private final AuthPathConfig authPathConfig;
    private final StringRedisTemplate redisTemplate;

    @Override
    public int getOrder() {
        return -5;
    }

    /**
     * 校验 token
     * 1. 获取请求路径
     * 2. 判断请求路径是否需要校验
     *     不需要：放行
     *     需要：对其进行身份认证
     * @param exchange 请求对象
     * @param chain 过滤器链
     * @return 响应对象
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求对象
        ServerHttpRequest request = exchange.getRequest();
        // 获取请求路径
        RequestPath path = request.getPath();
        // 判断请求路径是否需要校验
        if (authPathConfig.getAllowUrls().contains(path.toString())) {
            log.info("请求路径：{}，不需要进行身份认证", path);
            return chain.filter(exchange);
        }
        // 需要进行身份认证
        String authValue = request.getHeaders().getFirst(AuthConstants.AUTHORIZATION);
        if (StringUtils.hasText(authValue)) {
            String token = authValue.replaceFirst(AuthConstants.BEARER, "");
            if (StringUtils.hasText(token) && redisTemplate.hasKey(AuthConstants.AUTH_TOKEN_PREFIX + token)) {
                return chain.filter(exchange);
            }
        }

        log.error("认证失败：{} -> {}", request.getLocalAddress(), path);

        // 获取响应对象
        ServerHttpResponse response = exchange.getResponse();
        // 设置响应头
        response.getHeaders().set(HttpConstants.CONTENT_TYPE, HttpConstants.CONTENT_TYPE_JSON);
        // 设置响应体
        Result<Object> result = Result.fail(BusinessStatus.UNAUTHORIZED);
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] resultBytes;
        try {
            resultBytes = objectMapper.writeValueAsBytes(result);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return response.writeWith(Mono.just(response.bufferFactory().wrap(resultBytes)));
    }
}
