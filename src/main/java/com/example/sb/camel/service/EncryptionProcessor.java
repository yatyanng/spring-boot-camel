package com.example.sb.camel.service;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.stereotype.Component;

@Component("encryptionProcessor")
public class EncryptionProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword(exchange.getIn().getHeader("password").toString());
		config.setAlgorithm(exchange.getIn().getHeader("algorithm").toString());
		encryptor.setConfig(config);
		String encryptedText = encryptor.encrypt(exchange.getIn().getBody().toString());
		exchange.getMessage().setBody(encryptedText);
	}

}
