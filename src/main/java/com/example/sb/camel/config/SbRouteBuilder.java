package com.example.sb.camel.config;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class SbRouteBuilder extends RouteBuilder {

	@Autowired
	@Qualifier("encryptionProcessor")
	private Processor encryptionProcessor;

	@Override
	public void configure() throws Exception {
		from("direct:encryptionService").process(encryptionProcessor).end();
	}

}
