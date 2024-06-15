package com.example.sb.camel.config;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
