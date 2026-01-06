package cn.lazyking.power.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@RefreshScope
@ConfigurationProperties(prefix = "gateway.auth-path")
public class AuthPathConfig {

    /**
     * 允许访问的 url
     */
    private List<String> allowUrls;

}
