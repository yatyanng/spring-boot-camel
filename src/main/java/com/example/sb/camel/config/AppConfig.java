package com.example.sb.camel.config;

import java.time.Duration;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.commons.math3.ml.neuralnet.Network;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
public class AppConfig {

	@Bean("camelContext")
	public CamelContext camelContext() throws Exception {
		CamelContext camelContext = new DefaultCamelContext();
		camelContext.addRoutes(sbRouteBuilder());
		camelContext.start();
		return camelContext;
	}

	@Bean("sbRouteBuilder")
	public RouteBuilder sbRouteBuilder() {
		return new SbRouteBuilder();
	}

	@Bean("networkCache")
	public Cache<Long, Network> networkCache() {
		return Caffeine.newBuilder().expireAfterWrite(Duration.ofDays(1)).build();
	}
}
