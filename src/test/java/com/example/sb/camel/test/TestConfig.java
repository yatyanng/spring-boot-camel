package com.example.sb.camel.test;

import java.time.Duration;
import java.util.UUID;

import org.apache.commons.math3.ml.neuralnet.Network;
import org.projog.api.Projog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.sb.camel.service.ProjogService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
public class TestConfig {

	@Bean("networkCache")
	public Cache<Long, Network> networkCache() {
		return Caffeine.newBuilder().expireAfterWrite(Duration.ofDays(1)).build();
	}
	
	@Bean("projogCache")
	public Cache<UUID, Projog> projogCache() {
		return Caffeine.newBuilder().expireAfterWrite(Duration.ofDays(1)).build();
	}
	
	@Bean("projogService")
	public ProjogService projogService() {
		return new ProjogService();
	}
}
