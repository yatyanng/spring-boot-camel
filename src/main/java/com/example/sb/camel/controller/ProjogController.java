package com.example.sb.camel.controller;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sb.camel.config.SbRouteBuilder;

@RestController
@RequestMapping("/projog")
public class ProjogController {

	@Autowired
	@Qualifier("camelContext")
	protected CamelContext camelContext;

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/query" })
	public ResponseEntity<Map<String, Object>> query(@RequestBody Map<String, Object> requestBody) {
		Map<String, Object> payload = (Map<String, Object>) camelContext.createProducerTemplate()
				.sendBody(SbRouteBuilder.projogService, ExchangePattern.InOut, requestBody);

		return new ResponseEntity<>(payload, HttpStatus.OK);
	}
}
