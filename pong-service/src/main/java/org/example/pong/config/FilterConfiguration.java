package org.example.pong.config;

import org.example.pong.filter.ThrottlingControlFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

/**
 * @author Michael
 * @version 2024/6/23
 */
@Configuration
public class FilterConfiguration {

    @Bean
    public WebFilter rateLimitFilter() {
        return new ThrottlingControlFilter(1);
    }

}
