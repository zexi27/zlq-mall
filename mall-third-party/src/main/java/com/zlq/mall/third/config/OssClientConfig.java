package com.zlq.mall.third.config;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ProjectName:zlq-mall
 * @Package:com.zlq.mall.third.config
 * @ClassName: OssClientConfig
 * @description:
 * @author: LiQun
 l* @CreateDate:2022/12/8 20:48
 */
@Configuration
public class OssClientConfig {
    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endpoint;

    @Value("${spring.cloud.alicloud.access-key}")
    private String accessKeyId;

    @Value("${spring.cloud.alicloud.secret-key}")
    private String accessKeySecret;

    @Bean
    public OSSClient getOssClient(){
        return new OSSClient(endpoint,accessKeyId,accessKeySecret);
    }
}
