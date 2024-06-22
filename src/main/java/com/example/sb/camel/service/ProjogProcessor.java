package com.example.sb.camel.service;

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.projog.api.Projog;
import org.projog.api.QueryResult;
import org.projog.core.predicate.PredicateFactory;
import org.projog.core.predicate.PredicateKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.sb.camel.CamelConstantEnum;
import com.github.benmanes.caffeine.cache.Cache;

@Component("projogProcessor")
public class ProjogProcessor implements Processor {

	private static final Logger logger = LoggerFactory.getLogger(ProjogProcessor.class);

	@Autowired
	protected Cache<UUID, Projog> projogCache;

	@SuppressWarnings("unchecked")
	private Projog createProjog(Map<String, Object> requestBody) {
		Projog projog = new Projog();
		List<Map<String, Object>> predicates = (List<Map<String, Object>>) requestBody
				.get(CamelConstantEnum.CONST_PREDICATES.toString());
		if (predicates != null) {
			predicates.stream().forEach(predicateMap -> {
				String name = (String) predicateMap.get(CamelConstantEnum.CONST_PREDICATE_NAME.toString());
				Integer numArgs = (Integer) predicateMap.get(CamelConstantEnum.CONST_PREDICATE_ARGS.toString());
				String factoryName = (String) predicateMap.get(CamelConstantEnum.CONST_PREDICATE_FACTORY.toString());
				PredicateFactory predicateFactory;
				try {
					predicateFactory = (PredicateFactory) Class.forName(factoryName).getDeclaredConstructor()
							.newInstance();
					projog.addPredicateFactory(new PredicateKey(name, numArgs), predicateFactory);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException
						| ClassNotFoundException e) {
					logger.error("projog init factory error!", e);
				}
			});
		}
		return projog;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		Map<String, Object> requestBody = (Map<String, Object>) exchange.getIn().getBody();

		UUID uuid = UUID.randomUUID();

		if (requestBody.get(CamelConstantEnum.CONST_UUID.toString()) != null) {
			uuid = UUID.fromString((String) requestBody.get(CamelConstantEnum.CONST_UUID.toString()));
			if (projogCache.getIfPresent(uuid) == null) {
				Projog projog = createProjog(requestBody);
				String facts = String.join("\n",
						(List<String>) requestBody.get(CamelConstantEnum.CONST_FACTS.toString()));
				projog.consultReader(new StringReader(facts));
				projogCache.put(uuid, projog);
			}
		} else {
			Projog projog = createProjog(requestBody);
			String facts = String.join("\n", (List<String>) requestBody.get(CamelConstantEnum.CONST_FACTS.toString()));
			projog.consultReader(new StringReader(facts));
			projogCache.put(uuid, projog);
		}
		Projog projog = projogCache.getIfPresent(uuid);

		// Execute a query and iterate through all the results.
		QueryResult r1 = projog.executeQuery((String) requestBody.get(CamelConstantEnum.CONST_QUERY.toString()));
		List<String> result = new ArrayList<>();
		while (r1.next()) {
			result.add(String.join(", ",
					((List<String>) requestBody.get(CamelConstantEnum.CONST_VARIABLES.toString())).stream().map(var -> {
						return String.format("%s = %s", var, r1.getTerm(var));
					}).collect(Collectors.toList())));
		}

		exchange.getMessage().setBody(Map.of(CamelConstantEnum.CONST_UUID.toString(), uuid,
				CamelConstantEnum.CONST_RESULT.toString(), result));
	}

}
