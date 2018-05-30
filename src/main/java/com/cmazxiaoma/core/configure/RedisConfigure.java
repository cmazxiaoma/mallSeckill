package com.cmazxiaoma.core.configure;

import com.cmazxiaoma.core.cache.CacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cmazxiaoma
 * @version V1.0
 * @Description: TODO
 * @date 2018/4/28 15:32
 */
@Configuration
public class RedisConfigure {

    @Bean
    public CacheManager cacheManager(@Value("${spring.redis.host}") String ip,
                                     @Value("${spring.redis.port}") Integer port) {
        return new CacheManager(ip, port);
    }
}
