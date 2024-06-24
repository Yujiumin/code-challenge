package org.example.pong.filter;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 节流控制过滤器
 *
 * @author Michael
 * @version 2024/6/22
 */
public class ThrottlingControlFilter implements WebFilter {

    private final Queue<Object> queue = new ConcurrentLinkedQueue<>();

    private final int rps;

    public ThrottlingControlFilter(int rps) {
        this.rps = rps;
        rateControl();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return check()
                .then(chain.filter(exchange))
                .onErrorResume(throwable -> handleRateLimited(exchange));
    }

    /**
     * 检查请求是否速率限制
     *
     * @return
     */
    private Mono<Void> check() {
        return Mono.defer(() -> {
            if (queue.size() >= rps) {
                return Mono.error(new RuntimeException("Rate Limited"));
            }
            queue.offer(0);
            return Mono.empty();
        });
    }

    /**
     * 速率控制
     */
    private void rateControl() {
        Mono.delay(Duration.ofMillis(1000))
                .repeat()
                .subscribe(s -> Flux.range(0, rps).subscribe(i -> queue.poll()));
    }

    /**
     * 处理速率限制
     *
     * @param serverWebExchange
     * @return
     */
    private Mono<Void> handleRateLimited(ServerWebExchange serverWebExchange) {
        final ServerHttpResponse response = serverWebExchange.getResponse();
        response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        return response.setComplete();
    }
}
