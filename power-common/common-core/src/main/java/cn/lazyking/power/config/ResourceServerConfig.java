package cn.lazyking.power.config;

import cn.lazyking.constants.BusinessStatus;
import cn.lazyking.constants.HttpConstants;
import cn.lazyking.model.Result;
import cn.lazyking.power.constants.ResourceConstants;
import cn.lazyking.power.filter.TokenTranslationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ResourceServerConfig {

    private final TokenTranslationFilter tokenTranslationFilter;
    private final ObjectMapper objectMapper;

    /**
     * 配置 Security 过滤器链
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception 抛出异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(tokenTranslationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        config -> config
                                .authenticationEntryPoint(authenticationEntryPoint())
                                .accessDeniedHandler(accessDeniedHandler())
                )
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(ResourceConstants.RESOURCE_ALLOW_URLS)
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                );
        return http.build();
    }

    /**
     * 处理没有携带 token 请求
     * @return AuthenticationEntryPoint
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, e) -> {
            // 设置响应头信息
            response.setContentType(HttpConstants.CONTENT_TYPE_JSON);
            response.setCharacterEncoding(HttpConstants.UTF_8);
            // 创建一个响应结果对象
            Result<Object> result = Result.fail(BusinessStatus.UNAUTHORIZED);
            response.getWriter().write(objectMapper.writeValueAsString(result));
            response.getWriter().flush();
        };
    }

    /**
     * 处理权限不足的请求
     * @return AccessDeniedHandler
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, e) -> {
            // 设置响应头信息
            response.setContentType(HttpConstants.CONTENT_TYPE_JSON);
            response.setCharacterEncoding(HttpConstants.UTF_8);
            // 创建一个响应结果对象
            Result<Object> result = Result.fail(BusinessStatus.ACCESS_DENIED);
            response.getWriter().write(objectMapper.writeValueAsString(result));
            response.getWriter().flush();
        };
    }

}
