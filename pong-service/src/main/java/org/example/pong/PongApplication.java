package org.example.pong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * Pong服务程序入口
 *
 * @author Michael
 * @version 2024/6/21
 */
@EnableWebFlux
@SpringBootApplication
public class PongApplication {

    public static void main(String[] args) {
        SpringApplication.run(PongApplication.class, args);
    }

}
