package com.example.sb.camel.request;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class NeuralNetUpdateRequest {

	Long neuralNetId;
	String distanceFunction;
	Double learningFactor;
	Integer neighbourhoodSize;
	List<Double> features;

	public Long getNeuralNetId() {
		return neuralNetId;
	}
	public void setNeuralNetId(Long neuralNetId) {
		this.neuralNetId = neuralNetId;
	}
	public String getDistanceFunction() {
		return distanceFunction;
	}
	public void setDistanceFunction(String distanceFunction) {
		this.distanceFunction = distanceFunction;
	}
	public Double getLearningFactor() {
		return learningFactor;
	}
	public void setLearningFactor(Double learningFactor) {
		this.learningFactor = learningFactor;
	}
	public Integer getNeighbourhoodSize() {
		return neighbourhoodSize;
	}
	public void setNeighbourhoodSize(Integer neighbourhoodSize) {
		this.neighbourhoodSize = neighbourhoodSize;
	}
	public List<Double> getFeatures() {
		return features;
	}
	public void setFeatures(List<Double> features) {
		this.features = features;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
