package com.example.sb.camel.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sb.camel.service.ProjogService;

@RestController
@RequestMapping("/projog")
public class ProjogController {

	@Autowired
	protected ProjogService projogService;

	@PostMapping(value = { "/query" })
	public ResponseEntity<Map<String, Object>> query(@RequestBody Map<String, Object> requestBody) {

		Map<String, Object> payload = projogService.process(requestBody);

		return new ResponseEntity<>(payload, HttpStatus.OK);
	}
}
