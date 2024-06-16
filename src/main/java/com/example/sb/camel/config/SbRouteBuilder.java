package com.example.sb.camel.config;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class SbRouteBuilder extends RouteBuilder {

	public static final String neuralNetCreateService = "direct://neuralNetCreateService";
	public static final String neuralNetUpdateService = "direct://neuralNetUpdateService";
	public static final String encryptionService = "direct://encryptionService";

	@Autowired
	@Qualifier("encryptionProcessor")
	private Processor encryptionProcessor;

	@Autowired
	@Qualifier("neuralNetCreateProcessor")
	private Processor neuralNetCreateProcessor;

	@Autowired
	@Qualifier("neuralNetUpdateProcessor")
	private Processor neuralNetUpdateProcessor;

	@Override
	public void configure() throws Exception {
		from(encryptionService).process(encryptionProcessor).end();
		from(neuralNetCreateService).process(neuralNetCreateProcessor).end();
		from(neuralNetUpdateService).process(neuralNetUpdateProcessor).end();
	}

}
