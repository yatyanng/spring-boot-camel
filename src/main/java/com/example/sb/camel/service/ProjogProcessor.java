package com.example.sb.camel.service;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("projogProcessor")
public class ProjogProcessor implements Processor {

	@Autowired
	protected ProjogService projogService;

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		Map<String, Object> requestBody = (Map<String, Object>) exchange.getIn().getBody();

		exchange.getMessage().setBody(projogService.process(requestBody));
	}

}
