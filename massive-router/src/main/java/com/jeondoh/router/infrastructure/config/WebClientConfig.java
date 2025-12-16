package com.jeondoh.router.infrastructure.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Value("${webclient.timeout-seconds}")
    private int timeoutSeconds;

    @Value("${webclient.pending-timeout}")
    private int pendingTimeout;

    @Value("${webclient.max-connections}")
    private int maxConnections;

    @Value("${webclient.max-count}")
    private int maxCount;

    @Value("${webclient.max-idle-time}")
    private int maxIdleTime;

    @Value("${webclient.max-life-time}")
    private int maxLifeTime;

    @Value("${webclient.evict-background-time}")
    private int evictBackgroundTime;

    @Bean
    public ConnectionProvider connectionProvider() {
        return ConnectionProvider.builder("webclient-custom-provider")
                .maxConnections(maxConnections)                                 // 최대 연결 수
                .pendingAcquireMaxCount(maxCount)                               // 대기 큐 크기
                .pendingAcquireTimeout(Duration.ofSeconds(pendingTimeout))      // 대기 타임아웃
                .maxIdleTime(Duration.ofSeconds(maxIdleTime))                   // 유휴 연결 유지
                .maxLifeTime(Duration.ofMinutes(maxLifeTime))                   // 연결 최대 생존 시간
                .evictInBackground(Duration.ofSeconds(evictBackgroundTime))     // 백그라운드 정리
                .build();
    }

    @Bean
    public WebClient webClient(ConnectionProvider connectionProvider) {
        HttpClient httpClient = HttpClient.create(connectionProvider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutSeconds * 1000)
                .responseTimeout(Duration.ofSeconds(timeoutSeconds))
                .doOnConnected(conn -> {
                    conn.addHandlerFirst(new ReadTimeoutHandler(timeoutSeconds))
                            .addHandlerLast(new WriteTimeoutHandler(timeoutSeconds));
                });

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
