package com.example.sb.camel.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.projog.api.Projog;
import org.projog.api.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Cache;

@Component("projogProcessor")
public class ProjogProcessor  implements Processor {

	@Autowired
	protected Cache<UUID, Projog> projogCache;
	
	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		Map<String, Object> requestBody = (Map<String, Object>) exchange.getIn().getBody();
		Projog projog = null;
		UUID uuid = UUID.randomUUID();
		if (requestBody.get("uuid") != null) {
			uuid = UUID.fromString((String) requestBody.get("uuid"));
			if (projogCache.getIfPresent(uuid) == null) {
				String facts = String.join("\n", (List<String>) requestBody.get("facts"));
				projog = new Projog();
				projog.consultReader(new StringReader(facts));
			} else {
				projog = projogCache.getIfPresent(uuid);
			}
		} else {
			String facts = String.join("\n", (List<String>) requestBody.get("facts"));
			projog = new Projog();
			projog.consultReader(new StringReader(facts));
		}
		projogCache.put(uuid, projog);
		
		// Execute a query and iterate through all the results.
		QueryResult r1 = projog.executeQuery((String) requestBody.get("query"));
		List<String> result = new ArrayList<>();
		while (r1.next()) {
			result.add(String.join(", ", ((List<String>) requestBody.get("variables")).stream().map(var -> {
				return String.format("%s = %s", var, r1.getTerm(var));
			}).collect(Collectors.toList())));
		}
		
		exchange.getMessage().setBody(Map.of("uuid", uuid, "result", result));
	}

}
