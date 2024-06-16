package com.example.sb.camel.request;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class NeuralNetCreateRequest {

	int featureSize;
	List<List<Double>> neurons;
	List<List<Integer>> links;

	public int getFeatureSize() {
		return featureSize;
	}
	public void setFeatureSize(int featureSize) {
		this.featureSize = featureSize;
	}
	public List<List<Double>> getNeurons() {
		return neurons;
	}
	public void setNeurons(List<List<Double>> neurons) {
		this.neurons = neurons;
	}
	public List<List<Integer>> getLinks() {
		return links;
	}
	public void setLinks(List<List<Integer>> links) {
		this.links = links;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
