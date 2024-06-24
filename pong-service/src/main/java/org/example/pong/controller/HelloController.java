package org.example.pong.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Hello请求控制器
 *
 * @author Michael
 * @version 2024/6/22
 */
@RestController
public class HelloController {

    @PostMapping("/hello")
    public Mono<String> hello() {
        return Mono.just("World");
    }


}
