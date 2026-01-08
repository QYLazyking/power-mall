package cn.lazyking.power.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * swagger3 配置属性
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "swagger3")
public class SwaggerProperties {
    /**
     * 描述生成文档的包名
     */
    private String basePackage;

    /**
     * 标题
     */
    private String title;

    /**
     *  作者
     */
    private String author;

    /**
     * 主页路径
     */
    private String url;

    /**
     *  邮箱
     */
    private String email;

    /**
     *  描述
     */
    private String description;

    /**
     *  许可证
     */
    private String license;

    /**
     *  许可证 URL
     */
    private String licenseUrl;

    /**
     *  服务条款 URL
     */
    private String termsOfServiceUrl;

    /**
     * 版本号
     */
    private String version;
}
