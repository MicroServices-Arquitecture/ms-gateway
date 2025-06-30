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
			ServerHttpResponse response = exchange.getResponse();
			HttpHeaders headers = response.getHeaders();

			System.out.println("============ NUEVA REQUEST ============");
			System.out.println("Método: " + request.getMethod());
			System.out.println("Path: " + request.getPath());
			System.out.println("Origin: " + request.getHeaders().getOrigin());

        	// Imprime headers antes
			System.out.println("--- HEADERS ANTES ---");
			headers.forEach((k, v) -> System.out.println(k + ": " + v));

			if (CorsUtils.isCorsRequest(request)){
				String origin = request.getHeaders().getOrigin();
				headers.remove("Access-Control-Allow-Origin");
				
				if ("http://localhost:5173".equals(origin) || 
					"https://react-front-microservicios-production.up.railway.app".equals(origin)){
						headers.set("Access-Control-Allow-Origin", origin);
				} 
				
				headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
				headers.set("Access-Control-Allow-Headers", "*");
				headers.set("Access-Control-Allow-Credentials", "true");

				if (request.getMethod() == HttpMethod.OPTIONS){
					response.setStatusCode(HttpStatus.OK);
					System.out.println("=== RESPUESTA OPCIONES COMPLETA ===");
					System.out.println("--- HEADERS DESPUÉS ---");
					headers.forEach((k, v) -> System.out.println(k + ": " + v));
					return response.setComplete();
				}
			}

			// Imprime headers después
			System.out.println("--- HEADERS DESPUÉS ---");
			headers.forEach((k, v) -> System.out.println(k + ": " + v));
			System.out.println("=====================================");

			return chain.filter(exchange);
		};
	}

}
