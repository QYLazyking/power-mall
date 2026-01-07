package cn.lazyking.power.config;

import cn.lazyking.constants.AuthConstants;
import cn.lazyking.constants.BusinessStatus;
import cn.lazyking.constants.HttpConstants;
import cn.lazyking.model.Result;
import cn.lazyking.power.model.LoginResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.time.Duration;
import java.util.UUID;

import static cn.lazyking.constants.AuthConstants.LOGIN_URL;
import static cn.lazyking.constants.AuthConstants.LOGOUT_URL;

/**
 * Security 框架配置类
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthSecurityConfig {

    private final ObjectMapper objectMapper;
    private final StringRedisTemplate redisTemplate;

    /**
     * 登录成功处理器
     * @return AuthenticationSuccessHandler
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // 响应头
            response.setContentType(HttpConstants.CONTENT_TYPE_JSON);
            response.setCharacterEncoding(HttpConstants.UTF_8);
            // 令牌
            String token = UUID.randomUUID().toString();
            // 从 security 框架中获取认证用户对象，并转成json格式字符串
            String userJsonStr = objectMapper.writeValueAsString(authentication.getPrincipal());
            redisTemplate
                    .opsForValue()
                    .set(
                            AuthConstants.AUTH_TOKEN_PREFIX + token,
                            userJsonStr,
                            Duration.ofSeconds(AuthConstants.TOKEN_TIME)
                    );
            // 创建一个响应结果对象
            LoginResult loginResult = new LoginResult(token, AuthConstants.TOKEN_TIME);
            Result<LoginResult> result = Result.ok(loginResult);
            // 返回结果
            response.getWriter().write(objectMapper.writeValueAsString(result));
            response.getWriter().flush();
        };
    }

    /**
     * 登录失败处理器
     * @return AuthenticationFailureHandler
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, e) -> {
            // 响应头
            response.setContentType(HttpConstants.CONTENT_TYPE_JSON);
            response.setCharacterEncoding(HttpConstants.UTF_8);

            // 统一响应结果对象
            Result<Object> result = new Result<>();
            result.setCode(BusinessStatus.OPERATION_FAIL.getCode());
            if(e instanceof BadCredentialsException) {
                result.setMsg("用户名或密码错误");
            } else if (e instanceof UsernameNotFoundException) {
                result.setMsg("用户不存在");
            } else if (e instanceof AccountExpiredException) {
                result.setMsg("账户已过期");
            } else if (e instanceof AccountStatusException) {
                result.setMsg("账户异常，请联系管理员");
            } else {
                result.setMsg(e.getMessage());
            }
            // 返回结果
            response.getWriter().write(objectMapper.writeValueAsString(result));
            response.getWriter().flush();
        };
    }

    /**
     * 登出成功处理器
     * @return LogoutSuccessHandler
     */
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            // 设置响应头信息
            response.setContentType(HttpConstants.CONTENT_TYPE_JSON);
            response.setCharacterEncoding(HttpConstants.UTF_8);
            // 从请求头中获取 token
            String authorization = request.getHeader(AuthConstants.AUTHORIZATION);
            String token = authorization.replaceFirst(AuthConstants.BEARER, "");
            // 将当前的 token 从 redis 中删除
            redisTemplate.delete(AuthConstants.AUTH_TOKEN_PREFIX + token);
            // 创建一个响应结果对象
            Result<Object> result = Result.ok(null);
            response.getWriter().write(objectMapper.writeValueAsString(result));
            response.getWriter().flush();
        };
    }

    /**
     * 密码编码器
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置 Security 过滤器链
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
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/oauth2/**", "/login", "/public/**").permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin(form -> form
                    .loginProcessingUrl(LOGIN_URL)
                    .successHandler(authenticationSuccessHandler())
                    .failureHandler(authenticationFailureHandler())
            )
            .logout(config -> config.logoutUrl(LOGOUT_URL).logoutSuccessHandler(logoutSuccessHandler()));

        return http.build();
    }


}
