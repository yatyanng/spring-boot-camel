package com.example.sb.camel.service;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.math3.ml.distance.CanberraDistance;
import org.apache.commons.math3.ml.distance.ChebyshevDistance;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EarthMoversDistance;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.ml.distance.ManhattanDistance;
import org.apache.commons.math3.ml.neuralnet.Network;
import org.apache.commons.math3.ml.neuralnet.sofm.KohonenUpdateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.sb.camel.request.NeuralNetUpdateRequest;
import com.github.benmanes.caffeine.cache.Cache;

@Component("neuralNetUpdateProcessor")
public class NeuralNetUpdateProcessor implements Processor {

	private static final Logger logger = LoggerFactory.getLogger(NeuralNetCreateProcessor.class);

	@Autowired
	protected Cache<Long, Network> networkCache;

	// CanberraDistance, ChebyshevDistance, EarthMoversDistance, EuclideanDistance,
	// ManhattanDistance
	private DistanceMeasure getDistanceMeasure(NeuralNetUpdateRequest updateRequest) {
		switch (updateRequest.getDistanceFunction()) {
		case "CanberraDistance":
			return new CanberraDistance();
		case "ChebyshevDistance":
			return new ChebyshevDistance();
		case "EarthMoversDistance":
			return new EarthMoversDistance();
		case "ManhattanDistance":
			return new ManhattanDistance();
		case "EuclideanDistance":
		default:
			return new EuclideanDistance();
		}
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		NeuralNetUpdateRequest updateRequest = (NeuralNetUpdateRequest) exchange.getIn().getBody();
		
		logger.info("updateRequest: {}", updateRequest);
		
		KohonenUpdateAction kohonenUpdateAction = new KohonenUpdateAction(getDistanceMeasure(updateRequest),
				numOfCalls -> updateRequest.getLearningFactor(), numOfCalls -> updateRequest.getNeighbourhoodSize());
		double[] features = updateRequest.getFeatures().stream().mapToDouble(Double::doubleValue).toArray();
		Network neuralNet = networkCache.getIfPresent(updateRequest.getNeuralNetId());
		kohonenUpdateAction.update(neuralNet, features);
		exchange.getMessage().setBody(Triple.of(updateRequest.getNeuralNetId(), neuralNet,
				neuralNet.getNeurons(new Network.NeuronIdentifierComparator())));
	}
}
