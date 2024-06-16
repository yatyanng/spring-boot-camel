package com.example.sb.camel.controller;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sb.camel.config.SbRouteBuilder;

@RestController
@RequestMapping("/encryption")
public class EncryptionController {

	@Value("${app-server.encryption.password}")
	private String password;

	@Value("${app-server.encryption.algorithm}")
	private String algorithm;

	@Autowired
	@Qualifier("camelContext")
	protected CamelContext camelContext;

	@GetMapping(value = { "/encrypt/{text}" })
	public ResponseEntity<Map<String, String>> encrypt(@PathVariable("text") String text) {
		String encryptedText = (String) camelContext.createProducerTemplate().sendBodyAndHeaders(
				SbRouteBuilder.encryptionService, ExchangePattern.InOut, text,
				Map.of("password", password, "algorithm", algorithm));
		return new ResponseEntity<>(Map.of("text", text, "encryptedText", encryptedText), HttpStatus.OK);
	}
}
