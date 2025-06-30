package com.project.gateway.gatewayms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public WebFilter corsFilter() {
		return (ServerWebExchange exchange, WebFilterChain chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			ServerHttpResponse response = exchange.getResponse();
			HttpHeaders headers = response.getHeaders();

			String origin = request.getHeaders().getOrigin();
			if (CorsUtils.isCorsRequest(request)) {
				headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
				headers.add("Access-Control-Allow-Headers", "*");
				headers.add("Access-Control-Allow-Credentials", "true");

				if ("https://react-front-microservicios-production.up.railway.app".equals(origin)) {
					headers.add("Access-Control-Allow-Origin", origin);
				}
				if (request.getMethod() == HttpMethod.OPTIONS) {
					response.setStatusCode(HttpStatus.OK);
					return response.setComplete();
				}
			}
			return chain.filter(exchange);
		};
	}
}



