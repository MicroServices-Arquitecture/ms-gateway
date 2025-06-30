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

				String origin = request.getHeaders().getOrigin();
				headers.remove("Access-Control-Allow-Origin");
				if ("http://localhost:5173".equals(origin) || 
					"https://react-front-microservicios-production.up.railway.app".equals(origin)){
						headers.set("Access-Control-Allow-Origin", origin);
				} 
				// Avoid trailing slash which can break CORS matching
				headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
				headers.set("Access-Control-Allow-Headers", "*");
				headers.set("Access-Control-Allow-Credentials", "true");

				if (request.getMethod() == HttpMethod.OPTIONS){
					response.setStatusCode(HttpStatus.OK);
					return response.setComplete();
				}
			}
			return chain.filter(exchange);
		};
	}

}
