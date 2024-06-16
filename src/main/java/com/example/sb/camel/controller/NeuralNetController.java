package com.example.sb.camel.controller;

import java.util.Collection;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.math3.ml.neuralnet.Network;
import org.apache.commons.math3.ml.neuralnet.Neuron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sb.camel.config.SbRouteBuilder;
import com.example.sb.camel.request.NeuralNetCreateRequest;
import com.example.sb.camel.request.NeuralNetUpdateRequest;
import com.github.benmanes.caffeine.cache.Cache;

@RestController
@RequestMapping("/neuralNet")
public class NeuralNetController {

	private static final Logger logger = LoggerFactory.getLogger(NeuralNetController.class);

	@Autowired
	protected Cache<Long, Network> networkCache;

	@Autowired
	@Qualifier("camelContext")
	protected CamelContext camelContext;

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/create" })
	public ResponseEntity<Map<String, Object>> create(@RequestBody NeuralNetCreateRequest neuralNetRequest) {
		Triple<Long, Network, Collection<Neuron>> netInfo = (Triple<Long, Network, Collection<Neuron>>) camelContext
				.createProducerTemplate()
				.sendBody(SbRouteBuilder.neuralNetCreateService, ExchangePattern.InOut, neuralNetRequest);
		logger.info("netInfo: {}", netInfo);
		return new ResponseEntity<>(
				Map.of("id", netInfo.getLeft(), "network", netInfo.getMiddle(), "neurons", netInfo.getRight()),
				HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/update" })
	public ResponseEntity<Map<String, Object>> update(@RequestBody NeuralNetUpdateRequest neuralNetRequest) {
		Triple<Long, Network, Collection<Neuron>> netInfo = (Triple<Long, Network, Collection<Neuron>>) camelContext
				.createProducerTemplate()
				.sendBody(SbRouteBuilder.neuralNetUpdateService, ExchangePattern.InOut, neuralNetRequest);
		logger.info("netInfo: {}", netInfo);
		return new ResponseEntity<>(
				Map.of("id", netInfo.getLeft(), "network", netInfo.getMiddle(), "neurons", netInfo.getRight()),
				HttpStatus.OK);
	}

	@GetMapping(value = { "/read/{neuralNetId}" })
	public ResponseEntity<Map<String, Object>> update(@PathVariable Long neuralNetId) {
		Network neuralNet = networkCache.getIfPresent(neuralNetId);
		if (neuralNet == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(Map.of("id", neuralNetId, "network", neuralNet, "neurons",
				neuralNet.getNeurons(new Network.NeuronIdentifierComparator())), HttpStatus.OK);
	}
}
