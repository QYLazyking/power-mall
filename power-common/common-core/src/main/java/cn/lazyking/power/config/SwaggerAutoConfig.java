package cn.lazyking.power.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.HashSet;

/**
 * Swagger 自动装配
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerAutoConfig {
    private final SwaggerProperties swaggerProperties;
    private final Environment environment;

    @Bean
    public Docket docket() {
        // swagger 开关，默认开发环境开启，生产环境关闭
        boolean flag = true;
        String[] activeProfiles = environment.getActiveProfiles();
        for (String activeProfile : activeProfiles) {
            if ("pro".equals(activeProfile)) {
                flag = false;
                break;
            }
        }
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(new ApiInfo(
                        swaggerProperties.getTitle(),
                        swaggerProperties.getDescription(),
                        swaggerProperties.getVersion(),
                        swaggerProperties.getTermsOfServiceUrl(),
                        new Contact(
                                swaggerProperties.getAuthor(),
                                swaggerProperties.getUrl(),
                                swaggerProperties.getEmail()
                        ),
                        swaggerProperties.getLicense(),
                        swaggerProperties.getLicenseUrl(),
                        new HashSet<>()
                ))
                .enable(flag)
                .select()
                .apis(
                        RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage())
                )
                .build();
     }
}
