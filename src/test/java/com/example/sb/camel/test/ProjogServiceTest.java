package com.example.sb.camel.test;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.example.sb.camel.service.ProjogService;
import org.springframework.test.context.junit4.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitConfig(classes = TestConfig.class)
public class ProjogServiceTest {

	@Autowired
	@Qualifier("projogService")
	private ProjogService projogService;

	@Autowired
	private ApplicationContext applicationContext;

	@SuppressWarnings("unchecked")
	@Test
	public void testProjogServiceHappyCase() {
		Assert.assertTrue(applicationContext != null);
		Map<String, Object> payload = projogService.process(
				Map.of("variables", List.of("X"), "facts", List.of("knows(john,mary)."), "query", "knows(john,X)."));
		Assert.assertTrue(payload.containsKey("result"));
		Assert.assertTrue(payload.get("result") instanceof List);
		Assert.assertTrue(!((List<String>) payload.get("result")).isEmpty());
		Assert.assertTrue(((List<String>) payload.get("result")).get(0).equals("X = mary"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testProjogServiceReflectiveCase() {
		Assert.assertTrue(applicationContext != null);
		Map<String, Object> payload = projogService.process(Map.of("variables", List.of("X"), "facts",
				List.of("knows(john,mary).", "knows(X,Y):-knows(Y,X)."), "query", "knows(X,john)."));
		Assert.assertTrue(payload.containsKey("result"));
		Assert.assertTrue(payload.get("result") instanceof List);
		Assert.assertTrue(!((List<String>) payload.get("result")).isEmpty());
		Assert.assertTrue(((List<String>) payload.get("result")).get(0).equals("X = mary"));
	}
}
