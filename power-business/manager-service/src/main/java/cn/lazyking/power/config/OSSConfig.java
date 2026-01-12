package cn.lazyking.power.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OSSConfig {

    @Value("${oss.endpoint}")
    private String endpoint;

    @Getter
    @Value("${oss.bucket-name}")
    private String bucketName;

    @Value("${oss.access-key-id}")
    private String accessKeyId;

    @Value("${oss.access-key-secrct}")
    private String accessKeySecret;

    /**
     * 阿里云 OSS 客户端
     * @return OSS
     */
    @Bean(destroyMethod = "shutdown")
    public OSS ossClient() {
        return OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(
                        CredentialsProviderFactory.newDefaultCredentialProvider(
                                accessKeyId,
                                accessKeySecret
                        )
                )
                .build();
    }

}
