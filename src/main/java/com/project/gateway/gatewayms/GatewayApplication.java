package com.project.gateway.gatewayms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public WebFilter corsFilter(){
		return (ServerWebExchange exchange, WebFilterChain chain) ->{
			ServerHttpRequest request = exchange.getRequest();
			if (CorsUtils.isCorsRequest(request)){
				ServerHttpResponse response = exchange.getResponse();
				HttpHeaders headers = response.getHeaders();
                                // Avoid trailing slash which can break CORS matching
                                headers.add("Access-Control-Allow-Origin", "https://ms-gateway-production-97bb.up.railway.app");
				headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
				headers.add("Access-Control-Allow-Headers", "*");
				headers.add("Access-Control-Allow-Credentials", "true");

				if (request.getMethod() == HttpMethod.OPTIONS){
					response.setStatusCode(HttpStatus.OK);
					return response.setComplete();
				}
			}
			return chain.filter(exchange);
		};
	}

}
