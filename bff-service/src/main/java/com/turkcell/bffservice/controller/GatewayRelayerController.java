package com.turkcell.bffservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class GatewayRelayerController {
    private final WebClient webClient;

    public GatewayRelayerController(WebClient webClient) {
        this.webClient = webClient;
    }

    @RequestMapping("/**")
    public Mono<ResponseEntity<byte[]>> relay(ServerWebExchange exchange,
                                              @RequestBody(required = false) Mono<byte[]> body) {
        URI fullPath = exchange.getRequest().getURI();
        String downStreamPath = exchange.getRequest().getURI().getPath();
        String query = exchange.getRequest().getURI().getRawQuery();
        // id=1&name=abc&c=a

        // localhost:8989/api/v1/products?page=0

        String pathWithQuery = query != null ? downStreamPath + "?" + query : downStreamPath;

        String fullRequestPath = "http://gateway-server/" + pathWithQuery;

        return webClient
                .method(exchange.getRequest().getMethod())
                .uri(fullRequestPath)
                .body(body != null ? BodyInserters.fromPublisher(body, byte[].class) : BodyInserters.empty())
                .exchangeToMono(response -> response.toEntity(byte[].class));
    }
}
