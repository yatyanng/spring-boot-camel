package com.example.sb.camel.config;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class SbRouteBuilder extends RouteBuilder {

	public static final String encryptionService = "direct://encryptionService";
	public static final String neuralNetCreateService = "direct://neuralNetCreateService";
	public static final String neuralNetUpdateService = "direct://neuralNetUpdateService";
	public static final String projogService = "direct://projogService";

	@Autowired
	@Qualifier("encryptionProcessor")
	private Processor encryptionProcessor;

	@Autowired
	@Qualifier("neuralNetCreateProcessor")
	private Processor neuralNetCreateProcessor;

	@Autowired
	@Qualifier("neuralNetUpdateProcessor")
	private Processor neuralNetUpdateProcessor;
	
	@Autowired
	@Qualifier("projogProcessor")
	private Processor projogProcessor;

	@Override
	public void configure() throws Exception {
		super.from(encryptionService).process(encryptionProcessor).end();
		super.from(neuralNetCreateService).process(neuralNetCreateProcessor).end();
		super.from(neuralNetUpdateService).process(neuralNetUpdateProcessor).end();
		super.from(projogService).process(projogProcessor).end();
	}

}
