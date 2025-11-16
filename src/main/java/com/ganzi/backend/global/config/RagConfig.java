package com.ganzi.backend.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import reactor.netty.http.client.HttpClient;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class RagConfig {


    @Bean
    public WebClient ragApiClient() {
        HttpClient httpClient = HttpClient.create()
                // 연결 타임아웃 5초 설정
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                // 응답 전체 타임아웃 60초 설정
                .responseTimeout(Duration.ofSeconds(60))
                .doOnConnected(conn ->
                        // 읽기 타임아웃 60초 설정 (데이터를 읽을 때의 지연 시간)
                        conn.addHandlerLast(new ReadTimeoutHandler(60, TimeUnit.SECONDS)));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
