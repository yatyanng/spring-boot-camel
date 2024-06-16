package com.example.sb.camel.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.math3.ml.neuralnet.Network;
import org.apache.commons.math3.ml.neuralnet.Neuron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.sb.camel.request.NeuralNetCreateRequest;
import com.github.benmanes.caffeine.cache.Cache;

@Component("neuralNetCreateProcessor")
public class NeuralNetCreateProcessor implements Processor {

	private static final Logger logger = LoggerFactory.getLogger(NeuralNetCreateProcessor.class);

	@Autowired
	protected Cache<Long, Network> networkCache;

	@Override
	public void process(Exchange exchange) throws Exception {
		NeuralNetCreateRequest createRequest = (NeuralNetCreateRequest) exchange.getIn().getBody();
		
		logger.info("createRequest: {}", createRequest);
		
		Long id = System.currentTimeMillis();
		Network neuralNet = new Network(id, createRequest.getFeatureSize());

		List<List<Double>> neurons = createRequest.getNeurons();
		final AtomicInteger indexCounter = new AtomicInteger(0);
		Map<Integer, Long> indexToNeuronIdMap = neurons.stream().map(neuronFeatures -> {
			int index = indexCounter.getAndIncrement();
			long neuronId = neuralNet.createNeuron(neuronFeatures.stream().mapToDouble(Double::doubleValue).toArray());
			return Pair.of(Integer.valueOf(index), Long.valueOf(neuronId));
		}).collect(Collectors.toMap(Pair::getLeft, Pair::getRight));

		logger.info("indexToNeuronIdMap: {}", indexToNeuronIdMap);
		List<List<Integer>> links = createRequest.getLinks();
		links.stream().forEach(link -> {
			if (link.size() > 1) {
				int indexA = link.get(0);
				int indexB = link.get(1);
				logger.info("linking indices: {}<->{}", indexA, indexB);
				Neuron neuronA = neuralNet.getNeuron(indexToNeuronIdMap.get(indexA));
				Neuron neuronB = neuralNet.getNeuron(indexToNeuronIdMap.get(indexB));
				neuralNet.addLink(neuronA, neuronB);
			}
		});
		networkCache.put(id, neuralNet);
		exchange.getMessage()
				.setBody(Triple.of(id, neuralNet, neuralNet.getNeurons(new Network.NeuronIdentifierComparator())));
	}

}
