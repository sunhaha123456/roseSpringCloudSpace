package com.rose.conf;

import com.rose.common.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootApplication
public class Application {

    private final static List<String> URL_NOT_ARROW = Arrays.asList();

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @Order(0)
    public GlobalFilter a() { // 定义 springcloud gateway 全局过滤器，数值越小，越先执行
        return (exchange, chain) -> {
            ServerHttpRequest httpRequest = exchange.getRequest();
            String method = httpRequest.getMethodValue().toUpperCase();
            if ("OPTIONS".equals(method)) {
                return errorHandle(exchange);
            }
            // 访问 http://127.0.0.1:8888/rose-content-server/login/toSuccess?token=940a0b9140
            // 获取到的是 rose-content-server/login/toSuccess
            String url = httpRequest.getPath().toString();
            if (URL_NOT_ARROW.contains(url)) {
                log.error("禁止访问链接：{}", url);
                return errorHandle(exchange);
            }
            log.info("request url：{}，method：{}，放行！", url, method);
            return chain.filter(exchange);
        };
    }

    @Bean
    public CorsWebFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsWebFilter(source);
    }

    private CorsConfiguration buildConfig(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        return corsConfiguration;
    }

    private Mono errorHandle(ServerWebExchange exchange) {
        ServerHttpResponse httpResponse = exchange.getResponse();
        httpResponse.setStatusCode(HttpStatus.OK);
        String errorMsg = errorMsg();
        byte[] errorMsgBytes = errorMsg.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = httpResponse.bufferFactory().wrap(errorMsgBytes);
        return httpResponse.writeWith(Flux.just(buffer));
    }

    private String errorMsg() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 500);
        map.put("msg", "server error");
        return JsonUtil.objectToJson(map);
    }
}