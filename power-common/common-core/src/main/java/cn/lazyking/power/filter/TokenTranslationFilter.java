package cn.lazyking.power.filter;

import cn.lazyking.constants.AuthConstants;
import cn.lazyking.model.SecurityUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TokenTranslationFilter extends OncePerRequestFilter {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Token转换过滤器
     * 1. 获取token
     * 2. 将token装欢为用户信息对象
     * 3. 将用户信息对象放入SecurityContextHolder中
     *
     * @param request     请求对象
     * @param response    响应对象
     * @param filterChain 过滤器链
     * @throws ServletException Servlet 异常
     * @throws IOException      IO 异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从请求头中获取 token
        String authorization = request.getHeader(AuthConstants.AUTHORIZATION);
        if (StringUtils.hasText(authorization)) {
            String token = authorization.replaceFirst(AuthConstants.BEARER, "");
            if (StringUtils.hasText(token)) {
                // 解决 token 续签问题
                // 获取 token 剩余存活时长
                Long expire = redisTemplate.getExpire(AuthConstants.AUTH_TOKEN_PREFIX + token);
                // 判断是否超过阈值
                if(expire < AuthConstants.TOKEN_THRESHOLD) {
                    // 给当前用户的 token 续签
                    redisTemplate.expire(AuthConstants.AUTH_TOKEN_PREFIX + token, AuthConstants.TOKEN_TIME, TimeUnit.SECONDS);
                }
                String userDetailStr = redisTemplate.opsForValue().get(AuthConstants.AUTH_TOKEN_PREFIX + token);
                // 将用户信息对象放入 SecurityContextHolder 中
                SecurityUser securityUser = objectMapper.readValue(userDetailStr, SecurityUser.class);
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(
                                new UsernamePasswordAuthenticationToken(
                                        securityUser,
                                        null,
                                        securityUser.getPerms()
                                                .stream()
                                                .map(SimpleGrantedAuthority::new)
                                                .collect(Collectors.toSet())
                                )
                        );
            }
        }
        filterChain.doFilter(request, response);
    }
}
