package org.example.ping.task;

import org.example.ping.lock.RateLimitLock;
import org.example.ping.log.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.channels.FileLock;

/**
 * @author Michael
 * @version 2024/6/23
 */
@Component
public class PingTask {

    private static final WebClient webClient = WebClient.builder()
            .baseUrl("http://127.0.0.1:8888")
            .build();

    @Scheduled(cron = "0/1 * * * * ?")
    public void sayHello() {
        RateLimitLock.tryLock(PingTask::sayHello, PingTask::handleLockFailed);
    }

    public static void sayHello(FileLock fileLock) {
        final WebClient.RequestBodySpec requestBodySpec = webClient.post().uri("/hello");
        requestBodySpec.exchangeToMono(clientResponse -> {
                    final HttpStatusCode httpStatusCode = clientResponse.statusCode();
                    if (httpStatusCode.isSameCodeAs(HttpStatus.TOO_MANY_REQUESTS)) {
                        return Mono.just("Request sent and Pong Service throttled it");
                    }
                    return clientResponse
                            .bodyToMono(String.class)
                            .map(message -> "Request sent and Pong Service response => " + message);
                })
                .subscribe(Logger::log);

        RateLimitLock.releaseLock(fileLock);
    }

    public static void handleLockFailed(Exception ex) {
        if (ex instanceof NullPointerException) {
            Logger.log("Request not send as being 'Rate Limited'");
        } else {
            Logger.log("Lock Failed");
        }
    }
}
